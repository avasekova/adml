package models;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import params.NnetarParams;
import params.Params;
import rcaller.RCaller;
import rcaller.RCode;
import utils.Const;
import utils.Utils;

public class Nnetar implements Forecastable {
    
    @Override
    public TrainAndTestReport forecast(List<Double> allData, Params parameters){
        NnetarParams params = (NnetarParams) parameters;
        TrainAndTestReport report = new TrainAndTestReport("nnetar");

        RCaller caller = Utils.getCleanRCaller();
        caller.deleteTempFiles();

        RCode code = new RCode();
        code.clear();

        code.R_require("forecast");
        int numTrainingEntries = Math.round(((float) params.getPercentTrain()/100)*allData.size());
        List<Double> trainingPortionOfData = allData.subList(0, numTrainingEntries);
        report.setTrainData(trainingPortionOfData);
        List<Double> testingPortionOfData = allData.subList(numTrainingEntries, allData.size());
        report.setTestData(testingPortionOfData);

        code.addDoubleArray(Const.TRAINDATA, Utils.listToArray(trainingPortionOfData));
        String optionalParams = getOptionalParams(params);
        code.addRCode(Const.NNETWORK + " <- nnetar(" + Const.TRAINDATA + optionalParams + ")");

        int numForecasts = testingPortionOfData.size();
        numForecasts += params.getNumForecasts();
        code.addRCode(Const.FORECAST_MODEL + " <- forecast(" + Const.NNETWORK + ", " + numForecasts + ")");
        //skoro ma svihlo, kym som na to prisla, ale:
        //1. vo "forecastedModel" je strasne vela heterogennych informacii, neda sa to len tak poslat cele Jave
        //2. takze ked chcem len tie forecastedValues, ziskam ich ako "forecastedModel$mean[1:8]", kde 8 je ich pocet...
        code.addRCode(Const.FORECAST_VALS + " <- " + Const.FORECAST_MODEL + "$mean[1:" + testingPortionOfData.size() + "]");

        caller.setRCode(code);
        caller.runAndReturnResult(Const.FORECAST_VALS);
        double[] forecasted = caller.getParser().getAsDoubleArray(Const.FORECAST_VALS);
        report.setForecastData(Utils.arrayToList(forecasted));
            
        caller = Utils.getCleanRCaller();
        code.addDoubleArray(Const.TEST, Utils.listToArray(testingPortionOfData));
        code.addRCode(Const.ACC + " <- accuracy(" + Const.FORECAST_MODEL + ", " + Const.TEST + ")");

        caller.setRCode(code);
        caller.runAndReturnResult(Const.ACC);

        double[] acc = caller.getParser().getAsDoubleArray(Const.ACC); //pozor na poradie vysledkov, ochenta setenta...
        //vrati vysledky po stlpcoch, tj. ME train, ME test, RMSE train, RMSE test, MAE, MPE, MAPE, MASE

        //dalo by sa aj maticu, ale momentalne mi staci ten list:
        //double[][] acc2 = caller.getParser().getAsDoubleMatrix(ACC, 6, 2);

        report.setErrorMeasures(Utils.arrayToList(acc));
        //report.setForecastPlotCode("plot(" + Const.FORECAST_MODEL + ")"); //TODO ved on odinakial nevie, co to je za premennu!
        report.setForecastPlotCode("plot(seq(1,120))");
        
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
