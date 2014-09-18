package models;

import java.util.List;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;
import params.NnetarParams;
import params.Params;
import utils.Const;
import utils.MyRengine;
import utils.Utils;

public class Nnetar implements Forecastable {
    
    @Override
    public TrainAndTestReport forecast(List<Double> allData, Params parameters){
        NnetarParams params = (NnetarParams) parameters;
        TrainAndTestReport report = new TrainAndTestReport("nnetar");

        Rengine rengine = MyRengine.getRengine();
        rengine.eval("require(forecast)");
        int numTrainingEntries = Math.round(((float) params.getPercentTrain()/100)*allData.size());
        List<Double> trainingPortionOfData = allData.subList(0, numTrainingEntries);
        report.setTrainData(trainingPortionOfData);
        List<Double> testingPortionOfData = allData.subList(numTrainingEntries, allData.size());
        report.setTestData(testingPortionOfData);

        rengine.assign(Const.TRAINDATA, Utils.listToArray(trainingPortionOfData));
        String optionalParams = getOptionalParams(params);
        rengine.eval(Const.NNETWORK + " <- nnetar(" + Const.TRAINDATA + optionalParams + ")");

        int numForecasts = testingPortionOfData.size();
        numForecasts += params.getNumForecasts();
        rengine.eval(Const.FORECAST_MODEL + " <- forecast(" + Const.NNETWORK + ", " + numForecasts + ")");
        //skoro ma svihlo, kym som na to prisla, ale:
        //1. vo "forecastedModel" je strasne vela heterogennych informacii, neda sa to len tak poslat cele Jave
        //2. takze ked chcem len tie forecastedValues, ziskam ich ako "forecastedModel$mean[1:8]", kde 8 je ich pocet...
        REXP getForecastVals = rengine.eval(Const.FORECAST_MODEL + "$mean[1:" + testingPortionOfData.size() + "]");
        double[] forecast = getForecastVals.asDoubleArray();
        
        report.setForecastData(Utils.arrayToList(forecast));
            
        rengine.assign(Const.TEST, Utils.listToArray(testingPortionOfData));
        REXP getAcc = rengine.eval("accuracy(" + Const.FORECAST_MODEL + ", " + Const.TEST + ")[1:12]");//TODO [1:12] preto, ze v novej verzii
        // tam pribudla aj ACF a niekedy robi problemy
        double[] acc = getAcc.asDoubleArray(); //pozor na poradie vysledkov, ochenta setenta...
        //vrati vysledky po stlpcoch, tj. ME train, ME test, RMSE train, RMSE test, MAE, MPE, MAPE, MASE
        //nova verzia vracia aj ACF1
        
        report.setErrorMeasures(Utils.arrayToList(acc));
        report.setForecastPlotCode("plot(" + Const.FORECAST_MODEL + ")");
        
        return report;
    }
    
    @Override
    public String getOptionalParams(Params parameters) {
        NnetarParams params = (NnetarParams) parameters;
        StringBuilder optionalParams = new StringBuilder();
        if (params.getNumNonSeasonalLags() != null) {
            optionalParams.append(", p = ").append(params.getNumNonSeasonalLags());
        }
        
        if (params.getNumSeasonalLags() != null) {
            optionalParams.append(", P = ").append(params.getNumSeasonalLags());
        }
        
        if (params.getNumNodesHidden() != null) {
            optionalParams.append(", size = ").append(params.getNumNodesHidden());
}
        
        if (params.getNumReps() != null) {
            optionalParams.append(", repeats = ").append(params.getNumReps());
        }
        
        if (params.getLambda() != null) {
            optionalParams.append(", lambda = ").append(params.getLambda());
        }
        
        return optionalParams.toString();
    }
}
