package models;

import models.params.Params;
import models.params.SESParams;
import org.rosuda.JRI.REXP;
import utils.*;

import java.util.List;
import java.util.Map;

public class SES implements Forecastable {
    private static final long serialVersionUID = 1L;

    @Override
    public TrainAndTestReport forecast(Map<String, List<Double>> dataTableModel, Params parameters) {
        final String FORECAST_MODEL = Const.FORECAST_MODEL + Utils.getCounter();
        final String INPUT_TRAIN = Const.INPUT + Utils.getCounter();
        final String FIT = Const.FIT + Utils.getCounter();
        final String FORECAST = Const.FORECAST_VALS + Utils.getCounter();
        
        SESParams params = (SESParams) parameters;
        
        List<Double> allData = dataTableModel.get(params.getColName());
        List<Double> dataToUse = allData.subList((params.getDataRangeFrom() - 1), params.getDataRangeTo());
        
        MyRengine rengine = MyRengine.getRengine();
        rengine.require("forecast");
        int numTrainingEntries = Math.round(((float) params.getPercentTrain()/100)*dataToUse.size());
        
        List<Double> inputTrain = dataToUse.subList(0, numTrainingEntries);
        List<Double> inputTest = dataToUse.subList(numTrainingEntries, dataToUse.size());
        
        rengine.assign(INPUT_TRAIN, Utils.listToArray(inputTrain));
        
        int num4castsTestAndFuture = inputTest.size() + params.getNumForecasts();
        rengine.eval(FORECAST_MODEL + " <- forecast::ses(" + INPUT_TRAIN + ", h=" + num4castsTestAndFuture + 
                ", alpha=" + params.getAlpha() + ")");
        
        rengine.eval(FIT + " <- fitted(" + FORECAST_MODEL + ")[1:" + inputTrain.size() + "]");
        REXP getFittedVals = rengine.eval(FIT);
        double[] fittedVals = getFittedVals.asDoubleArray();
        
        rengine.eval(FORECAST + " <- data.frame(" + FORECAST_MODEL + ")[\"Point.Forecast\"][1:" + num4castsTestAndFuture + ",]"); //if somebody renames this field in the next version, well...
        REXP getForecastTestAndFuture = rengine.eval(FORECAST);
        List<Double> forecastTestAndFuture = Utils.arrayToList(getForecastTestAndFuture.asDoubleArray());
        List<Double> forecastTest = forecastTestAndFuture.subList(0, inputTest.size());
        List<Double> forecastFuture = forecastTestAndFuture.subList(inputTest.size(), forecastTestAndFuture.size());
        
        REXP getFinalAlpha = rengine.eval(FORECAST_MODEL + "$model$par[\"alpha\"]");
        double finalAlpha = getFinalAlpha.asDoubleArray()[0];
        
        TrainAndTestReportCrisp report = new TrainAndTestReportCrisp(Const.SES);
        report.setModelDescription(params.toString());
        
        report.setNumTrainingEntries(numTrainingEntries);
        report.setFittedValues(fittedVals);
        report.setForecastValuesTest(Utils.listToArray(forecastTest));
        report.setForecastValuesFuture(Utils.listToArray(forecastFuture));
        report.setPlotCode("plot.ts(c(" + Utils.arrayToRVectorString(fittedVals) + "," + Utils.listToRVectorString(forecastTestAndFuture) + "))");
        report.setRealOutputsTrain(Utils.listToArray(inputTrain));
        report.setRealOutputsTest(Utils.listToArray(inputTest));
        
        ErrorMeasuresCrisp errorMeasures = ErrorMeasuresUtils.computeAllErrorMeasuresCrisp(inputTrain, inputTest, 
                Utils.arrayToList(fittedVals), forecastTest, params.getSeasonality());
        report.setErrorMeasures(errorMeasures);
        
        rengine.rm(FORECAST_MODEL, INPUT_TRAIN, FIT, FORECAST);
        
        return report;
    }

    @Override
    public String getOptionalParams(Params parameters) {
        return "";
    }
}
