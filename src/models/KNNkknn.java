package models;

import gui.tablemodels.DataTableModel;
import java.util.List;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;
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
        final String FORECAST_VALS = Const.FORECAST_VALS + Utils.getCounter();
        final String FITTED_VALS = Const.FIT + Utils.getCounter();
        final String BEST_K = "bestk" + Utils.getCounter();
        
        KNNkknnParams params = (KNNkknnParams) parameters;
        
        List<Double> allData = dataTableModel.getDataForColname(params.getColName());
        List<Double> dataToUse = allData.subList((params.getDataRangeFrom() - 1), params.getDataRangeTo());

        Rengine rengine = MyRengine.getRengine();
        rengine.eval("require(kknn)");
        
        rengine.assign(INPUT, Utils.listToArray(dataToUse));
        rengine.assign(OUTPUT, Utils.listToArray(dataToUse));
        rengine.eval(INPUT + " <- " + INPUT + "[1:(length(" + INPUT + ") - " + params.getLag() + ")]"); //1:(length-lag)
        rengine.eval(OUTPUT + " <- " + OUTPUT + "[(1 + " + params.getLag() + "):length(" + OUTPUT + ")]"); //(1+lag):length
        
        int numTrainingEntries = Math.round(((float) params.getPercentTrain()/100)*dataToUse.size());
        
        rengine.eval(INPUT_TRAIN + " <- " + INPUT + "[1:" + numTrainingEntries + "]");
        rengine.eval(INPUT_TEST + " <- " + INPUT + "[" + (numTrainingEntries+1) + ":length(" + INPUT + ")]");
        rengine.eval(OUTPUT_TRAIN + " <- " + OUTPUT + "[1:" + numTrainingEntries + "]");
        rengine.eval(OUTPUT_TEST + " <- " + OUTPUT + "[" + (numTrainingEntries+1) + ":length(" + OUTPUT + ")]");
        
        //nescalujem, dufam, ze nebude treba
        
        rengine.eval(MODEL + " <- kknn::train.kknn(" + OUTPUT_TRAIN + " ~ " + INPUT_TRAIN + ", data.frame(" + INPUT_TRAIN + ", " 
                + OUTPUT_TRAIN + "), kmax = " + params.getMaxNeighbours() + ", distance = 2)"); //dist=2 = Euclidean dist
        
        //fitted.values(model)[model$best.parameters$k][[1]][1:51]     - dalo zabrat na to prist, tak to nemazat...
        rengine.eval(FITTED_VALS + " <- fitted.values(" + MODEL + ")[" + MODEL + "$best.parameters$k][[1]][1:" + numTrainingEntries + "]");
        REXP getTrainingVals = rengine.eval(FITTED_VALS);
        double[] trainingVals = getTrainingVals.asDoubleArray();
        
        //test data forecasts
        //pozor, treba nazvat tie stlpce v data.frame tak isto, ako sa volaju vo formulke v train.kknn - zato tu je IN/OUT_TRAIN
        rengine.eval(FORECAST_VALS + " <- predict(" + MODEL + ", data.frame(" + INPUT_TRAIN + " = " + INPUT_TEST + ", "
                                                                              + OUTPUT_TRAIN + " = " + OUTPUT_TEST + "))");
        //velmi dufam, ze to bude fungovat, a nie ze to interpretuje tie IN/OUT_TRAIN ako hodnoty miesto nazvov :(
        REXP getTestingVals = rengine.eval(FORECAST_VALS);
        double[] testingVals = getTestingVals.asDoubleArray();
        
        rengine.eval(BEST_K + " <- " + MODEL + "$best.parameters$k");
        REXP getBestK = rengine.eval(BEST_K);
        double[] bestKarray = getBestK.asDoubleArray();
        long bestK = Math.round(bestKarray[0]); //will be integer anyway
        
        
        rengine.eval(OUTPUT + " <- c(rep(NA, " + params.getLag() + "), " + OUTPUT + ")");
        rengine.eval(OUTPUT_TRAIN + " <- " + OUTPUT + "[1:" + numTrainingEntries + "]");
        rengine.eval(OUTPUT_TEST + " <- " + OUTPUT + "[" + (numTrainingEntries+1) + ":length(" + OUTPUT + ")]");
        REXP getOutputTrain = rengine.eval(OUTPUT_TRAIN);
        double[] outputTrain = getOutputTrain.asDoubleArray();
        REXP getOutputTest = rengine.eval(OUTPUT_TEST);
        double[] outputTest = getOutputTest.asDoubleArray();
        
        
        ErrorMeasuresCrisp errorMeasures = ErrorMeasuresUtils.computeAllErrorMeasuresCrisp(Utils.arrayToList(outputTrain), 
                Utils.arrayToList(outputTest), Utils.arrayToList(trainingVals), Utils.arrayToList(testingVals), 
                parameters.getSeasonality());
        
        TrainAndTestReportCrisp report = new TrainAndTestReportCrisp(Const.KNN_KKNN); //MODEL$best.parameters$k
        report.setModelDescription("(" + bestK + ")");
        report.setNumTrainingEntries(numTrainingEntries);
        report.setFittedValues(trainingVals);
        report.setForecastValuesTest(testingVals); //TODO add forecasts...
        
        report.setRealOutputsTrain(outputTrain);
        report.setRealOutputsTest(outputTest);
        
//        report.setForecastValuesFuture(); //nothing yet
        report.setPlotCode("plot.ts(c(rep(NA, " + params.getLag() + "), " + FITTED_VALS + ", " + FORECAST_VALS + "))");
        report.setErrorMeasures(errorMeasures);
        
        return report;
    }

    @Override
    public String getOptionalParams(Params parameters) {
        return "";
    }
}
