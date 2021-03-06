package models;

import models.params.ArimaParams;
import models.params.Params;
import utils.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Arima implements Forecastable {
    private static final long serialVersionUID = 1L;

    @Override
    public TrainAndTestReport forecast(Map<String, List<Double>> data, Params parameters) {
        final String INPUT = Const.INPUT + Utils.getCounter();
        final String SCALED_INPUT = "scaled." + INPUT;
        final String INPUT_TRAIN = Const.INPUT + Utils.getCounter();
        final String SCALED_INPUT_TRAIN = "scaled." + INPUT_TRAIN;
        final String INPUT_TEST = Const.INPUT + Utils.getCounter();
        final String SCALED_INPUT_TEST = "scaled." + INPUT_TEST;
        final String FORECAST_VALS = Const.FORECAST_VALS + Utils.getCounter();
        final String FORECAST_VALS_CUT = FORECAST_VALS + ".cut";
        final String UNSCALED_FORECAST_VALS = "unscaled." + FORECAST_VALS;
        final String FITTED_VALS = Const.FIT + Utils.getCounter();
        final String UNSCALED_FITTED_VALS = "unscaled." + FITTED_VALS;
        final String PRED_INT_LOWER = Const.OUTPUT + Utils.getCounter();
        final String PRED_INT_UPPER = Const.OUTPUT + Utils.getCounter();
        
        final String MODEL = Const.MODEL + Utils.getCounter();
        
        ArimaParams params = (ArimaParams) parameters;
        
        List<Double> allData = data.get(params.getColName());
        List<Double> dataToUse = allData.subList((params.getDataRangeFrom() - 1), params.getDataRangeTo());

        MyRengine rengine = MyRengine.getRengine();
        rengine.require("forecast");
        
        rengine.assign(INPUT, Utils.listToArray(dataToUse));
        rengine.eval(SCALED_INPUT + " <- MLPtoR.scale(" + INPUT + ")");
        
        int numTrainingEntries = Math.round(((float) params.getPercentTrain()/100)*dataToUse.size());
        
        rengine.eval(INPUT_TRAIN +        " <- " +        INPUT + "[1:" + numTrainingEntries + "]");
        rengine.eval(SCALED_INPUT_TRAIN + " <- " + SCALED_INPUT + "[1:" + numTrainingEntries + "]");
        rengine.eval(INPUT_TEST +         " <- " +        INPUT + "[" + (numTrainingEntries+1) + ":" + dataToUse.size() + "]");
        rengine.eval(SCALED_INPUT_TEST +  " <- " + SCALED_INPUT + "[" + (numTrainingEntries+1) + ":" + dataToUse.size() + "]");
        
        String arimaDescription;
        if (params.isOptimize()) {
            rengine.eval(MODEL + " <- forecast::auto.arima(" + SCALED_INPUT_TRAIN + ")");
            
            //extract the order of ARIMA:
            final String ORDER = Const.INPUT + Utils.getCounter();
            rengine.eval(ORDER + " <- " + MODEL + "$arma");
            double[] allArray = rengine.evalAndReturnArray(ORDER);
            arimaDescription = "(" + allArray[0] + ", " + allArray[5] + ", " + allArray[1] + ")(" + allArray[2]
                               + ", " + allArray[6] + ", " + allArray[3] + ")";
            
            rengine.rm(ORDER);
        } else {
            rengine.eval(MODEL + " <- forecast::Arima(" + SCALED_INPUT_TRAIN + ", order = c(" + params.getNonSeasPotato() + ", "
                                                                       + params.getNonSeasDonkey() + ", "
                                                                       + params.getNonSeasQuark()
                                     + "), seasonal = list(order=c(" + params.getSeasPotato() + ", "
                                                                       + params.getSeasDonkey() + ", "
                                                                       + params.getNonSeasQuark()
                                     + "), period=NA), include.constant = " + Utils.booleanToRBool(params.isWithConstant()) + ", "
                                     + "method=\"ML\")");
            arimaDescription = "(" + params.getNonSeasPotato() + "," + 
                params.getNonSeasDonkey() + "," + params.getNonSeasQuark() + ")(" + params.getSeasPotato() + "," +
                params.getSeasDonkey() + "," + params.getSeasQuark() + ")";
            if (params.isWithConstant()) {
                arimaDescription += ",const.";
            }
        }
        
        rengine.eval(FITTED_VALS + " <- fitted.values(" + MODEL + ")");
        rengine.eval(UNSCALED_FITTED_VALS + " <- MLPtoR.unscale(" + FITTED_VALS + ", " + INPUT + ")");
        double[] fitted = rengine.evalAndReturnArray(UNSCALED_FITTED_VALS);
        if (fitted == null) {
            return null;
        }

        //"forecast" testing data
        //in case I was wondering about this again: yes, it does compute the forecasts _directly_ (not iteratively using
        //  forecast values in the subsequent forecasts)
        int numForecasts = dataToUse.size() - numTrainingEntries + params.getNumForecasts();
        if (params.getPredIntPercent() == 0) {
            rengine.eval(FORECAST_VALS + " <- forecast::forecast(" + MODEL + ", h = " + numForecasts + ")"); //predict all
        } else {
            rengine.eval(FORECAST_VALS + " <- forecast::forecast(" + MODEL + ", h = " + numForecasts + ", " +
                "level=" + params.getPredIntPercent() + ")"); //predict all
        }
        
        //take all from forecasted vals (a part of them is from test data, a part from future)
        rengine.eval(FORECAST_VALS_CUT + " <- " + FORECAST_VALS + "$mean[1:" + numForecasts + "]");
        rengine.eval(UNSCALED_FORECAST_VALS + " <- MLPtoR.unscale(" + FORECAST_VALS_CUT + ", " + INPUT + ")");
        double[] allForecasts = rengine.evalAndReturnArray(UNSCALED_FORECAST_VALS);
        //TODO avoid this conversion array<->list whenever possible - can slow it down for huge data
        List<Double> allForecastsList = Utils.arrayToList(allForecasts);
        
        //compute the error measures just from test data, you can't do it for the future
        double[] trainingPortionOfDataArray = rengine.evalAndReturnArray(INPUT_TRAIN);
        List<Double> trainingPortionOfData = Utils.arrayToList(trainingPortionOfDataArray);
        double[] testingPortionOfDataArray = rengine.evalAndReturnArray(INPUT_TEST);
        List<Double> testingPortionOfData = Utils.arrayToList(testingPortionOfDataArray);
        
        double[] forecastsTest = Arrays.copyOf(allForecasts, testingPortionOfData.size());
        ErrorMeasuresCrisp errorMeasures = ErrorMeasuresUtils.computeAllErrorMeasuresCrisp(trainingPortionOfData, 
                testingPortionOfData, Utils.arrayToList(fitted), Utils.arrayToList(forecastsTest), params.getSeasonality());
        
        TrainAndTestReportCrisp report = new TrainAndTestReportCrisp(Model.ARIMA);
        report.setModelDescription(arimaDescription + "\n" + params.toString()); //TODO cleanup
        report.setNumTrainingEntries(numTrainingEntries);
        report.setFittedValues(fitted);
        
        report.setRealOutputsTrain(trainingPortionOfDataArray);
        report.setRealOutputsTest(testingPortionOfDataArray);
        
        report.setForecastValuesTest(Utils.listToArray(allForecastsList.subList(0, dataToUse.size() - numTrainingEntries)));
        report.setForecastValuesFuture(Utils.listToArray(allForecastsList.subList(dataToUse.size() - numTrainingEntries, allForecastsList.size())));
        
        report.setPlotCode("plot.ts(c(" + Utils.arrayToRVectorString(fitted) + ", " + Utils.listToRVectorString(allForecastsList) + "))");
        
        report.setErrorMeasures(errorMeasures);
        
        
        
        
        
        //prediction intervals:
        if ((params.getPredIntPercent() != 0)) {
            rengine.eval(PRED_INT_LOWER + " <- data.frame(" + FORECAST_VALS + ")[\"Lo." + params.getPredIntPercent() + "\"]"
                    + "[1:" + numForecasts + ",]");
            rengine.eval(PRED_INT_UPPER + " <- data.frame(" + FORECAST_VALS + ")[\"Hi." + params.getPredIntPercent() + "\"]"
                    + "[1:" + numForecasts + ",]");
            double[] predIntLowers = rengine.evalAndReturnArray("MLPtoR.unscale(" + PRED_INT_LOWER + "," + INPUT + ")");
            double[] predIntUppers = rengine.evalAndReturnArray("MLPtoR.unscale(" + PRED_INT_UPPER + "," + INPUT + ")");
            report.setPredictionIntervalsLowers(predIntLowers);
            report.setPredictionIntervalsUppers(predIntUppers);
        }
        
        
        rengine.rm(INPUT, SCALED_INPUT, INPUT_TRAIN, SCALED_INPUT_TRAIN, INPUT_TEST, SCALED_INPUT_TEST, MODEL, FITTED_VALS, 
                FORECAST_VALS, FORECAST_VALS_CUT, PRED_INT_LOWER, PRED_INT_UPPER, UNSCALED_FITTED_VALS, UNSCALED_FORECAST_VALS);
        
        return report;
    }
    
}
