package models;

import java.util.ArrayList;
import java.util.List;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;
import params.NnetParams;
import params.Params;
import utils.Const;
import utils.MyRengine;
import utils.Utils;

public class Nnet implements Forecastable {

    @Override
    public TrainAndTestReport forecast(List<Double> allData, Params parameters) {
        final String INPUT = Const.INPUT + Utils.getCounter();
        final String OUTPUT = Const.OUTPUT + Utils.getCounter();
        final String NNETWORK = Const.NNETWORK + Utils.getCounter();
        final String TEST = Const.TEST + Utils.getCounter();
        final String FORECAST_MODEL = Const.FORECAST_MODEL + Utils.getCounter();
        final String FIT = Const.FIT + Utils.getCounter();
        
        NnetParams params = (NnetParams) parameters;
        TrainAndTestReport report = new TrainAndTestReport("nnet");

        Rengine rengine = MyRengine.getRengine();
        rengine.eval("require(nnet)");

        int numTrainingEntries = Math.round(((float) params.getPercentTrain()/100)*allData.size());
        report.setNumTrainingEntries(numTrainingEntries);
        List<Double> trainingPortionOfData = allData.subList(0, numTrainingEntries);
        List<Double> trainingPortionOfInputValues = params.getInputs().subList(0, numTrainingEntries);
        
        List<Double> testingPortionOfData = allData.subList(numTrainingEntries, allData.size());
        List<Double> testingPortionOfInputValues = params.getInputs().subList(numTrainingEntries, params.getInputs().size());

        rengine.assign(INPUT, Utils.listToArray(trainingPortionOfInputValues));
        rengine.assign(OUTPUT, Utils.listToArray(trainingPortionOfData));
        String optionalParams = getOptionalParams(params);
        
        rengine.eval(NNETWORK + " <- nnet(" + INPUT + ", " + OUTPUT + optionalParams + ", linout = TRUE)");
        //TODO potom tu nemat natvrdo linout!
        //- dovolit vybrat. akurat bez toho je to len na classification, a neni to zrejme z tych moznosti na vyber

        
        //toto pouzit na spocitanie tych error measures - napredikuje hodnoty, ktore sa to ucilo:
        //code.addRCode(Const.FORECAST_MODEL + " <- predict(" + Const.NNETWORK + ", type='raw')");
        
        
        //TODO toto potom preklopit do forecastov - ale zatial to nerobi forecast! (lebo to pouziva TEST)
//        rengine.assign(TEST, Utils.listToArray(testingPortionOfInputValues));
//        rengine.eval(FORECAST_MODEL + " <- predict(" + NNETWORK + ", data.frame(" + TEST + "), type=\"raw\")");
//        REXP getForecastModel = rengine.eval(FORECAST_MODEL);
//        double[] forecast = getForecastModel.asDoubleArray();
//        report.setForecastValues(forecast);
        
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
        
        rengine.eval(FIT + " <- fitted.values(" + NNETWORK + ")");
        REXP getFittedVals = rengine.eval(FIT);
        double[] fitted = getFittedVals.asDoubleArray();
        report.setFittedValues(fitted);
        
        report.setFittedValuesPlotCode("plot.ts(" + FIT + ")");
        
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
        
//        if (params.getLinearElseLogistic() != null) {
//            optionalParams.append(", linout = ").append(params.getLinearElseLogistic());
//        }
//        
//        
//        if (params.getLeastSqrsElseMaxCondLikelihood() != null) {
//            optionalParams.append(", entropy = ").append(params.getLeastSqrsElseMaxCondLikelihood());
//        }
//        
//        if (params.getLoglinSoftmaxElseMaxCondLikelihood() != null) {
//            optionalParams.append(", softmax = ").append(params.getLoglinSoftmaxElseMaxCondLikelihood());
//        }
//        
//        if (params.getCensoredOnElseOff() != null) {
//            optionalParams.append(", censored = ").append(params.getCensoredOnElseOff());
//        }
        
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
