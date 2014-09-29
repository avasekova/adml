package models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;
import params.NnetParams;
import params.Params;
import utils.Const;
import utils.MyRengine;
import utils.Utils;

public class Nnet implements Forecastable { //TODO note: berie len jeden vstup a jeden vystup! inak treba zovseobecnit kopu
    //           veci, napr. scaling, a dalsie volania rengine

    @Override
    public TrainAndTestReport forecast(List<Double> allData, Params parameters) {
        final String INPUT = Const.INPUT + Utils.getCounter();
        final String MIN_INPUT = Const.MIN + Utils.getCounter();
        final String MAX_INPUT = Const.MAX + Utils.getCounter();
        final String SCALED_INPUT = "scaled." + INPUT;
        final String OUTPUT = Const.OUTPUT + Utils.getCounter();
        final String MIN_OUTPUT = Const.MIN + Utils.getCounter();
        final String MAX_OUTPUT = Const.MAX + Utils.getCounter();
        final String SCALED_OUTPUT = "scaled." + OUTPUT;
        final String NNETWORK = Const.NNETWORK + Utils.getCounter();
        final String TEST = Const.TEST + Utils.getCounter();
        final String FORECAST_MODEL = Const.FORECAST_MODEL + Utils.getCounter();
        final String FIT = Const.FIT + Utils.getCounter();
        final String SCALED_FIT = "scaled." + FIT;
        
        NnetParams params = (NnetParams) parameters;
        TrainAndTestReport report = new TrainAndTestReport("nnet");

        Rengine rengine = MyRengine.getRengine();
        rengine.eval("require(nnet)");

        int numTrainingEntries = Math.round(((float) params.getPercentTrain()/100)*allData.size());
        report.setNumTrainingEntries(numTrainingEntries);
        List<Double> trainingPortionOfData = allData.subList(0, numTrainingEntries);
        List<Double> trainingPortionOfInputValues = params.getInputs().subList(0, numTrainingEntries);
        
        //TODO make it draw the testing and forecast data as well
        List<Double> testingPortionOfData = allData.subList(numTrainingEntries, allData.size());
        List<Double> testingPortionOfInputValues = params.getInputs().subList(numTrainingEntries, params.getInputs().size());

        rengine.assign(INPUT, Utils.listToArray(trainingPortionOfInputValues));
        rengine.assign(OUTPUT, Utils.listToArray(trainingPortionOfData));
        String optionalParams = getOptionalParams(params);
        
        //scale the input first, so that we do not get the same value for all predictions:
        rengine.eval(MIN_INPUT + " <- min(" + INPUT + ")");
        rengine.eval(MAX_INPUT + " <- max(" + INPUT + ")");
        rengine.eval(MIN_OUTPUT + " <- min(" + OUTPUT + ")");
        rengine.eval(MAX_OUTPUT + " <- max(" + OUTPUT + ")");
        rengine.eval(SCALED_INPUT + " <- (" + INPUT + " - " + MIN_INPUT + ")/(" + MAX_INPUT + " - " + MIN_INPUT + ")");
        rengine.eval(SCALED_OUTPUT + " <- (" + OUTPUT + " - " + MIN_OUTPUT + ")/(" + MAX_OUTPUT + " - " + MIN_OUTPUT + ")");
        rengine.eval(NNETWORK + " <- nnet(" + SCALED_INPUT + ", " + SCALED_OUTPUT + optionalParams + ", linout = TRUE)");
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
        
        rengine.eval(FIT + " <- fitted.values(" + NNETWORK + ")");
        
        //scale back the fitted values:
        rengine.eval(SCALED_FIT + " <- " + FIT + " * (" + MAX_OUTPUT + " - " + MIN_OUTPUT + ") + " + MIN_OUTPUT);
        REXP getFittedVals = rengine.eval(SCALED_FIT);
        double[] fitted = getFittedVals.asDoubleArray();
        report.setFittedValues(fitted);
        System.out.println("" + Arrays.toString(fitted));
        
        report.setFittedValuesPlotCode("plot.ts(" + SCALED_FIT + ")");
        
               /*TODO zmazat!!!*/             report.setForecastValues(fitted); //iba zatial, aby kreslilo plot
        
        return report;
    }

    @Override
    public String getOptionalParams(Params parameters) {
        NnetParams params = (NnetParams) parameters;
        StringBuilder optionalParams = new StringBuilder();
        
        //weights
        
        if (params.getNumNodesHiddenLayer() != null) {
            optionalParams.append(", size = ").append(params.getNumNodesHiddenLayer());
        } else {
            optionalParams.append(", size = 1 ");
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

    @Override
    public TrainAndTestReport forecastIntervalCenterRadius(List<Double> centerData, List<Double> radiusData, Params parameters) {
        throw new UnsupportedOperationException("Not supported for crisp data.");
    }

    @Override
    public TrainAndTestReport forecastIntervalLowerUpper(List<Double> lowerData, List<Double> upperData, Params parameters) {
        throw new UnsupportedOperationException("Not supported for crisp data.");
    }
    
}
