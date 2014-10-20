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
        final String SCALED_INPUT = "scaled." + INPUT;
        final String OUTPUT = Const.OUTPUT + Utils.getCounter();
        final String SCALED_OUTPUT = "scaled." + OUTPUT;
        final String INPUT_TRAIN = Const.INPUT + Utils.getCounter();
        final String SCALED_INPUT_TRAIN = "scaled." + INPUT_TRAIN;
        final String INPUT_TEST = Const.INPUT + Utils.getCounter();
        final String SCALED_INPUT_TEST = "scaled." + INPUT_TEST;
        final String OUTPUT_TRAIN = Const.OUTPUT + Utils.getCounter();
        final String SCALED_OUTPUT_TRAIN = "scaled." + OUTPUT_TRAIN;
        final String OUTPUT_TEST = Const.OUTPUT + Utils.getCounter();
        final String SCALED_OUTPUT_TEST = "scaled." + OUTPUT_TEST;
        final String NNETWORK = Const.NNETWORK + Utils.getCounter();
        final String FORECAST_VALS = Const.FORECAST_VALS + Utils.getCounter();
        final String FITTED_VALS = Const.FIT + Utils.getCounter();
        final String UNSCALED_FITTED_VALS = "unscaled." + FITTED_VALS;
        final String UNSCALED_FORECAST_VALS = "unscaled." + FORECAST_VALS;
        
        NnetParams params = (NnetParams) parameters;
        TrainAndTestReportCrisp report = new TrainAndTestReportCrisp("nnet");
        List<Double> dataToUse = allData.subList((params.getDataRangeFrom() - 1), params.getDataRangeTo());

        Rengine rengine = MyRengine.getRengine();
        rengine.eval("require(nnet)");

        rengine.assign(INPUT, Utils.listToArray(dataToUse));
        rengine.assign(OUTPUT, Utils.listToArray(dataToUse));
        rengine.eval(INPUT + " <- " + INPUT + "[1:(length(" + INPUT + ") - " + params.getLag() + ")]"); //1:(length-lag)
        rengine.eval(OUTPUT + " <- " + OUTPUT + "[(1 + " + params.getLag() + "):length(" + OUTPUT + ")]"); //(1+lag):length
        
        rengine.eval(SCALED_INPUT + " <- MLPtoR.scale(" + INPUT + ")");
        rengine.eval(SCALED_OUTPUT + " <- MLPtoR.scale(" + OUTPUT + ")");
        
        int lengthInputOutput = dataToUse.size() - params.getLag();
        int numTrainingEntries = Math.round(((float) params.getPercentTrain()/100)*lengthInputOutput);
        report.setNumTrainingEntries(numTrainingEntries);
        
        rengine.eval(INPUT_TRAIN +        " <- " +        INPUT + "[1:" + numTrainingEntries + "]");
        rengine.eval(SCALED_INPUT_TRAIN + " <- " + SCALED_INPUT + "[1:" + numTrainingEntries + "]");
        rengine.eval(INPUT_TEST +        " <- " +        INPUT + "[" + (numTrainingEntries+1) + ":length(" +        INPUT + ")]");
        rengine.eval(SCALED_INPUT_TEST + " <- " + SCALED_INPUT + "[" + (numTrainingEntries+1) + ":length(" + SCALED_INPUT + ")]");
        
        rengine.eval(OUTPUT_TRAIN +        " <- " +        OUTPUT + "[1:" + numTrainingEntries + "]");
        rengine.eval(SCALED_OUTPUT_TRAIN + " <- " + SCALED_OUTPUT + "[1:" + numTrainingEntries + "]");
        
        rengine.eval(OUTPUT_TEST +        " <- " +        OUTPUT + "[" + (numTrainingEntries+1) + ":length(" +        OUTPUT + ")]");
        rengine.eval(SCALED_OUTPUT_TEST + " <- " + SCALED_OUTPUT + "[" + (numTrainingEntries+1) + ":length(" + SCALED_OUTPUT + ")]");
        
        REXP getTrainingOutputs = rengine.eval(OUTPUT_TRAIN);
        double[] trainingOutputs = getTrainingOutputs.asDoubleArray();
        
        REXP getTestingOutputs = rengine.eval(OUTPUT_TEST);
        double[] testingOutputs = getTestingOutputs.asDoubleArray();
        
        
        String optionalParams = getOptionalParams(params);
        rengine.eval(NNETWORK + " <- nnet::nnet(" + SCALED_INPUT_TRAIN + ", " + SCALED_OUTPUT_TRAIN + optionalParams + ", linout = TRUE)");
        //TODO potom tu nemat natvrdo linout!
        //- dovolit vybrat. akurat bez toho je to len na classification, a neni to zrejme z tych moznosti na vyber
        rengine.eval(FITTED_VALS + " <- fitted.values(" + NNETWORK + ")");
        rengine.eval(UNSCALED_FITTED_VALS + " <- MLPtoR.unscale(" + FITTED_VALS + ", " + OUTPUT + ")");
        
        rengine.eval(FORECAST_VALS + " <- predict(" + NNETWORK + ", data.frame(" + SCALED_INPUT_TEST + "), type='raw')");
        rengine.eval(UNSCALED_FORECAST_VALS + " <- MLPtoR.unscale(" + FORECAST_VALS + ", " + OUTPUT + ")");
        
        REXP getFittedVals = rengine.eval(UNSCALED_FITTED_VALS);
        double[] fittedVals = getFittedVals.asDoubleArray();
        REXP getForecastVals = rengine.eval(UNSCALED_FORECAST_VALS);
        double[] forecastVals = getForecastVals.asDoubleArray();
        
        report.setFittedValues(fittedVals);
        report.setForecastValuesTest(forecastVals);
