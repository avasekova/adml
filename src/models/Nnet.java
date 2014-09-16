package models;

import java.util.ArrayList;
import java.util.List;
import params.NnetParams;
import params.Params;
import rcaller.RCaller;
import rcaller.RCode;
import utils.Const;
import utils.RCodeSession;
import utils.Utils;

public class Nnet implements Forecastable {

    @Override
    public TrainAndTestReport forecast(List<Double> allData, Params parameters) {
        NnetParams params = (NnetParams) parameters;
        TrainAndTestReport report = new TrainAndTestReport("nnet");

        RCaller caller = Utils.getCleanRCaller();
        caller.deleteTempFiles();

        RCode code = RCodeSession.INSTANCE.getRCode();
        
        code.R_require("nnet");
        int numTrainingEntries = Math.round(((float) params.getPercentTrain()/100)*allData.size());
        List<Double> trainingPortionOfData = allData.subList(0, numTrainingEntries);
        report.setTrainData(trainingPortionOfData);
        List<Double> trainingPortionOfInputValues = params.getInputs().subList(0, numTrainingEntries);
        
        List<Double> testingPortionOfData = allData.subList(numTrainingEntries, allData.size());
        report.setTestData(testingPortionOfData);
        List<Double> testingPortionOfInputValues = params.getInputs().subList(numTrainingEntries, params.getInputs().size());

        final String inputData = Const.INPUT + RCodeSession.INSTANCE.getCounter();
        code.addDoubleArray(inputData, Utils.listToArray(trainingPortionOfInputValues));
        final String outputData = Const.OUTPUT + RCodeSession.INSTANCE.getCounter();
        code.addDoubleArray(outputData, Utils.listToArray(trainingPortionOfData));
        String optionalParams = getOptionalParams(params);
        
        final String nnetwork = Const.NNETWORK + RCodeSession.INSTANCE.getCounter();
        code.addRCode(nnetwork + " <- nnet(" + inputData + ", " + outputData + optionalParams + ")");
        
        //toto pouzit na spocitanie tych error measures - napredikuje hodnoty, ktore sa to ucilo:
        //code.addRCode(Const.FORECAST_MODEL + " <- predict(" + Const.NNETWORK + ", type='raw')");
        
        final String testData = Const.TEST + RCodeSession.INSTANCE.getCounter();
        code.addDoubleArray(testData, Utils.listToArray(testingPortionOfInputValues));
        final String forecastModel = Const.FORECAST_MODEL + RCodeSession.INSTANCE.getCounter();
        code.addRCode(forecastModel + " <- predict(" + nnetwork + ", " + testData + ", type='raw')");
        
        
        caller.setRCode(code);
        caller.runAndReturnResult(forecastModel);
        double[] forecasted = caller.getParser().getAsDoubleArray(forecastModel);
        report.setForecastData(Utils.arrayToList(forecasted));
        //..
        //..
        //tu pokracovat: spocitat tie error measures (zatial len tie, co mal nnetar), a zobrazit graf forecasted vals
        //TODO spocitat naozaj tie error measures
        //zatial len dummy data
        List<Double> dummyErrorMeasures = new ArrayList<>();
        dummyErrorMeasures.add(0.0);
        dummyErrorMeasures.add(0.1);
        dummyErrorMeasures.add(0.2);
        dummyErrorMeasures.add(0.3);
        dummyErrorMeasures.add(0.4);
        dummyErrorMeasures.add(0.5);
        dummyErrorMeasures.add(0.6);
        dummyErrorMeasures.add(0.7);
        dummyErrorMeasures.add(0.8);
        dummyErrorMeasures.add(0.9);
        dummyErrorMeasures.add(1.0);
        dummyErrorMeasures.add(1.1);
        report.setErrorMeasures(dummyErrorMeasures);
        
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
        
        //TODO inak spravit ten plot. takto jednoducho to pre nnet nejde. treba asi rucne
        report.setForecastPlotCode("plot(" + forecastModel + ")");
        
        RCodeSession.INSTANCE.setRCode(code);
        
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
