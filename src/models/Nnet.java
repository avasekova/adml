package models;

import java.util.List;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;
import params.NnetParams;
import params.Params;
import utils.Const;
import utils.ErrorMeasuresCrisp;
import utils.ErrorMeasuresUtils;
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
        TrainAndTestReportCrisp report = new TrainAndTestReportCrisp("nnet");

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
        
        rengine.eval(FIT + " <- fitted.values(" + NNETWORK + ")");
        
        //scale back the fitted values:
        rengine.eval(SCALED_FIT + " <- " + FIT + " * (" + MAX_OUTPUT + " - " + MIN_OUTPUT + ") + " + MIN_OUTPUT);
        REXP getFittedVals = rengine.eval(SCALED_FIT);
        double[] fitted = getFittedVals.asDoubleArray();
        report.setFittedValues(fitted);
        
        //TODO spocitat zbytok tych error measures
        List<Double> errorsTrain = Utils.getErrors(trainingPortionOfData, Utils.arrayToList(fitted));
        ErrorMeasuresCrisp errorMeasures = new ErrorMeasuresCrisp();
        errorMeasures.setMEtrain(ErrorMeasuresUtils.ME(errorsTrain));
        errorMeasures.setMEtest(0.0);
        errorMeasures.setRMSEtrain(ErrorMeasuresUtils.RMSE(errorsTrain));
        errorMeasures.setRMSEtest(0.0);
        errorMeasures.setMAEtrain(ErrorMeasuresUtils.MAE(errorsTrain));
        errorMeasures.setMAEtest(0.0);
        errorMeasures.setMPEtrain(ErrorMeasuresUtils.MPE(trainingPortionOfData, Utils.arrayToList(fitted)));
        errorMeasures.setMPEtest(0.0);
        errorMeasures.setMAPEtrain(ErrorMeasuresUtils.MAPE(trainingPortionOfData, Utils.arrayToList(fitted)));
        errorMeasures.setMAPEtest(0.0);
        errorMeasures.setMASEtrain(ErrorMeasuresUtils.MASE(trainingPortionOfData, Utils.arrayToList(fitted)));
        errorMeasures.setMASEtest(0.0);
        errorMeasures.setMSEtrain(ErrorMeasuresUtils.MSE(errorsTrain));
        errorMeasures.setMSEtest(0.0);
        errorMeasures.setTheilUtrain(ErrorMeasuresUtils.theilsU(trainingPortionOfData, Utils.arrayToList(fitted)));
        errorMeasures.setTheilUtest(42);
        report.setErrorMeasures(errorMeasures);
        
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
}
