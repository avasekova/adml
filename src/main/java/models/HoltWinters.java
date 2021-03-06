package models;

import models.params.HoltWintersParams;
import models.params.Params;
import utils.*;

import java.util.List;
import java.util.Map;

public class HoltWinters implements Forecastable {
    private static final long serialVersionUID = 1L;

    @Override
    public TrainAndTestReport forecast(Map<String, List<Double>> dataTableModel, Params parameters) {
        final String FORECAST_MODEL = Const.FORECAST_MODEL + Utils.getCounter();
        final String INPUT_TRAIN = Const.INPUT + Utils.getCounter();
        final String FIT = Const.FIT + Utils.getCounter();
        final String FORECAST = Const.FORECAST_VALS + Utils.getCounter();
        
        HoltWintersParams params = (HoltWintersParams) parameters;
        
        List<Double> allData = dataTableModel.get(params.getColName());
        List<Double> dataToUse = allData.subList((params.getDataRangeFrom() - 1), params.getDataRangeTo());
        
        MyRengine rengine = MyRengine.getRengine();
        rengine.require("forecast");
        int numTrainingEntries = Math.round(((float) params.getPercentTrain()/100)*dataToUse.size());
        
        List<Double> inputTrain = dataToUse.subList(0, numTrainingEntries);
        List<Double> inputTest = dataToUse.subList(numTrainingEntries, dataToUse.size());
        
        rengine.assign(INPUT_TRAIN, Utils.listToArray(inputTrain));
        
        //we need seasonal data
        rengine.eval(INPUT_TRAIN + " <- ts(" + INPUT_TRAIN + ", frequency=" + params.getFrequency() + ")");
        
        int num4castsTestAndFuture = inputTest.size() + params.getNumForecasts();
        
        if (params.getFrequency() <= 24) { //forecast::HW can do up to 24
            //TODO chg - if the freq is too high, redirect to tbats and just call a different model
            rengine.eval(FORECAST_MODEL + " <- forecast::hw(" + INPUT_TRAIN + ", h=" + num4castsTestAndFuture + 
                ", alpha=" + params.getAlpha() + ", beta=" + params.getBeta() + ", gamma=" + params.getGamma()
                + ", seasonal=\"" + params.getSeasonalityAddMult() + "\""
                + ", damped=" + params.getDamped() + ")");
            rengine.eval(FIT + " <- fitted(" + FORECAST_MODEL + ")[1:" + inputTrain.size() + "]");
            rengine.eval(FORECAST + " <- data.frame(" + FORECAST_MODEL + ")[\"Point.Forecast\"][1:" + num4castsTestAndFuture + ",]"); //if somebody renames this field in the next version, well...
            double finalAlpha = rengine.evalAndReturnArray(FORECAST_MODEL + "$model$par[\"alpha\"]")[0];
            double finalBeta = rengine.evalAndReturnArray(FORECAST_MODEL + "$model$par[\"beta\"]")[0];
            double finalGamma = rengine.evalAndReturnArray(FORECAST_MODEL + "$model$par[\"gamma\"]")[0];
        } else {
            rengine.eval(FORECAST_MODEL + " <- forecast::tbats(" + INPUT_TRAIN + ")");
            rengine.eval(FIT + " <- fitted.values(" + FORECAST_MODEL + ")");
            rengine.eval(FORECAST + " <- forecast(" + FORECAST_MODEL + ", h=" + num4castsTestAndFuture + ")$mean");
        }
        
        //if this is not seasonal data, R throws an error
        if (rengine.eval(FORECAST_MODEL) == null) {
            TrainAndTestReportCrisp report = new TrainAndTestReportCrisp(Model.HOLT_WINTERS);
            report.setModelDescription("(unable to fit the requested model)");
            report.setRealOutputsTrain(Utils.listToArray(inputTrain));
            report.setFittedValues(Utils.listToArray(inputTrain));
            report.setRealOutputsTest(Utils.listToArray(inputTest));
            report.setForecastValuesTest(Utils.listToArray(inputTest));
            report.setNumTrainingEntries(inputTrain.size());
            report.setPlotCode("plot.ts(rep(0," + dataToUse.size() + "))"); //dummy
            report.setErrorMeasures(new ErrorMeasuresCrisp());
        
            return report;
        }

        double[] fittedVals = rengine.evalAndReturnArray(FIT);

        List<Double> forecastTestAndFuture = rengine.evalAndReturnList(FORECAST);
        List<Double> forecastTest = forecastTestAndFuture.subList(0, inputTest.size());
        List<Double> forecastFuture = forecastTestAndFuture.subList(inputTest.size(), forecastTestAndFuture.size());
        
        TrainAndTestReportCrisp report = new TrainAndTestReportCrisp(Model.HOLT_WINTERS);
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
        
        rengine.rm(INPUT_TRAIN, FORECAST_MODEL, FIT, FORECAST);
        
        return report;
    }
}
