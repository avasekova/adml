package models;

import gui.tablemodels.DataTableModel;
import java.util.List;
import org.rosuda.JRI.REXP;
import models.params.KNNkknnParams;
import models.params.Params;
import utils.Const;
import utils.ErrorMeasuresCrisp;
import utils.ErrorMeasuresUtils;
import utils.MyRengine;
import utils.Utils;

public class KNNkknn implements Forecastable {

    @Override
    public TrainAndTestReport forecast(DataTableModel dataTableModel, Params parameters) {
        final String INPUT = Const.INPUT + Utils.getCounter();
        final String OUTPUT = Const.OUTPUT + Utils.getCounter();
        final String INPUT_TRAIN = Const.INPUT + Utils.getCounter();
        final String INPUT_TEST = Const.INPUT + Utils.getCounter();
        final String OUTPUT_TRAIN = Const.OUTPUT + Utils.getCounter();
        final String OUTPUT_TEST = Const.OUTPUT + Utils.getCounter();
        final String MODEL = Const.MODEL + Utils.getCounter();
        final String PREDICTED_OUTPUT = Const.OUTPUT + Utils.getCounter();
        final String FORECAST_VALS = Const.FORECAST_VALS + Utils.getCounter();
        final String FITTED_VALS = Const.FIT + Utils.getCounter();
        final String BEST_K = "bestk" + Utils.getCounter();
        
        KNNkknnParams params = (KNNkknnParams) parameters;
        
        List<Double> allData = dataTableModel.getDataForColname(params.getColName());
        List<Double> dataToUse = allData.subList((params.getDataRangeFrom() - 1), params.getDataRangeTo());

        MyRengine rengine = MyRengine.getRengine();
        rengine.require("kknn");
        
        final int LAG = 1;
        
        rengine.assign(INPUT, Utils.listToArray(dataToUse));
        rengine.assign(OUTPUT, Utils.listToArray(dataToUse));
        
        int numTrainingEntries = Math.round(((float) params.getPercentTrain()/100)*dataToUse.size());
        
        rengine.eval(INPUT_TRAIN + " <- " + INPUT + "[1:" + numTrainingEntries + "]");
        rengine.eval(INPUT_TEST + " <- " + INPUT + "[" + (numTrainingEntries+1) + ":(length(" + INPUT + ")-1)]");
        rengine.eval(OUTPUT_TRAIN + " <- " + OUTPUT + "[2:" + (numTrainingEntries+1) + "]");
        rengine.eval(OUTPUT_TEST + " <- " + OUTPUT + "[" + (numTrainingEntries+2) + ":length(" + OUTPUT + ")]");
        
        //nescalujem, dufam, ze nebude treba
        
        rengine.eval(MODEL + " <- kknn::train.kknn(" + OUTPUT_TRAIN + " ~ " + INPUT_TRAIN + ", data.frame(" + INPUT_TRAIN + ", " 
                + OUTPUT_TRAIN + "), kmax = " + params.getMaxNeighbours() + ", distance = 2)"); //dist=2 = Euclidean dist
        
        //fitted.values(model)[model$best.parameters$k][[1]][1:51]     - dalo zabrat na to prist, tak to nemazat...
        rengine.eval(FITTED_VALS + " <- fitted.values(" + MODEL + ")[" + MODEL + "$best.parameters$k][[1]][1:" + numTrainingEntries + "]");
        
        //test data forecasts
        //pozor, treba nazvat tie stlpce v data.frame tak isto, ako sa volaju vo formulke v train.kknn - zato tu je IN/OUT_TRAIN
        rengine.eval(FORECAST_VALS + " <- predict(" + MODEL + ", data.frame(" + INPUT_TRAIN + " = " + INPUT_TEST + ", "
                                                                              + OUTPUT_TRAIN + " = " + OUTPUT_TEST + "))");
        
        rengine.eval(BEST_K + " <- " + MODEL + "$best.parameters$k");
        REXP getBestK = rengine.eval(BEST_K);
        double[] bestKarray = getBestK.asDoubleArray();
        long bestK = Math.round(bestKarray[0]); //will be integer anyway
        params.setBestNumNeighbours(bestK);
        
        //cele to posunut podla lagu:
        rengine.eval(OUTPUT + " <- c(rep(NA, " + LAG + "), " + OUTPUT_TRAIN + ", " + OUTPUT_TEST + ")");
        rengine.eval(OUTPUT_TRAIN + " <- " + OUTPUT + "[1:" + numTrainingEntries + "]");
        rengine.eval(OUTPUT_TEST + " <- " + OUTPUT + "[" + (numTrainingEntries+1) + ":length(" + OUTPUT + ")]");
        
        REXP getTrainingOutputs = rengine.eval(OUTPUT_TRAIN);
        double[] trainingOutputs = getTrainingOutputs.asDoubleArray();
        
        REXP getTestingOutputs = rengine.eval(OUTPUT_TEST);
        double[] testingOutputs = getTestingOutputs.asDoubleArray();
        //-----
        rengine.eval(PREDICTED_OUTPUT + " <- c(rep(NA, " + LAG + "), " + FITTED_VALS + ", " + FORECAST_VALS + ")");
        rengine.eval(FITTED_VALS + " <- " + PREDICTED_OUTPUT + "[1:" + numTrainingEntries + "]");
        rengine.eval(FORECAST_VALS + " <- " + PREDICTED_OUTPUT + "[" + (numTrainingEntries+1) + ":length(" + PREDICTED_OUTPUT + ")]");
        
        REXP getTrainingPredicted = rengine.eval(FITTED_VALS);
        double[] trainingPredicted = getTrainingPredicted.asDoubleArray();
        
        REXP getTestingPredicted = rengine.eval(FORECAST_VALS);
        double[] testingPredicted = getTestingPredicted.asDoubleArray();
        
        
        ErrorMeasuresCrisp errorMeasures = ErrorMeasuresUtils.computeAllErrorMeasuresCrisp(Utils.arrayToList(trainingOutputs), 
                Utils.arrayToList(testingOutputs), Utils.arrayToList(trainingPredicted), Utils.arrayToList(testingPredicted), 
                parameters.getSeasonality());
        
        TrainAndTestReportCrisp report = new TrainAndTestReportCrisp(Const.KNN_KKNN); //MODEL$best.parameters$k
        report.setModelDescription(params.toString());
        report.setNumTrainingEntries(numTrainingEntries);
        report.setFittedValues(trainingPredicted);
        report.setForecastValuesTest(testingPredicted); //TODO add forecasts...
        
        report.setRealOutputsTrain(trainingOutputs);
        report.setRealOutputsTest(testingOutputs);
        
//        report.setForecastValuesFuture(); //nothing yet
        report.setPlotCode("plot.ts(c(" + FITTED_VALS + ", " + FORECAST_VALS + "))");
        report.setErrorMeasures(errorMeasures);
        
        rengine.rm(INPUT, OUTPUT, INPUT_TRAIN, INPUT_TEST, OUTPUT_TRAIN, OUTPUT_TEST, MODEL, PREDICTED_OUTPUT, BEST_K); //POZOR, nemazat FIT a FORECAST
        
        return report;
    }

    @Override
    public String getOptionalParams(Params parameters) {
        return "";
    }
}
