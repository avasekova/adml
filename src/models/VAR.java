package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;
import params.Params;
import params.VARParams;
import utils.Const;
import utils.ErrorMeasuresCrisp;
import utils.ErrorMeasuresUtils;
import utils.MyRengine;
import utils.Utils;

public class VAR implements Forecastable {

    @Override
    public TrainAndTestReport forecast(List<Double> alllllData, Params parameters) {
        //alllllData is not used, is empty!
        final String FORECAST_MODEL = Const.FORECAST_MODEL + Utils.getCounter();
        final String INPUT = Const.INPUT + Utils.getCounter();
        final String FORECAST = Const.FORECAST_VALS + Utils.getCounter();
        final String FIT = Const.FIT + Utils.getCounter();
        final String REAL_OUTPUT = Const.OUTPUT + Utils.getCounter();
        
        VARParams params = (VARParams) parameters;
        TrainAndTestReportCrisp report = new TrainAndTestReportCrisp("VAR");
        report.setModelDescription("(lag=" + params.getLag() + ",type=" + params.getType() + ")");
        Map<String, List<Double>> dataToUse = trimToRange((params.getDataRangeFrom() - 1), params.getDataRangeTo(), params.getData());
        
        Rengine rengine = MyRengine.getRengine();
        rengine.eval("require(vars)");
        int numTrainingEntries = Math.round(((float) params.getPercentTrain()/100)*dataToUse.get(dataToUse.keySet().toArray(new String[]{})[0]).size());
        report.setNumTrainingEntries(numTrainingEntries);
        
        //nenastavim realOutputsTest (zatial pouzivam 100% na train) - takze to moze niekde vyhadzovat NPE
        List<Double> trimmedOutVals = params.getOutputVarVals().subList((params.getDataRangeFrom() - 1), params.getDataRangeTo());
        rengine.assign(REAL_OUTPUT, Utils.listToArray(trimmedOutVals));
        rengine.eval(REAL_OUTPUT + " <- c(rep(NA," + params.getLag() + ")," + REAL_OUTPUT + ")");
        REXP getRealOutput = rengine.eval(REAL_OUTPUT);
        double[] realOutput = getRealOutput.asDoubleArray();
        report.setRealOutputsTrain(realOutput);
        
        
        ((MyRengine)rengine).assignMatrix(INPUT, dataToUse);
        rengine.eval(FORECAST_MODEL + " <- vars::VAR(" + INPUT + ", p=" + params.getLag() + ", type=\"" + params.getType() + "\")");
        
        rengine.eval(FIT + " <- fitted(" + FORECAST_MODEL + ")"); //to je fitted pre vsetky, mna zaujima len pre tu jednu
        rengine.eval(FIT + " <- " + FIT + "[,\"" + INPUT + "." + params.getOutputVarName() + "\"]"); //len pre jednu
        rengine.eval(FIT + " <- c(rep(NA," + params.getLag() + "), as.vector(" + FIT + "))"); //lagnute
        REXP getFittedOutput = rengine.eval(FIT);
        double[] fittedOutput = getFittedOutput.asDoubleArray();
        report.setFittedValues(fittedOutput);
        
        
        report.setForecastValuesTest(new double[]{});
        
        
        int numFutureForecasts = params.getNumForecasts();
        if (numFutureForecasts > 0) {
            rengine.eval(FORECAST + " <- predict(" + FORECAST_MODEL + ", n.ahead=" + numFutureForecasts + ")");
            rengine.eval(FORECAST + " <- " + FORECAST + "$fcst$" + INPUT + "." + params.getOutputVarName() + "[,1]");
            REXP getForecastOutput = rengine.eval(FORECAST);
            double[] forecastOutput = getForecastOutput.asDoubleArray();
            report.setForecastValuesFuture(forecastOutput);
            report.setPlotCode("plot.ts(c(" + FIT + "," + FORECAST + "))");
        } else {
            report.setPlotCode("plot.ts(c(" + FIT + "))");
        }
        
        
        ErrorMeasuresCrisp errorMeasures = ErrorMeasuresUtils.computeAllErrorMeasuresCrisp(
                Utils.arrayToList(realOutput), new ArrayList<Double>(),
                Utils.arrayToList(fittedOutput), new ArrayList<Double>());
        report.setErrorMeasures(errorMeasures);
        
        
        
        
        
        return report;
    }

    //TODO tuto metodu zrusit... potrebuje ju len asi jeden model. alebo teda ju vyuzit, tj. doplnit vsetky parametre zo
    //  vsetkych balikov
    @Override
    public String getOptionalParams(Params parameters) {
        return "";
    }

    private Map<String, List<Double>> trimToRange(int from, int to, Map<String, List<Double>> data) {
        Map<String, List<Double>> trimmed = new HashMap<>();
        
        for (String s : data.keySet()) {
            trimmed.put(s, data.get(s).subList(from, to));
        }
        
        return trimmed;
    }
    
}
