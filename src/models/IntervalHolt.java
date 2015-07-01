package models;

import gui.tablemodels.DataTableModel;
import java.util.List;
import org.rosuda.JRI.REXP;
import models.params.IntervalHoltParams;
import models.params.Params;
import utils.Const;
import utils.ErrorMeasuresInterval;
import utils.ErrorMeasuresUtils;
import utils.MyRengine;
import utils.Utils;
import utils.imlp.Interval;

public class IntervalHolt implements Forecastable {
    //A.holt(dejta, h=10, alpha=alpha, beta=beta, gamma=FALSE)

    @Override
    public TrainAndTestReport forecast(DataTableModel dataTableModel, Params parameters) {
        final String FORECAST_MODEL = Const.FORECAST_MODEL + Utils.getCounter();
        final String INPUT_TRAIN = Const.INPUT + Utils.getCounter();
        final String INPUT_TRAIN_CENTER = INPUT_TRAIN + ".center";
        final String INPUT_TRAIN_RADIUS = INPUT_TRAIN + ".radius";
        final String FIT_LOWER = Const.FIT + Utils.getCounter();
        final String FIT_UPPER = Const.FIT + Utils.getCounter();
        final String FORECAST = Const.FORECAST_VALS + Utils.getCounter();
        
        IntervalHoltParams params = (IntervalHoltParams) parameters;
        
        List<Double> allDataCenter = dataTableModel.getDataForColname(params.getColNameCenter());
        List<Double> allDataRadius = dataTableModel.getDataForColname(params.getColNameRadius());
        List<Double> dataToUseCenter = allDataCenter.subList((params.getDataRangeFrom() - 1), params.getDataRangeTo());
        List<Double> dataToUseRadius = allDataRadius.subList((params.getDataRangeFrom() - 1), params.getDataRangeTo());
        
        int numTrainingEntries = Math.round(((float) params.getPercentTrain()/100)*dataToUseCenter.size());
        
        List<Double> inputTrainCenter = dataToUseCenter.subList(0, numTrainingEntries);
        List<Double> inputTrainRadius = dataToUseRadius.subList(0, numTrainingEntries);
        List<Double> inputTestCenter = dataToUseCenter.subList(numTrainingEntries, dataToUseCenter.size());
        List<Double> inputTestRadius = dataToUseRadius.subList(numTrainingEntries, dataToUseCenter.size());
        
        MyRengine rengine = MyRengine.getRengine();
        rengine.require("forecast");
        
        rengine.assign(INPUT_TRAIN_CENTER, Utils.listToArray(inputTrainCenter));
        rengine.assign(INPUT_TRAIN_RADIUS, Utils.listToArray(inputTrainRadius));
        
        //potrebujem maticu LB a UB, a mam Center a Radius
        rengine.eval(INPUT_TRAIN + " <- cbind((" + INPUT_TRAIN_CENTER + "-" + INPUT_TRAIN_RADIUS + ")," + 
                                             "(" + INPUT_TRAIN_CENTER + "+" + INPUT_TRAIN_RADIUS + "))");
        
        int num4castsTestAndTrain = inputTestCenter.size() + params.getNumForecasts();
        rengine.eval(FORECAST_MODEL + " <- A.holt(" + INPUT_TRAIN + ", h=" + num4castsTestAndTrain + ", alpha=" + 
                params.getAlpha() + ", beta=" + params.getBeta() + ", gamma=FALSE)");
        
        rengine.eval(FIT_LOWER + " <- fitted(" + FORECAST_MODEL + ")[,1][1:" + numTrainingEntries + "]");
        rengine.eval(FIT_UPPER + " <- fitted(" + FORECAST_MODEL + ")[,2][1:" + numTrainingEntries + "]");
        REXP getFitLower = rengine.eval(FIT_LOWER);
        List<Double> fitLower = Utils.arrayToList(getFitLower.asDoubleArray());
        REXP getFitUpper = rengine.eval(FIT_UPPER);
        List<Double> fitUpper = Utils.arrayToList(getFitUpper.asDoubleArray());
        
        List<Interval> fittedIntervals = Utils.zipLowerUpperToIntervals(fitLower, fitUpper);
        
        //zatial neviem forecastovat; produkuje to len bodove forecasty a navyse neviem, odkial ich berie.
        //TODO doplnit forecasty, zatial pouzijem toto ako LB=UB=tentoZlyForecast
        rengine.eval(FORECAST + " <- data.frame(" + FORECAST_MODEL + ")[\"Point.Forecast\"]"
                + "[1:" + num4castsTestAndTrain + ",]");
        REXP getForecastsAll = rengine.eval(FORECAST);
        List<Double> forecastsAll = Utils.arrayToList(getForecastsAll.asDoubleArray());
        
        List<Interval> forecastIntervalsAll = Utils.zipLowerUpperToIntervals(forecastsAll, forecastsAll); //TODO!
        List<Interval> forecastsTest = forecastIntervalsAll.subList(0, num4castsTestAndTrain);
        List<Interval> forecastsFuture = forecastIntervalsAll.subList(num4castsTestAndTrain, forecastIntervalsAll.size());
        
        TrainAndTestReportInterval report = new TrainAndTestReportInterval(Const.INTERVAL_HOLT);
        report.setModelDescription(params.toString());
        
        report.setFittedValues(fittedIntervals);
        report.setForecastValuesTest(forecastsTest);
        report.setForecastValuesFuture(forecastsFuture);
        report.setNumTrainingEntries(numTrainingEntries);
        report.setRealValues(dataToUseCenter, dataToUseRadius);
        
        ErrorMeasuresInterval errorMeasures = ErrorMeasuresUtils.computeAllErrorMeasuresInterval(
                Utils.zipCentersRadiiToIntervals(inputTrainCenter, inputTrainRadius), 
                Utils.zipCentersRadiiToIntervals(inputTestCenter, inputTestRadius), 
                fittedIntervals, forecastsTest, params.getDistance(), params.getSeasonality());
        report.setErrorMeasures(errorMeasures);
        
        rengine.rm(FORECAST, FORECAST_MODEL, INPUT_TRAIN, INPUT_TRAIN_CENTER, INPUT_TRAIN_RADIUS, FIT_LOWER, FIT_UPPER);
        
        return report;
    }

    @Override
    public String getOptionalParams(Params parameters) {
        return "";
    }
}
