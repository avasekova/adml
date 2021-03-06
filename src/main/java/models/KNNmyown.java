package models;

import models.params.KNNfnnParams;
import models.params.Params;
import utils.Const;
import utils.MyRengine;
import utils.Utils;

import java.util.List;
import java.util.Map;

public class KNNmyown implements Forecastable {
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
        
        rengine.assign(INPUT, Utils.listToArray(dataToUse));
//        rengine.assign(OUTPUT, Utils.listToArray(dataToUse));
        
        int numTrainingEntries = Math.round(((float) params.getPercentTrain()/100)*dataToUse.size());
        report.setNumTrainingEntries(numTrainingEntries);
        
        rengine.eval(INPUT_TRAIN + " <- " + INPUT + "[1:" + numTrainingEntries + "]");
        rengine.eval(INPUT_TEST + " <- " + INPUT + "[" + (numTrainingEntries+1) + ":(length(" + INPUT + ")-1)]");
        
//        rengine.eval(OUTPUT_TRAIN + " <- " + OUTPUT + "[2:" + (numTrainingEntries+1) + "]");
//        rengine.eval(OUTPUT_TEST + " <- " + OUTPUT + "[" + (numTrainingEntries+2) + ":length(" + OUTPUT + ")]");
        
        //first run it without testing data - will give residuals for training data
        //knn.simple(train, test = NULL, distance.fun, k)
        rengine.eval(PREDICTED_TRAIN + " <- knn.simple(train=" + INPUT_TRAIN + ", distance.fun = function(x,y) {abs(x-y)}"
                                                    + ", k = " + params.getNumNeighbours() + ")");
        
        //then run it with testing data (TODO and later with forecasts)
//        rengine.eval(NBRS_WITH_TEST + " <- FNN::knn.reg(train = " + INPUT_TRAIN + ", test = data.frame(" + INPUT_TEST
//                                                   + "), y = " + OUTPUT_TRAIN + ", k = " + params.getNumNeighbours() + ")");
//        rengine.eval(PREDICTED_TEST + " <- " + NBRS_WITH_TEST + "$pred");
        
        //shift it all by lag:
//        rengine.eval(OUTPUT + " <- c(rep(NA, " + LAG + "), " + OUTPUT_TRAIN + ", " + OUTPUT_TEST + ")");
//        rengine.eval(OUTPUT_TRAIN + " <- " + OUTPUT + "[1:" + numTrainingEntries + "]");
//        rengine.eval(OUTPUT_TEST + " <- " + OUTPUT + "[" + (numTrainingEntries+1) + ":length(" + OUTPUT + ")]");
//        
//        REXP getTrainingOutputs = rengine.eval(OUTPUT_TRAIN);
//        double[] trainingOutputs = getTrainingOutputs.asDoubleArray();
//        
//        REXP getTestingOutputs = rengine.eval(OUTPUT_TEST);
//        double[] testingOutputs = getTestingOutputs.asDoubleArray();
        //-----
        rengine.eval(PREDICTED_OUTPUT + " <- c(rep(NA, " + LAG + "), " + PREDICTED_TRAIN + ", " + PREDICTED_TEST + ")");
        rengine.eval(PREDICTED_TRAIN + " <- " + PREDICTED_OUTPUT + "[1:" + numTrainingEntries + "]");
        rengine.eval(PREDICTED_TEST + " <- " + PREDICTED_OUTPUT + "[" + (numTrainingEntries+1) + ":length(" + PREDICTED_OUTPUT + ")]");

        double[] trainingPredicted = rengine.evalAndReturnArray(PREDICTED_TRAIN);
        double[] testingPredicted = rengine.evalAndReturnArray(PREDICTED_TEST);
        
        //then compute ErrorMeasures
        //TODO check the error measures
/*        ErrorMeasuresCrisp errorMeasures = ErrorMeasuresUtils.computeAllErrorMeasuresCrisp(Utils.arrayToList(trainingOutputs), 
                Utils.arrayToList(testingOutputs), Utils.arrayToList(trainingPredicted), Utils.arrayToList(testingPredicted), 
                parameters.getSeasonality());
        
        report.setErrorMeasures(errorMeasures);
        
        //then report.setEverything
        report.setFittedValues(trainingPredicted);
        report.setForecastValuesTest(testingPredicted);
//        report.setForecastValuesFuture(); //nothing yet
        
        report.setRealOutputsTrain(trainingOutputs);
        report.setRealOutputsTest(testingOutputs);
        
        report.setPlotCode("plot.ts(c(" + PREDICTED_TRAIN + ", " + PREDICTED_TEST + "))");
*/        
        return report;
    }
    
}
