package models;

import models.params.KNNfnnParams;
import models.params.Params;
import utils.*;

import java.util.List;
import java.util.Map;

public class KNNfnn implements Forecastable {
    private static final long serialVersionUID = 1L;

    @Override
    public TrainAndTestReport forecast(Map<String, List<Double>> dataTableModel, Params parameters) {
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
        TrainAndTestReportCrisp report = new TrainAndTestReportCrisp(Model.KNN_FNN);
        report.setModelDescription(params.toString());
        
        List<Double> allData = dataTableModel.get(params.getColName());
        List<Double> dataToUse = allData.subList((params.getDataRangeFrom() - 1), params.getDataRangeTo());

        MyRengine rengine = MyRengine.getRengine();
        rengine.require("FNN");
        
        final int LAG = 1;
        
        //create two sets of data (inputs, outputs), lagged by the specified lag (the same length)
        rengine.assign(INPUT, Utils.listToArray(dataToUse));
        rengine.assign(OUTPUT, Utils.listToArray(dataToUse));
        
        //then compute numTraining bsd on percentTrain
        int numTrainingEntries = Math.round(((float) params.getPercentTrain()/100)*dataToUse.size());
        report.setNumTrainingEntries(numTrainingEntries);
        
        //then create trainingValues, testingValues
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
        
        //shift it all by lag:
        rengine.eval(OUTPUT + " <- c(rep(NA, " + LAG + "), " + OUTPUT_TRAIN + ", " + OUTPUT_TEST + ")");
        rengine.eval(OUTPUT_TRAIN + " <- " + OUTPUT + "[1:" + numTrainingEntries + "]");
        rengine.eval(OUTPUT_TEST + " <- " + OUTPUT + "[" + (numTrainingEntries+1) + ":length(" + OUTPUT + ")]");

        double[] trainingOutputs = rengine.evalAndReturnArray(OUTPUT_TRAIN);
        double[] testingOutputs = rengine.evalAndReturnArray(OUTPUT_TEST);
        //-----
        rengine.eval(PREDICTED_OUTPUT + " <- c(rep(NA, " + LAG + "), " + PREDICTED_TRAIN + ", " + PREDICTED_TEST + ")");
        rengine.eval(PREDICTED_TRAIN + " <- " + PREDICTED_OUTPUT + "[1:" + numTrainingEntries + "]");
        rengine.eval(PREDICTED_TEST + " <- " + PREDICTED_OUTPUT + "[" + (numTrainingEntries+1) + ":length(" + PREDICTED_OUTPUT + ")]");

        double[] trainingPredicted = rengine.evalAndReturnArray(PREDICTED_TRAIN);
        double[] testingPredicted = rengine.evalAndReturnArray(PREDICTED_TEST);
        
        //then compute ErrorMeasures
        //TODO check the error measures; I think it may not take into account the shift due to the lag. and then
        // e.g. for the test data we only have a few observations, but the avg errors are divided by the num of tests, and
        // we get very small errors. either the plot is incorrect or the computation of the errors, because the plot shows higher vals
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
}
