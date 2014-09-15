package models;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import params.NnetParams;
import params.Params;
import rcaller.RCaller;
import rcaller.RCode;
import utils.Const;
import utils.Utils;

public class Nnet implements Forecastable {

    @Override
    public TrainAndTestReport forecast(List<Double> allData, Params parameters) {
        NnetParams params = (NnetParams) parameters;
        TrainAndTestReport report = new TrainAndTestReport();

        RCaller caller = Utils.getCleanRCaller();
        caller.deleteTempFiles();

        RCode code = new RCode();
        code.clear();

        code.R_require("nnet");
        int numTrainingEntries = Math.round(((float) params.getPercentTrain()/100)*allData.size());
        List<Double> trainingPortionOfData = allData.subList(0, numTrainingEntries);
        report.setTrainData(trainingPortionOfData);
        List<Double> trainingPortionOfInputValues = params.getInputs().subList(0, numTrainingEntries);
        
        List<Double> testingPortionOfData = allData.subList(numTrainingEntries, allData.size());
        report.setTestData(testingPortionOfData);
        List<Double> testingPortionOfInputValues = params.getInputs().subList(numTrainingEntries, params.getInputs().size());

        code.addDoubleArray(Const.INPUT, Utils.listToArray(trainingPortionOfInputValues));
        code.addDoubleArray(Const.OUTPUT, Utils.listToArray(trainingPortionOfData));
        String optionalParams = getOptionalParams(params);
        
        code.addRCode(Const.NNETWORK + " <- nnet(" + Const.INPUT + ", " + Const.OUTPUT + optionalParams + ")");
        
        //toto pouzit na spocitanie tych error measures - napredikuje hodnoty, ktore sa to ucilo:
        //code.addRCode(Const.FORECAST_MODEL + " <- predict(" + Const.NNETWORK + ", type='raw')");
        
        code.addDoubleArray(Const.TEST, Utils.listToArray(testingPortionOfInputValues));
        code.addRCode(Const.FORECAST_MODEL + " <- predict(" + Const.NNETWORK + ", " + Const.TEST + ", type='raw')");
        
        
        caller.setRCode(code);
        caller.runAndReturnResult(Const.FORECAST_MODEL);
        double[] forecasted = caller.getParser().getAsDoubleArray(Const.FORECAST_MODEL);
        
//        report.setForecastData(Utils.arrayToList(forecasted));
//            
//        caller = Utils.getCleanRCaller();
//        code.addDoubleArray(Const.TEST, Utils.listToArray(testingPortionOfData));
//        code.addRCode(Const.ACC + " <- accuracy(" + Const.FORECAST_MODEL + ", " + Const.TEST + ")");
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
//            code.addRCode("plot(" + Const.FORECAST_MODEL + ")");
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
        NnetParams params = (NnetParams) parameters;
        StringBuilder optionalParams = new StringBuilder();
        
        //weights
        
        if (params.getNumNodesHiddenLayer() != null) {
            optionalParams.append(", size = ").append(params.getNumNodesHiddenLayer());
        }
        
        //Wts
        
        //mask
        
        if (params.getLinearElseLogistic() != null) {
            optionalParams.append(", linout = ").append(params.getLinearElseLogistic());
        }
        
        
        if (params.getLeastSqrsElseMaxCondLikelihood() != null) {
            optionalParams.append(", entropy = ").append(params.getLeastSqrsElseMaxCondLikelihood());
        }
        
        if (params.getLoglinSoftmaxElseMaxCondLikelihood() != null) {
            optionalParams.append(", softmax = ").append(params.getLoglinSoftmaxElseMaxCondLikelihood());
        }
        
        if (params.getCensoredOnElseOff() != null) {
            optionalParams.append(", censored = ").append(params.getCensoredOnElseOff());
        }
        
        if (params.getSkipLayerConnections() != null) {
            optionalParams.append(", skip = ").append(params.getSkipLayerConnections());
        }
        
        if (params.getInitWeightsRange() != null) {
            optionalParams.append(", rang = ").append(params.getInitWeightsRange());
        }
        
        if (params.getWeightDecay() != null) {
            optionalParams.append(", decay = ").append(params.getWeightDecay());
        }
        
        if (params.getMaxIterations() != null) {
            optionalParams.append(", maxit = ").append(params.getMaxIterations());
        }
     
        //Hessian
        
        if (params.getTraceOptimization() != null) {
            optionalParams.append(", trace = ").append(params.getTraceOptimization());
        }
        
        //MaxNWts
        
        if (params.getAbstol()!= null) {
            optionalParams.append(", abstol = ").append(params.getAbstol());
        }
        
        if (params.getReltol()!= null) {
            optionalParams.append(", reltol = ").append(params.getReltol());
        }
        
        return optionalParams.toString();
    }
    
}
