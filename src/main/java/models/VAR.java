package models;

import models.params.Params;
import models.params.VARParams;
import utils.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//(not used, may contain dragons)
public class VAR { //TODO implements Forecastable, alebo ForecastableMultipleReports

    public List<TrainAndTestReportCrisp> forecast(Params parameters) {
        //alllllData is not used, is empty!
        final String FORECAST_MODEL = Const.FORECAST_MODEL + Utils.getCounter();
        final String INPUT = Const.INPUT + Utils.getCounter();
        final String FORECAST = Const.FORECAST_VALS + Utils.getCounter();
        final String FIT = Const.FIT + Utils.getCounter();
        final String REAL_OUTPUT = Const.OUTPUT + Utils.getCounter();
        
        VARParams params = (VARParams) parameters;
        List<TrainAndTestReportCrisp> allReports = new ArrayList<>();
        
        Map<String, List<Double>> dataToUse = trimToRange((params.getDataRangeFrom() - 1), params.getDataRangeTo(), params.getData());
        
        MyRengine rengine = MyRengine.getRengine();
        rengine.require("vars");
        int numTrainingEntries = Math.round(((float) params.getPercentTrain()/100)*dataToUse.get(dataToUse.keySet().toArray(new String[]{})[0]).size());
        
        //nenastavim realOutputsTest (zatial pouzivam 100% na train) - takze to moze niekde vyhadzovat NPE
        List<Double> trimmedOutVals = params.getOutputVarVals().subList((params.getDataRangeFrom() - 1), params.getDataRangeTo());
        rengine.assign(REAL_OUTPUT, Utils.listToArray(trimmedOutVals));
        rengine.eval(REAL_OUTPUT + " <- c(rep(NA," + params.getLag() + ")," + REAL_OUTPUT + ")");
        double[] realOutput = rengine.evalAndReturnArray(REAL_OUTPUT);
        
        
        rengine.assignMatrix(INPUT, dataToUse);
        rengine.eval(FORECAST_MODEL + " <- vars::VAR(" + INPUT + ", p=" + params.getLag() + ", type=\"" + params.getType() + "\")");
        
        rengine.eval(FIT + " <- fitted(" + FORECAST_MODEL + ")"); //to je fitted pre vsetky, mna zaujima len pre tu jednu
        
        
        for (String s : params.getEndogenousVars()) {
            final String FIT_THIS = FIT + Utils.getCounter();
            
            TrainAndTestReportCrisp report = new TrainAndTestReportCrisp(Model.VAR);
            report.setModelDescription(params.toString());
            report.setNumTrainingEntries(numTrainingEntries);
            report.setRealOutputsTrain(realOutput);
            
            rengine.eval(FIT_THIS + " <- " + FIT + "[,\"" + INPUT + "." + s + "\"]"); //len pre jednu
            rengine.eval(FIT_THIS + " <- c(rep(NA," + params.getLag() + "), as.vector(" + FIT_THIS + "))"); //lagnute
            double[] fittedOutput = rengine.evalAndReturnArray(FIT_THIS);
            report.setFittedValues(fittedOutput);


            report.setForecastValuesTest(new double[]{});


            int numFutureForecasts = params.getNumForecasts();
            if (numFutureForecasts > 0) {
                rengine.eval(FORECAST + " <- predict(" + FORECAST_MODEL + ", n.ahead=" + numFutureForecasts + ")");
                rengine.eval(FORECAST + " <- " + FORECAST + "$fcst$" + INPUT + "." + s + "[,1]");
                double[] forecastOutput = rengine.evalAndReturnArray(FORECAST);
                report.setForecastValuesFuture(forecastOutput);
                report.setPlotCode("plot.ts(c(" + Utils.arrayToRVectorString(fittedOutput) + "," + Utils.arrayToRVectorString(forecastOutput) + "))");
            } else {
                report.setPlotCode("plot.ts(c(" + Utils.arrayToRVectorString(fittedOutput) + "))");
            }


            ErrorMeasuresCrisp errorMeasures = ErrorMeasuresUtils.computeAllErrorMeasuresCrisp(
                    Utils.arrayToList(realOutput), new ArrayList<Double>(),
                    Utils.arrayToList(fittedOutput), new ArrayList<Double>(), params.getSeasonality());
            report.setErrorMeasures(errorMeasures);


            allReports.add(report);

            rengine.rm(FIT_THIS);
        }
        
        rengine.rm(FORECAST_MODEL, INPUT, FIT, REAL_OUTPUT);
        
        
        return allReports;
    }

    private Map<String, List<Double>> trimToRange(int from, int to, Map<String, List<Double>> data) {
        Map<String, List<Double>> trimmed = new HashMap<>();
        
        for (String s : data.keySet()) {
            trimmed.put(s, data.get(s).subList(from, to));
        }
        
        return trimmed;
    }
    
}
