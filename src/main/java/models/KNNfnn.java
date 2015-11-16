package models;

import gui.tablemodels.DataTableModel;
import java.util.List;
import org.rosuda.JRI.REXP;
import models.params.KNNfnnParams;
import models.params.Params;
import utils.Const;
import utils.ErrorMeasuresCrisp;
import utils.ErrorMeasuresUtils;
import utils.MyRengine;
import utils.Utils;

public class KNNfnn implements Forecastable {
    private static final long serialVersionUID = 1L;

    @Override
    public TrainAndTestReport forecast(DataTableModel dataTableModel, Params parameters) {
        final String NBRS_NO_TEST = Const.NEIGHBOURS + Utils.getCounter();
        final String NBRS_WITH_TEST = Const.NEIGHBOURS + Utils.getCounter();
        final String INPUT_TRAIN = Const.INPUT + Utils.getCounter();
        final String INPUT_TEST = Const.INPUT + Utils.getCounter();
        final String OUTPUT_TRAIN = Const.OUTPUT + Utils.getCounter();
        final String PREDICTED_TRAIN = "predicted." + OUTPUT_TRAIN;
        final String OUTPUT_TEST = Const.OUTPUT + Utils.getCounter();
        final String PREDICTED_TEST = "predicted." + OUTPUT_TEST;
        final String INPUT = Const.INPUT + Utils.getCounter();
        final String OUTPUT = Const.OUTPUT + Utils.getCounter();
        final String PREDICTED_OUTPUT = Const.OUTPUT + Utils.getCounter();
        
        KNNfnnParams params = (KNNfnnParams) parameters;
        TrainAndTestReportCrisp report = new TrainAndTestReportCrisp(Const.KNN_FNN);
        report.setModelDescription(params.toString());
        
        List<Double> allData = dataTableModel.getDataForColname(params.getColName());
        List<Double> dataToUse = allData.subList((params.getDataRangeFrom() - 1), params.getDataRangeTo());

        MyRengine rengine = MyRengine.getRengine();
        rengine.require("FNN");
        
        final int LAG = 1;
        
        //vyrobit dva subory dat (vstupy, vystupy), lagnuty o prislusny lag (rovnaka dlzka)
        rengine.assign(INPUT, Utils.listToArray(dataToUse));
        rengine.assign(OUTPUT, Utils.listToArray(dataToUse));
        
        //potom z dlzky tychto suborov vypocitat numTrainingEntries podla percentTrain
        int numTrainingEntries = Math.round(((float) params.getPercentTrain()/100)*dataToUse.size());
        report.setNumTrainingEntries(numTrainingEntries);
        
        //potom si vyrobit trainingValues, testingValues
        rengine.eval(INPUT_TRAIN + " <- " + INPUT + "[1:" + numTrainingEntries + "]");
        rengine.eval(INPUT_TEST + " <- " + INPUT + "[" + (numTrainingEntries+1) + ":(length(" + INPUT + ")-1)]");
        
        rengine.eval(OUTPUT_TRAIN + " <- " + OUTPUT + "[2:" + (numTrainingEntries+1) + "]");
        rengine.eval(OUTPUT_TEST + " <- " + OUTPUT + "[" + (numTrainingEntries+2) + ":length(" + OUTPUT + ")]");
        
        //first run it without testing data - will give residuals for training data
        rengine.eval(NBRS_NO_TEST + " <- FNN::knn.reg(" + INPUT_TRAIN + ", y = " + OUTPUT_TRAIN
                                                    + ", k = " + params.getNumNeighbours() + ")");
        rengine.eval(PREDICTED_TRAIN + " <- " + NBRS_NO_TEST + "$pred");
        
        //then run it with testing data (TODO and later with forecasts)
        rengine.eval(NBRS_WITH_TEST + " <- FNN::knn.reg(train = " + INPUT_TRAIN + ", test = data.frame(" + INPUT_TEST
                                                   + "), y = " + OUTPUT_TRAIN + ", k = " + params.getNumNeighbours() + ")");
        rengine.eval(PREDICTED_TEST + " <- " + NBRS_WITH_TEST + "$pred");
        
        //cele to posunut podla lagu:
        rengine.eval(OUTPUT + " <- c(rep(NA, " + LAG + "), " + OUTPUT_TRAIN + ", " + OUTPUT_TEST + ")");
        rengine.eval(OUTPUT_TRAIN + " <- " + OUTPUT + "[1:" + numTrainingEntries + "]");
        rengine.eval(OUTPUT_TEST + " <- " + OUTPUT + "[" + (numTrainingEntries+1) + ":length(" + OUTPUT + ")]");
        
        REXP getTrainingOutputs = rengine.eval(OUTPUT_TRAIN);
        double[] trainingOutputs = getTrainingOutputs.asDoubleArray();
        
        REXP getTestingOutputs = rengine.eval(OUTPUT_TEST);
        double[] testingOutputs = getTestingOutputs.asDoubleArray();
        //-----
        rengine.eval(PREDICTED_OUTPUT + " <- c(rep(NA, " + LAG + "), " + PREDICTED_TRAIN + ", " + PREDICTED_TEST + ")");
        rengine.eval(PREDICTED_TRAIN + " <- " + PREDICTED_OUTPUT + "[1:" + numTrainingEntries + "]");
        rengine.eval(PREDICTED_TEST + " <- " + PREDICTED_OUTPUT + "[" + (numTrainingEntries+1) + ":length(" + PREDICTED_OUTPUT + ")]");
        
        REXP getTrainingPredicted = rengine.eval(PREDICTED_TRAIN);
        double[] trainingPredicted = getTrainingPredicted.asDoubleArray();
        
        REXP getTestingPredicted = rengine.eval(PREDICTED_TEST);
        double[] testingPredicted = getTestingPredicted.asDoubleArray();
        
        //then compute ErrorMeasures
        //TODO check the error measures, pretoze si myslim, ze to napriklad nepocita s tym posunom kvoli lagu. a potom
        // napriklad pre test data mam len par pozorovani, ale priemerne errory delim celkovym poctom testov, a vychadzaju
        // mi velmi male errory. bud je chybny graf alebo pocitanie errorov, pretoze na grafe su vyssie hodnoty
        ErrorMeasuresCrisp errorMeasures = ErrorMeasuresUtils.computeAllErrorMeasuresCrisp(Utils.arrayToList(trainingOutputs), 
                Utils.arrayToList(testingOutputs), Utils.arrayToList(trainingPredicted), Utils.arrayToList(testingPredicted), 
                parameters.getSeasonality());
        
        report.setErrorMeasures(errorMeasures);
        
        //then report.setEverything
        report.setFittedValues(trainingPredicted);
        report.setForecastValuesTest(testingPredicted);
//        report.setForecastValuesFuture(); //nothing yet
        
        report.setRealOutputsTrain(trainingOutputs);
        report.setRealOutputsTest(testingOutputs);
        
        report.setPlotCode("plot.ts(c(" + Utils.arrayToRVectorString(trainingPredicted) + ", " + Utils.arrayToRVectorString(testingPredicted) + "))");
        
        rengine.rm(NBRS_NO_TEST, NBRS_WITH_TEST, INPUT_TRAIN, INPUT_TEST, OUTPUT_TRAIN, OUTPUT_TEST, INPUT, OUTPUT, PREDICTED_OUTPUT, PREDICTED_TRAIN, PREDICTED_TEST);
        
        return report;
    }

    @Override
    public String getOptionalParams(Params parameters) {
        return "";
    }
}
