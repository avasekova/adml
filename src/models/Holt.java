package models;

import gui.tablemodels.DataTableModel;
import java.util.List;
import org.rosuda.JRI.REXP;
import models.params.HoltParams;
import models.params.Params;
import utils.Const;
import utils.ErrorMeasuresCrisp;
import utils.ErrorMeasuresUtils;
import utils.MyRengine;
import utils.Utils;

public class Holt implements Forecastable {

    @Override
    public TrainAndTestReport forecast(DataTableModel dataTableModel, Params parameters) {
        final String FORECAST_MODEL = Const.FORECAST_MODEL + Utils.getCounter();
        final String INPUT_TRAIN = Const.INPUT + Utils.getCounter();
        final String FIT = Const.FIT + Utils.getCounter();
        final String FORECAST = Const.FORECAST_VALS + Utils.getCounter();
        final String PRED_INT_LOWER = Const.OUTPUT + Utils.getCounter();
        final String PRED_INT_UPPER = Const.OUTPUT + Utils.getCounter();
        
        HoltParams params = (HoltParams) parameters;
        
        List<Double> allData = dataTableModel.getDataForColname(params.getColName());
        List<Double> dataToUse = allData.subList((params.getDataRangeFrom() - 1), params.getDataRangeTo());
        
        MyRengine rengine = MyRengine.getRengine();
        rengine.eval("require(forecast)");
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
        REXP getFittedVals = rengine.eval(FIT);
        double[] fittedVals = getFittedVals.asDoubleArray();
        
        rengine.eval(FORECAST + " <- data.frame(" + FORECAST_MODEL + ")[\"Point.Forecast\"][1:" + num4castsTestAndFuture + ",]"); //if somebody renames this field in the next version, well...
        REXP getForecastTestAndFuture = rengine.eval(FORECAST);
        List<Double> forecastTestAndFuture = Utils.arrayToList(getForecastTestAndFuture.asDoubleArray());
        List<Double> forecastTest = forecastTestAndFuture.subList(0, inputTest.size());
        List<Double> forecastFuture = forecastTestAndFuture.subList(inputTest.size(), forecastTestAndFuture.size());
        
        REXP getFinalAlpha = rengine.eval(FORECAST_MODEL + "$model$par[\"alpha\"]");
        double finalAlpha = getFinalAlpha.asDoubleArray()[0];
        REXP getFinalBeta = rengine.eval(FORECAST_MODEL + "$model$par[\"beta\"]");
        double finalBeta = getFinalBeta.asDoubleArray()[0];
        
        //tak a teraz postupne napredikujeme ten zbytok
        
        
        
        TrainAndTestReportCrisp report = new TrainAndTestReportCrisp(Const.HOLT);
        report.setModelDescription(params.toString());
        
        report.setNumTrainingEntries(numTrainingEntries);
        report.setFittedValues(fittedVals);
        report.setForecastValuesTest(Utils.listToArray(forecastTest));
        report.setForecastValuesFuture(Utils.listToArray(forecastFuture));
        report.setPlotCode("plot.ts(c(" + FIT + "," + FORECAST + "))");
        report.setRealOutputsTrain(Utils.listToArray(inputTrain));
        report.setRealOutputsTest(Utils.listToArray(inputTest));
        
        //prediction intervals:
        if ((params.getPredIntPercent() != 0)) {
            rengine.eval(PRED_INT_LOWER + " <- data.frame(" + FORECAST_MODEL + ")[\"Lo." + params.getPredIntPercent() + "\"]"
                    + "[1:" + num4castsTestAndFuture + ",]");
            rengine.eval(PRED_INT_UPPER + " <- data.frame(" + FORECAST_MODEL + ")[\"Hi." + params.getPredIntPercent() + "\"]"
                    + "[1:" + num4castsTestAndFuture + ",]");
            REXP getPredIntLowers = rengine.eval(PRED_INT_LOWER);
            REXP getPredIntUppers = rengine.eval(PRED_INT_UPPER);
            double[] predIntLowers = getPredIntLowers.asDoubleArray();
            double[] predIntUppers = getPredIntUppers.asDoubleArray();
            report.setPredictionIntervalsLowers(predIntLowers);
            report.setPredictionIntervalsUppers(predIntUppers);
        }
        
        ErrorMeasuresCrisp errorMeasures = ErrorMeasuresUtils.computeAllErrorMeasuresCrisp(inputTrain, inputTest, 
                Utils.arrayToList(fittedVals), forecastTest, params.getSeasonality());
        report.setErrorMeasures(errorMeasures);
        
        rengine.rm(FORECAST_MODEL, INPUT_TRAIN, PRED_INT_LOWER, PRED_INT_UPPER); //POZOR, nemazat FIT, FORECAST
        
        return report;
    }

    @Override
    public String getOptionalParams(Params parameters) {
        return "";
    }
}