//        report.setForecastValuesFuture(); //nothing yet
        //TODO: it _could_ forecast as long as it does not have expl vars, only lag
        
        //TODO spocitat zbytok tych error measures
        ErrorMeasuresCrisp errorMeasures = new ErrorMeasuresCrisp();
        errorMeasures.setMEtrain(ErrorMeasuresUtils.ME(Utils.arrayToList(trainingOutputs), Utils.arrayToList(fittedVals)));
        errorMeasures.setMEtest(ErrorMeasuresUtils.ME(Utils.arrayToList(testingOutputs), Utils.arrayToList(forecastVals)));
        errorMeasures.setRMSEtrain(ErrorMeasuresUtils.RMSE(Utils.arrayToList(trainingOutputs), Utils.arrayToList(fittedVals)));
        errorMeasures.setRMSEtest(ErrorMeasuresUtils.RMSE(Utils.arrayToList(testingOutputs), Utils.arrayToList(forecastVals)));
        errorMeasures.setMAEtrain(ErrorMeasuresUtils.MAE(Utils.arrayToList(trainingOutputs), Utils.arrayToList(fittedVals)));
        errorMeasures.setMAEtest(ErrorMeasuresUtils.MAE(Utils.arrayToList(testingOutputs), Utils.arrayToList(forecastVals)));
        errorMeasures.setMPEtrain(ErrorMeasuresUtils.MPE(Utils.arrayToList(trainingOutputs), Utils.arrayToList(fittedVals)));
        errorMeasures.setMPEtest(ErrorMeasuresUtils.MPE(Utils.arrayToList(testingOutputs), Utils.arrayToList(forecastVals)));
        errorMeasures.setMAPEtrain(ErrorMeasuresUtils.MAPE(Utils.arrayToList(trainingOutputs), Utils.arrayToList(fittedVals)));
        errorMeasures.setMAPEtest(ErrorMeasuresUtils.MAPE(Utils.arrayToList(testingOutputs), Utils.arrayToList(forecastVals)));
        errorMeasures.setMASEtrain(ErrorMeasuresUtils.MASE(Utils.arrayToList(trainingOutputs), Utils.arrayToList(fittedVals)));
        errorMeasures.setMASEtest(ErrorMeasuresUtils.MASE(Utils.arrayToList(testingOutputs), Utils.arrayToList(forecastVals)));
        errorMeasures.setMSEtrain(ErrorMeasuresUtils.MSE(Utils.arrayToList(trainingOutputs), Utils.arrayToList(fittedVals)));
        errorMeasures.setMSEtest(ErrorMeasuresUtils.MSE(Utils.arrayToList(testingOutputs), Utils.arrayToList(forecastVals)));
        errorMeasures.setTheilUtrain(ErrorMeasuresUtils.theilsU(Utils.arrayToList(trainingOutputs), Utils.arrayToList(fittedVals)));
        errorMeasures.setTheilUtest(ErrorMeasuresUtils.theilsU(Utils.arrayToList(testingOutputs), Utils.arrayToList(forecastVals)));
        report.setErrorMeasures(errorMeasures);
        
        report.setPlotCode("plot.ts(c(rep(NA, " + params.getLag() + "), " + UNSCALED_FITTED_VALS + ", " + UNSCALED_FORECAST_VALS + "))");
        
        report.setNnDiagramPlotCode("plot.nnet(" + NNETWORK + ", main = \"nnet\")");
        
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
