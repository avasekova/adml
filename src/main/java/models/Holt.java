package models;

import models.params.HoltParams;
import models.params.Params;
import utils.*;

import java.util.List;
import java.util.Map;

public class Holt implements Forecastable {
    private static final long serialVersionUID = 1L;

    @Override
    public TrainAndTestReport forecast(Map<String, List<Double>> dataTableModel, Params parameters) {
        final String FORECAST_MODEL = Const.FORECAST_MODEL + Utils.getCounter();
        final String INPUT_TRAIN = Const.INPUT + Utils.getCounter();
        final String FIT = Const.FIT + Utils.getCounter();
        final String FORECAST = Const.FORECAST_VALS + Utils.getCounter();
        final String PRED_INT_LOWER = Const.OUTPUT + Utils.getCounter();
        final String PRED_INT_UPPER = Const.OUTPUT + Utils.getCounter();
        
        HoltParams params = (HoltParams) parameters;
        
        List<Double> allData = dataTableModel.get(params.getColName());
        List<Double> dataToUse = allData.subList((params.getDataRangeFrom() - 1), params.getDataRangeTo());
        
        MyRengine rengine = MyRengine.getRengine();
        rengine.require("forecast");
        int numTrainingEntries = Math.round(((float) params.getPercentTrain()/100)*dataToUse.size());
        
        List<Double> inputTrain = dataToUse.subList(0, numTrainingEntries);
        List<Double> inputTest = dataToUse.subList(numTrainingEntries, dataToUse.size());
        
        rengine.assign(INPUT_TRAIN, Utils.listToArray(inputTrain));
        
        int num4castsTestAndFuture /*= 1; */ = inputTest.size() + params.getNumForecasts();
        if (params.getPredIntPercent() == 0) {
            rengine.eval(FORECAST_MODEL + " <- forecast::holt(" + INPUT_TRAIN + ", h=" + num4castsTestAndFuture + 
                ", alpha=" + params.getAlpha() + ", beta=" + params.getBeta() + ", damped=" + params.getDamped() + ")");
        } else {
            rengine.eval(FORECAST_MODEL + " <- forecast::holt(" + INPUT_TRAIN + ", h=" + num4castsTestAndFuture + 
                ", alpha=" + params.getAlpha() + ", beta=" + params.getBeta() + ", damped=" + params.getDamped()
                    + ", level=" + params.getPredIntPercent() + ")");
        }
        
        
        rengine.eval(FIT + " <- fitted(" + FORECAST_MODEL + ")[1:" + inputTrain.size() + "]");
        double[] fittedVals = rengine.evalAndReturnArray(FIT);
        
        rengine.eval(FORECAST + " <- data.frame(" + FORECAST_MODEL + ")[\"Point.Forecast\"][1:" + num4castsTestAndFuture + ",]"); //if somebody renames this field in the next version, well...
        List<Double> forecastTestAndFuture = rengine.evalAndReturnList(FORECAST);
        List<Double> forecastTest = forecastTestAndFuture.subList(0, inputTest.size());
        List<Double> forecastFuture = forecastTestAndFuture.subList(inputTest.size(), forecastTestAndFuture.size());
        
        double finalAlpha = rengine.evalAndReturnArray(FORECAST_MODEL + "$model$par[\"alpha\"]")[0];
        double finalBeta = rengine.evalAndReturnArray(FORECAST_MODEL + "$model$par[\"beta\"]")[0];
        
        //tak a teraz postupne napredikujeme ten zbytok
        
        
        
        TrainAndTestReportCrisp report = new TrainAndTestReportCrisp(Model.HOLT);
        report.setModelDescription(params.toString());
        
        report.setNumTrainingEntries(numTrainingEntries);
        report.setFittedValues(fittedVals);
        report.setForecastValuesTest(Utils.listToArray(forecastTest));
        report.setForecastValuesFuture(Utils.listToArray(forecastFuture));
        report.setPlotCode("plot.ts(c(" + Utils.arrayToRVectorString(fittedVals) + "," + Utils.listToRVectorString(forecastTestAndFuture) + "))");
        report.setRealOutputsTrain(Utils.listToArray(inputTrain));
        report.setRealOutputsTest(Utils.listToArray(inputTest));
        
        //prediction intervals:
        if ((params.getPredIntPercent() != 0)) {
            rengine.eval(PRED_INT_LOWER + " <- data.frame(" + FORECAST_MODEL + ")[\"Lo." + params.getPredIntPercent() + "\"]"
                    + "[1:" + num4castsTestAndFuture + ",]");
            rengine.eval(PRED_INT_UPPER + " <- data.frame(" + FORECAST_MODEL + ")[\"Hi." + params.getPredIntPercent() + "\"]"
                    + "[1:" + num4castsTestAndFuture + ",]");
            double[] predIntLowers = rengine.evalAndReturnArray(PRED_INT_LOWER);
            double[] predIntUppers = rengine.evalAndReturnArray(PRED_INT_UPPER);
            report.setPredictionIntervalsLowers(predIntLowers);
            report.setPredictionIntervalsUppers(predIntUppers);
        }
        
        ErrorMeasuresCrisp errorMeasures = ErrorMeasuresUtils.computeAllErrorMeasuresCrisp(inputTrain, inputTest, 
                Utils.arrayToList(fittedVals), forecastTest, params.getSeasonality());
        report.setErrorMeasures(errorMeasures);
        
        rengine.rm(FORECAST_MODEL, INPUT_TRAIN, PRED_INT_LOWER, PRED_INT_UPPER, FIT, FORECAST);
        
        return report;
    }
}
