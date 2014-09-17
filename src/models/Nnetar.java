package models;

import java.util.List;
import params.NnetarParams;
import params.Params;
import rcaller.RCaller;
import rcaller.RCode;
import utils.Const;
import utils.RCodeSession;
import utils.Utils;

public class Nnetar implements Forecastable {
    
    @Override
    public TrainAndTestReport forecast(List<Double> allData, Params parameters){
        final String TRAINDATA = Const.TRAINDATA + RCodeSession.INSTANCE.getCounter();
        final String NNETWORK = Const.NNETWORK + RCodeSession.INSTANCE.getCounter();
        final String FORECASTMODEL = Const.FORECAST_MODEL + RCodeSession.INSTANCE.getCounter();
        final String FORECASTVALS = Const.FORECAST_VALS + RCodeSession.INSTANCE.getCounter();
        final String TESTDATA = Const.TEST + RCodeSession.INSTANCE.getCounter();
        final String ACCURACY = Const.ACC + RCodeSession.INSTANCE.getCounter();
        
        NnetarParams params = (NnetarParams) parameters;
        TrainAndTestReport report = new TrainAndTestReport("nnetar");

        RCaller caller = Utils.getCleanRCaller();
        caller.deleteTempFiles();

        RCode code = RCodeSession.INSTANCE.getRCode();
        
        code.R_require("forecast");
        int numTrainingEntries = Math.round(((float) params.getPercentTrain()/100)*allData.size());
        List<Double> trainingPortionOfData = allData.subList(0, numTrainingEntries);
        report.setTrainData(trainingPortionOfData);
        List<Double> testingPortionOfData = allData.subList(numTrainingEntries, allData.size());
        report.setTestData(testingPortionOfData);

        code.addDoubleArray(TRAINDATA, Utils.listToArray(trainingPortionOfData));
        String optionalParams = getOptionalParams(params);
        code.addRCode(NNETWORK + " <- nnetar(" + TRAINDATA + optionalParams + ")");

        int numForecasts = testingPortionOfData.size();
        numForecasts += params.getNumForecasts();
        code.addRCode(FORECASTMODEL + " <- forecast(" + NNETWORK + ", " + numForecasts + ")");
        //skoro ma svihlo, kym som na to prisla, ale:
        //1. vo "forecastedModel" je strasne vela heterogennych informacii, neda sa to len tak poslat cele Jave
        //2. takze ked chcem len tie forecastedValues, ziskam ich ako "forecastedModel$mean[1:8]", kde 8 je ich pocet...
        code.addRCode(FORECASTVALS + " <- " + FORECASTMODEL + "$mean[1:" + testingPortionOfData.size() + "]");

        caller.setRCode(code);
        caller.runAndReturnResult(FORECASTVALS);
        double[] forecasted = caller.getParser().getAsDoubleArray(FORECASTVALS);
        report.setForecastData(Utils.arrayToList(forecasted));
            
        caller = Utils.getCleanRCaller();
        code.addDoubleArray(TESTDATA, Utils.listToArray(testingPortionOfData));
        code.addRCode(ACCURACY + " <- accuracy(" + FORECASTMODEL + ", " + TESTDATA + ")[1:12]"); //TODO [1:12] preto, ze v novej verzii
        // tam pribudla aj ACF a niekedy robi problemy

        caller.setRCode(code);
        caller.runAndReturnResult(ACCURACY);

        double[] acc = caller.getParser().getAsDoubleArray(ACCURACY); //pozor na poradie vysledkov, ochenta setenta...
        //vrati vysledky po stlpcoch, tj. ME train, ME test, RMSE train, RMSE test, MAE, MPE, MAPE, MASE
        //nova verzia vracia aj ACF1

        //dalo by sa aj maticu, ale momentalne mi staci ten list:
        //double[][] acc2 = caller.getParser().getAsDoubleMatrix(ACC, 6, 2);

        report.setErrorMeasures(Utils.arrayToList(acc));
        report.setForecastPlotCode("plot(" + FORECASTMODEL + ", type=\"l\")");
        
        RCodeSession.INSTANCE.setRCode(code);
        
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
