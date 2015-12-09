package models;

import models.params.Params;
import models.params.VARintParams;
import utils.*;
import utils.imlp.Interval;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VARint implements Forecastable {
    private static final long serialVersionUID = 1L;

    @Override
    public TrainAndTestReport forecast(Map<String, List<Double>> dataTableModel, Params parameters) {
        final String FORECAST_MODEL = Const.FORECAST_MODEL + Utils.getCounter();
        final String INPUT = Const.INPUT + Utils.getCounter(); //should not be used anywhere
        final String INPUT_TRAIN = INPUT + ".train";
        final String INPUTCENTER = INPUT + ".center";
        final String INPUTRADIUS = INPUT + ".radius";
        final String FORECAST = Const.FORECAST_VALS + Utils.getCounter();
        final String FORECAST_CENTER = FORECAST + ".center";
        final String FORECAST_RADIUS = FORECAST + ".radius";
        final String FIT = Const.FIT + Utils.getCounter();
        final String FIT_CENTER = FIT + ".center";
        final String FIT_RADIUS = FIT + ".radius";
        final String REAL_OUTPUT = Const.OUTPUT + Utils.getCounter();
        final String REAL_OUTPUT_CENTER = REAL_OUTPUT + ".center";
        final String REAL_OUTPUT_RADIUS = REAL_OUTPUT + ".radius";
        
        VARintParams params = (VARintParams) parameters;
        
        List<Double> inputsCenter = dataTableModel.get(params.getCenter()).subList(params.getDataRangeFrom() - 1, params.getDataRangeTo());
        List<Double> inputsRadius = dataTableModel.get(params.getRadius()).subList(params.getDataRangeFrom() - 1, params.getDataRangeTo());
        
        MyRengine rengine = MyRengine.getRengine();
        rengine.require("vars");
        int numTrainingEntries = Math.round(((float) params.getPercentTrain()/100)*inputsCenter.size());
        
        rengine.assign(INPUTCENTER, Utils.listToArray(inputsCenter));
        rengine.assign(INPUTRADIUS, Utils.listToArray(inputsRadius));
        rengine.eval(INPUT_TRAIN +  " <- cbind(" + INPUTCENTER + "[1:" + numTrainingEntries + "]" + ", " 
                + INPUTRADIUS + "[1:" + numTrainingEntries + "]" + ")");
        rengine.eval("colnames(" + INPUT_TRAIN + ") <- c(\"center\", \"radius\")");
        
        
        rengine.eval(REAL_OUTPUT_CENTER + " <- " + INPUTCENTER);
        rengine.eval(REAL_OUTPUT_RADIUS + " <- " + INPUTRADIUS);
        List<Double> realOutputCenter = rengine.evalAndReturnList(REAL_OUTPUT_CENTER);
        List<Double> realOutputRadius = rengine.evalAndReturnList(REAL_OUTPUT_RADIUS);
        List<Interval> realOutputs = Utils.zipCentersRadiiToIntervals(realOutputCenter, realOutputRadius);
        List<Interval> realOutputsTrain = realOutputs.subList(0, numTrainingEntries);
        List<Interval> realOutpustTest = realOutputs.subList(numTrainingEntries, realOutputs.size());
        
        //nepridavat NA nikam, prida sa pri plotovani!
        //TODO zostavit si prirucku, kde co treba spravit... co sa robi v plotDraweri a co v jednotlivych Forecastable
        long finalLag;
        if (params.isOptimizeLag()) {
            rengine.eval(FORECAST_MODEL + " <- vars::VAR(" + INPUT_TRAIN + ", lag.max=" + params.getLag() + ", ic=\"" + params.getCriterionOptimizeLag() + "\", type=\"" + params.getType() + "\")");
            finalLag = Math.round(rengine.evalAndReturnArray(FORECAST_MODEL + "$p")[0]);
        } else {
            rengine.eval(FORECAST_MODEL + " <- vars::VAR(" + INPUT_TRAIN + ", p=" + params.getLag() + ", type=\"" + params.getType() + "\")");
            finalLag = params.getLag();
        }
        rengine.eval(FIT + " <- fitted(" + FORECAST_MODEL + ")");
        rengine.eval(FIT_CENTER + " <- c(rep(NA," + finalLag + "), as.vector(" + FIT + "[,\"center\"]" + "))");
        rengine.eval(FIT_RADIUS + " <- c(rep(NA," + finalLag + "), as.vector(" + FIT + "[,\"radius\"]" + "))");
        List<Double> fitCenter = rengine.evalAndReturnList(FIT_CENTER);
        List<Double> fitRadius = rengine.evalAndReturnList(FIT_RADIUS);
        List<Interval> fitted = Utils.zipCentersRadiiToIntervals(fitCenter, fitRadius);
        
        TrainAndTestReportInterval report = new TrainAndTestReportInterval(Model.VAR_INT);
        report.setModelDescription(params.toString());
        
        report.setNumTrainingEntries(numTrainingEntries);
        report.setRealValues(realOutputs);
        report.setFittedValues(fitted);
        
        //
        int num4castsTestAndFuture = realOutpustTest.size() + params.getNumForecasts();
        rengine.eval(FORECAST + " <- predict(" + FORECAST_MODEL + ", n.ahead=" + num4castsTestAndFuture + ")");
        rengine.eval(FORECAST_CENTER + " <- " + FORECAST + "$fcst$center[,1]");
        rengine.eval(FORECAST_RADIUS + " <- " + FORECAST + "$fcst$radius[,1]");
        List<Double> forecastCenter = rengine.evalAndReturnList(FORECAST_CENTER);
        List<Double> forecastRadius = rengine.evalAndReturnList(FORECAST_RADIUS);
        List<Interval> forecasts = Utils.zipCentersRadiiToIntervals(forecastCenter, forecastRadius);
        List<Interval> forecastsTest = new ArrayList<>(forecasts.subList(0, realOutpustTest.size()));
        List<Interval> forecastsFuture = new ArrayList<>(forecasts.subList(realOutpustTest.size(), forecasts.size()));
        report.setForecastValuesTest(forecastsTest);
        report.setForecastValuesFuture(forecastsFuture);
        
        ErrorMeasuresInterval errorMeasures = ErrorMeasuresUtils.computeAllErrorMeasuresInterval(
                realOutputsTrain, realOutpustTest, fitted, forecastsTest, params.getDistance(), 
                params.getSeasonality());
        report.setErrorMeasures(errorMeasures);
        
        rengine.rm(FORECAST_MODEL, INPUT, INPUT_TRAIN, INPUTCENTER, INPUTRADIUS, FORECAST, FORECAST_CENTER, FORECAST_RADIUS, FIT,
                FIT_CENTER, FIT_RADIUS, REAL_OUTPUT, REAL_OUTPUT_CENTER, REAL_OUTPUT_RADIUS);
        
        return report;
    }
}
