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

        final String trainData = Const.TRAINDATA + RCodeSession.INSTANCE.getCounter();
        code.addDoubleArray(trainData, Utils.listToArray(trainingPortionOfData));
        String optionalParams = getOptionalParams(params);
        final String nnetwork = Const.NNETWORK + RCodeSession.INSTANCE.getCounter();
        code.addRCode(nnetwork + " <- nnetar(" + trainData + optionalParams + ")");

        int numForecasts = testingPortionOfData.size();
        numForecasts += params.getNumForecasts();
        final String forecastModel = Const.FORECAST_MODEL + RCodeSession.INSTANCE.getCounter();
        code.addRCode(forecastModel + " <- forecast(" + nnetwork + ", " + numForecasts + ")");
        //skoro ma svihlo, kym som na to prisla, ale:
        //1. vo "forecastedModel" je strasne vela heterogennych informacii, neda sa to len tak poslat cele Jave
        //2. takze ked chcem len tie forecastedValues, ziskam ich ako "forecastedModel$mean[1:8]", kde 8 je ich pocet...
        final String forecastVals = Const.FORECAST_VALS + RCodeSession.INSTANCE.getCounter();
        code.addRCode(forecastVals + " <- " + forecastModel + "$mean[1:" + testingPortionOfData.size() + "]");

        caller.setRCode(code);
        caller.runAndReturnResult(forecastVals);
        double[] forecasted = caller.getParser().getAsDoubleArray(forecastVals);
        report.setForecastData(Utils.arrayToList(forecasted));
            
        caller = Utils.getCleanRCaller();
        final String testData = Const.TEST + RCodeSession.INSTANCE.getCounter();
        code.addDoubleArray(testData, Utils.listToArray(testingPortionOfData));
        final String accuracy = Const.ACC + RCodeSession.INSTANCE.getCounter();
        code.addRCode(accuracy + " <- accuracy(" + forecastModel + ", " + testData + ")");

        caller.setRCode(code);
        caller.runAndReturnResult(accuracy);

        double[] acc = caller.getParser().getAsDoubleArray(accuracy); //pozor na poradie vysledkov, ochenta setenta...
        //vrati vysledky po stlpcoch, tj. ME train, ME test, RMSE train, RMSE test, MAE, MPE, MAPE, MASE

        //dalo by sa aj maticu, ale momentalne mi staci ten list:
        //double[][] acc2 = caller.getParser().getAsDoubleMatrix(ACC, 6, 2);

        report.setErrorMeasures(Utils.arrayToList(acc));
        report.setForecastPlotCode("plot(" + forecastModel + ")"); //TODO ved on odinakial nevie, co to je za premennu!
        
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
