package models;

import gui.DataTableModel;
import java.util.ArrayList;
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
import utils.imlp.dist.WeightedEuclideanDistance;

public class VARint implements Forecastable {

    @Override
    public TrainAndTestReport forecast(DataTableModel dataTableModel, Params parameters) {
        final String FORECAST_MODEL = Const.FORECAST_MODEL + Utils.getCounter();
        final String INPUT = Const.INPUT + Utils.getCounter();
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
        
        TrainAndTestReportInterval report = new TrainAndTestReportInterval("VAR(i)");
        report.setModelDescription("(TODO)");
        
        VARintParams params = (VARintParams) parameters;
        
        List<Double> inputsCenter = dataTableModel.getDataForColname(params.getCenter()).subList(params.getDataRangeFrom() - 1, params.getDataRangeTo());
        List<Double> inputsRadius = dataTableModel.getDataForColname(params.getRadius()).subList(params.getDataRangeFrom() - 1, params.getDataRangeTo());
        
        Rengine rengine = MyRengine.getRengine();
        rengine.eval("require(vars)");
        int numTrainingEntries = Math.round(((float) params.getPercentTrain()/100)*inputsCenter.size());
        report.setNumTrainingEntries(numTrainingEntries);
        
        rengine.assign(INPUTCENTER, Utils.listToArray(inputsCenter));
        rengine.assign(INPUTRADIUS, Utils.listToArray(inputsRadius));
        rengine.eval(INPUT + " <- cbind(" + INPUTCENTER + ", " + INPUTRADIUS + ")");
        rengine.eval("colnames(" + INPUT + ") <- c(\"center\", \"radius\")");
        
        
        rengine.eval(REAL_OUTPUT_CENTER + " <- " + INPUTCENTER); // REAL_OUTPUT_CENTER + " <- " + INPUTCENTER + "[" + (params.getLag() + 1) + ":length(" + INPUTCENTER + ")]");
        //v Radius nechavam predok zalagovany, aby sa v zipcenters odrezal a nahradil u oboch Intervalom(NaN, NaN)
        rengine.eval(REAL_OUTPUT_RADIUS + " <- " + INPUTRADIUS);//c(rep(NA," + params.getLag() + ")," + INPUTRADIUS + "[" + (params.getLag() + 1) + ":length(" + INPUTRADIUS + ")])");
        REXP getRealOutputCenter = rengine.eval(REAL_OUTPUT_CENTER);
        REXP getRealOutputRadius = rengine.eval(REAL_OUTPUT_RADIUS);
        List<Double> realOutputCenter = Utils.arrayToList(getRealOutputCenter.asDoubleArray());
        List<Double> realOutputRadius = Utils.arrayToList(getRealOutputRadius.asDoubleArray());
        List<Interval> realOutputs = Utils.zipCentersRadiiToIntervals(realOutputCenter, realOutputRadius);
        report.setRealValues(realOutputs);
        
        //nepridavat NA nikam, prida sa pri plotovani!
        //TODO zostavit si prirucku, kde co treba spravit... co sa robi v plotDraweri a co v jednotlivych Forecastable
        rengine.eval(FORECAST_MODEL + " <- vars::VAR(" + INPUT + ", p=" + params.getLag() + ", type=\"" + params.getType() + "\")");
        rengine.eval(FIT + " <- fitted(" + FORECAST_MODEL + ")");
        rengine.eval(FIT_CENTER + " <- c(rep(NA," + params.getLag() + "), as.vector(" + FIT + "[,\"center\"]" + "))");
        rengine.eval(FIT_RADIUS + " <- c(rep(NA," + params.getLag() + "), as.vector(" + FIT + "[,\"radius\"]" + "))");
        REXP getFitCenter = rengine.eval(FIT_CENTER);
        REXP getFitRadius = rengine.eval(FIT_RADIUS);
        List<Double> fitCenter = Utils.arrayToList(getFitCenter.asDoubleArray());
        List<Double> fitRadius = Utils.arrayToList(getFitRadius.asDoubleArray());
        List<Interval> fitted = Utils.zipCentersRadiiToIntervals(fitCenter, fitRadius);
        report.setFittedValues(fitted);
        
        report.setForecastValuesTest(new ArrayList<Interval>());
        
        int numFutureForecasts = params.getNumForecasts();
        if (numFutureForecasts > 0) {
            rengine.eval(FORECAST + " <- predict(" + FORECAST_MODEL + ", n.ahead=" + numFutureForecasts + ")");
            rengine.eval(FORECAST_CENTER + " <- " + FORECAST + "$fcst$center[,1]");
            rengine.eval(FORECAST_RADIUS + " <- " + FORECAST + "$fcst$radius[,1]");
            REXP getForecastCenter = rengine.eval(FORECAST_CENTER);
            REXP getForecastRadius = rengine.eval(FORECAST_RADIUS);
            List<Double> forecastCenter = Utils.arrayToList(getForecastCenter.asDoubleArray());
            List<Double> forecastRadius = Utils.arrayToList(getForecastRadius.asDoubleArray());
            List<Interval> forecastFuture = Utils.zipCentersRadiiToIntervals(forecastCenter, forecastRadius);
            report.setForecastValuesFuture(forecastFuture);
        }
        
        ErrorMeasuresInterval errorMeasures = ErrorMeasuresUtils.computeAllErrorMeasuresInterval(
                realOutputs, new ArrayList<Interval>(),
                fitted, new ArrayList<Interval>(), new WeightedEuclideanDistance(0.5)); //TODO distance
        report.setErrorMeasures(errorMeasures);
        
        
        return report;
    }

    @Override
    public String getOptionalParams(Params parameters) {
        return "";
    }
}
