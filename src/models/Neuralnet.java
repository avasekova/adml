package models;

import java.util.List;
import params.NeuralnetParams;
import params.Params;

public class Neuralnet implements Forecastable {

    @Override
    public TrainAndTestReport forecast(List<Double> allData, Params parameters) {
        TrainAndTestReport report = new TrainAndTestReport("neuralnet");

//        RCaller caller = Utils.getCleanRCaller();
//        caller.deleteTempFiles();
//
//        RCode code = new RCode();
//        code.clear();
//
//        code.R_require("forecast");
//        int numTrainingEntries = Math.round(((float) params.get("percentTrain")/100)*allData.size());
//        List<Double> trainingPortionOfData = allData.subList(0, numTrainingEntries);
//        report.setTrainData(trainingPortionOfData);
//        List<Double> testingPortionOfData = allData.subList(numTrainingEntries, allData.size());
//        report.setTestData(testingPortionOfData);
//
//        code.addDoubleArray(Const.TRAINDATA, Utils.listToArray(trainingPortionOfData));
//        String optionalParams = getOptionalParams(params);
//        code.addRCode(Const.NNETWORK + " <- nnetar(" + Const.TRAINDATA + optionalParams + ")");
//
//        int numForecasts = testingPortionOfData.size();
//        if (params.get("numForecasts") != null) {
//            numForecasts += params.get("numForecasts");
//        }
//        code.addRCode(Const.FORECASTED_MODEL + " <- forecast(" + Const.NNETWORK + ", " + numForecasts + ")");
//        //skoro ma svihlo, kym som na to prisla, ale:
//        //1. vo "forecastedModel" je strasne vela heterogennych informacii, neda sa to len tak poslat cele Jave
//        //2. takze ked chcem len tie forecastedValues, ziskam ich ako "forecastedModel$mean[1:8]", kde 8 je ich pocet...
//        code.addRCode(Const.FORECASTED_VALS + " <- " + Const.FORECASTED_MODEL + "$mean[1:" + testingPortionOfData.size() + "]");
//
//        caller.setRCode(code);
//        caller.runAndReturnResult(Const.FORECASTED_VALS);
//        double[] forecasted = caller.getParser().getAsDoubleArray(Const.FORECASTED_VALS);
//        report.setForecastData(Utils.arrayToList(forecasted));
//            
//        caller = Utils.getCleanRCaller();
//        code.addDoubleArray(Const.TEST, Utils.listToArray(testingPortionOfData));
//        code.addRCode(Const.ACC + " <- accuracy(" + Const.FORECASTED_MODEL + ", " + Const.TEST + ")");
//
//        caller.setRCode(code);
//        caller.runAndReturnResult(Const.ACC);
//
//        double[] acc = caller.getParser().getAsDoubleArray(Const.ACC); //pozor na poradie vysledkov, ochenta setenta...
//        //vrati vysledky po stlpcoch, tj. ME train, ME test, RMSE train, RMSE test, MAE, MPE, MAPE, MASE
//
//        //dalo by sa aj maticu, ale momentalne mi staci ten list:
//        //double[][] acc2 = caller.getParser().getAsDoubleMatrix(ACC, 6, 2);
//
//        report.setErrorMeasures(Utils.arrayToList(acc));
//        
//        try {
//            caller = Utils.getCleanRCaller();
//            File forecastPlotFile = code.startPlot();
//            code.addRCode("plot(" + Const.FORECASTED_MODEL + ")");
//            code.endPlot();
//            caller.setRCode(code);
//            caller.runOnly();
//            report.setForecastPlot(code.getPlot(forecastPlotFile));
//        } catch (IOException ex) {
//            Logger.getLogger(Nnetar.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
        return report;
    }

    @Override
    public String getOptionalParams(Params parameters) {
        NeuralnetParams params = (NeuralnetParams) parameters;
        StringBuilder optionalParams = new StringBuilder();
//        if (params.getNumNonSeasonalLags() != null) {
//            optionalParams.append(", p = ").append(params.getNumNonSeasonalLags());
//        }
//        
//        if (params.getNumSeasonalLags() != null) {
//            optionalParams.append(", P = ").append(params.getNumSeasonalLags());
//        }
//        
//        if (params.getNumNodesHidden() != null) {
//            optionalParams.append(", size = ").append(params.getNumNodesHidden());
//}
//        
//        if (params.getNumReps() != null) {
//            optionalParams.append(", repeats = ").append(params.getNumReps());
//        }
//        
//        if (params.getLambda() != null) {
//            optionalParams.append(", lambda = ").append(params.getLambda());
//        }
        
        return optionalParams.toString();
    }
    
}
