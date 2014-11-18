package models;

import gui.DataTableModel;
import java.util.List;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;
import params.Params;
import params.VARintParams;
import utils.Const;
import utils.ErrorMeasuresInterval;
import utils.ErrorMeasuresUtils;
import utils.MyRengine;
import utils.Utils;
import utils.imlp.Interval;

public class VARint implements Forecastable {

    @Override
    public TrainAndTestReport forecast(DataTableModel dataTableModel, Params parameters) {
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
        
        List<Double> inputsCenter = dataTableModel.getDataForColname(params.getCenter()).subList(params.getDataRangeFrom() - 1, params.getDataRangeTo());
        List<Double> inputsRadius = dataTableModel.getDataForColname(params.getRadius()).subList(params.getDataRangeFrom() - 1, params.getDataRangeTo());
        
        Rengine rengine = MyRengine.getRengine();
        rengine.eval("require(vars)");
        int numTrainingEntries = Math.round(((float) params.getPercentTrain()/100)*inputsCenter.size());
        
        rengine.assign(INPUTCENTER, Utils.listToArray(inputsCenter));
        rengine.assign(INPUTRADIUS, Utils.listToArray(inputsRadius));
        rengine.eval(INPUT_TRAIN +  " <- cbind(" + INPUTCENTER + "[1:" + numTrainingEntries + "]" + ", " 
                + INPUTRADIUS + "[1:" + numTrainingEntries + "]" + ")");
        rengine.eval("colnames(" + INPUT_TRAIN + ") <- c(\"center\", \"radius\")");
        
        
        rengine.eval(REAL_OUTPUT_CENTER + " <- " + INPUTCENTER); // REAL_OUTPUT_CENTER + " <- " + INPUTCENTER + "[" + (params.getLag() + 1) + ":length(" + INPUTCENTER + ")]");
        //v Radius nechavam predok zalagovany, aby sa v zipcenters odrezal a nahradil u oboch Intervalom(NaN, NaN)
        rengine.eval(REAL_OUTPUT_RADIUS + " <- " + INPUTRADIUS);//c(rep(NA," + params.getLag() + ")," + INPUTRADIUS + "[" + (params.getLag() + 1) + ":length(" + INPUTRADIUS + ")])");
        REXP getRealOutputCenter = rengine.eval(REAL_OUTPUT_CENTER);
        REXP getRealOutputRadius = rengine.eval(REAL_OUTPUT_RADIUS);
        List<Double> realOutputCenter = Utils.arrayToList(getRealOutputCenter.asDoubleArray());
        List<Double> realOutputRadius = Utils.arrayToList(getRealOutputRadius.asDoubleArray());
        List<Interval> realOutputs = Utils.zipCentersRadiiToIntervals(realOutputCenter, realOutputRadius);
        List<Interval> realOutputsTrain = realOutputs.subList(0, numTrainingEntries);
        List<Interval> realOutpustTest = realOutputs.subList(numTrainingEntries, realOutputs.size());
        
        //nepridavat NA nikam, prida sa pri plotovani!
        //TODO zostavit si prirucku, kde co treba spravit... co sa robi v plotDraweri a co v jednotlivych Forecastable
        long finalLag;
        if (params.isOptimizeLag()) {
            rengine.eval(FORECAST_MODEL + " <- vars::VAR(" + INPUT_TRAIN + ", lag.max=" + params.getLag() + ", ic=\"" + params.getCriterionOptimizeLag() + "\", type=\"" + params.getType() + "\")");
            REXP getFinalLag = rengine.eval(FORECAST_MODEL + "$p");
            finalLag = Math.round(getFinalLag.asDoubleArray()[0]);
        } else {
            rengine.eval(FORECAST_MODEL + " <- vars::VAR(" + INPUT_TRAIN + ", p=" + params.getLag() + ", type=\"" + params.getType() + "\")");
            finalLag = params.getLag();
        }
        rengine.eval(FIT + " <- fitted(" + FORECAST_MODEL + ")");
        rengine.eval(FIT_CENTER + " <- c(rep(NA," + finalLag + "), as.vector(" + FIT + "[,\"center\"]" + "))");
        rengine.eval(FIT_RADIUS + " <- c(rep(NA," + finalLag + "), as.vector(" + FIT + "[,\"radius\"]" + "))");
        REXP getFitCenter = rengine.eval(FIT_CENTER);
        REXP getFitRadius = rengine.eval(FIT_RADIUS);
        List<Double> fitCenter = Utils.arrayToList(getFitCenter.asDoubleArray());
        List<Double> fitRadius = Utils.arrayToList(getFitRadius.asDoubleArray());
        List<Interval> fitted = Utils.zipCentersRadiiToIntervals(fitCenter, fitRadius);
        
        TrainAndTestReportInterval report = new TrainAndTestReportInterval("VAR(i)");
        report.setModelDescription("(lag=" + finalLag + ",dist=" + params.getDistance() + ")");
        
        report.setNumTrainingEntries(numTrainingEntries);
        report.setRealValues(realOutputs);
        report.setFittedValues(fitted);
        
        //
        int num4castsTestAndFuture = realOutpustTest.size() + params.getNumForecasts();
        rengine.eval(FORECAST + " <- predict(" + FORECAST_MODEL + ", n.ahead=" + num4castsTestAndFuture + ")");
        rengine.eval(FORECAST_CENTER + " <- " + FORECAST + "$fcst$center[,1]");
        rengine.eval(FORECAST_RADIUS + " <- " + FORECAST + "$fcst$radius[,1]");
        REXP getForecastCenter = rengine.eval(FORECAST_CENTER);
        REXP getForecastRadius = rengine.eval(FORECAST_RADIUS);
        List<Double> forecastCenter = Utils.arrayToList(getForecastCenter.asDoubleArray());
        List<Double> forecastRadius = Utils.arrayToList(getForecastRadius.asDoubleArray());
        List<Interval> forecasts = Utils.zipCentersRadiiToIntervals(forecastCenter, forecastRadius);
        List<Interval> forecastsTest = forecasts.subList(0, realOutpustTest.size());
        List<Interval> forecastsFuture = forecasts.subList(realOutpustTest.size(), forecasts.size());
        report.setForecastValuesTest(forecastsTest);
        report.setForecastValuesFuture(forecastsFuture);
        
        ErrorMeasuresInterval errorMeasures = ErrorMeasuresUtils.computeAllErrorMeasuresInterval(
                realOutputsTrain, realOutpustTest, fitted, forecastsTest, params.getDistance(), 
                params.getSeasonality());
        report.setErrorMeasures(errorMeasures);
        
        
        return report;
    }

    @Override
    public String getOptionalParams(Params parameters) {
        return "";
    }
}