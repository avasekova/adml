package models;

import models.params.NnetParams;
import models.params.Params;
import utils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Nnet implements Forecastable {
    private static final long serialVersionUID = 1L;
    
    private int maxLag = 0; //a stupid solution, but whatever

    @Override
    public TrainAndTestReport forecast(Map<String, List<Double>> dataTableModel, Params parameters) {
        final String OUTPUT = Const.OUTPUT + Utils.getCounter();
        final String SCALED_OUTPUT = "scaled." + OUTPUT;
        final String ORIGINAL_OUTPUT = "original." + OUTPUT;
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
        final String ALL_AUX = "aux" + Utils.getCounter();
        final String FINAL_UNSCALED_FITTED_VALS = "final." + UNSCALED_FITTED_VALS;
        final String FINAL_UNSCALED_FORECAST_VALS = "final." + UNSCALED_FORECAST_VALS;
        final String FINAL_OUTPUT_TRAIN = "final." + OUTPUT_TRAIN;
        final String FINAL_OUTPUT_TEST = "final." + OUTPUT_TEST;
        
//        final String FUTURE_FORECASTS = "future." + Const.FORECAST_VALS + Utils.getCounter();
//        final String VAL = "val" + Utils.getCounter();
//        final String MIN = Const.MIN + Utils.getCounter();
//        final String MAX = Const.MAX + Utils.getCounter();
        
        NnetParams params = (NnetParams) parameters;
        TrainAndTestReportCrisp report = new TrainAndTestReportCrisp(Model.NNET);
        report.setModelDescription(params.toString());
        
        List<List<Double>> data = prepareData(dataTableModel, params.getExplVars(), params.getDataRangeFrom()-1, params.getDataRangeTo());
        int numTrainingEntries = Math.round(((float) params.getPercentTrain()/100)*(params.getDataRangeTo()-(params.getDataRangeFrom()-1)));
        report.setNumTrainingEntries(numTrainingEntries);
//        numTrainingEntries -= maxLag; //TODO not sure about this! (works better with AVG if disabled... stil not sure about it though)
        
        List<List<Double>> allInputsScaled = RBF.getScaledInputs(data, 0);
        List<List<Double>> trainingInputsScaled = RBF.getInputsCut(allInputsScaled, 0, numTrainingEntries);
        List<List<Double>> testingInputsScaled = RBF.getInputsCut(allInputsScaled, numTrainingEntries, allInputsScaled.get(0).size());
        
        MyRengine rengine = MyRengine.getRengine();
        rengine.require("nnet");

        rengine.assignMatrix(SCALED_INPUT_TRAIN, trainingInputsScaled);
        rengine.assignMatrix(SCALED_INPUT_TEST, testingInputsScaled);

        List<Double> allDataOutput = dataTableModel.get(params.getColName());
        List<Double> dataToUseOutput = allDataOutput.subList((params.getDataRangeFrom() - 1), params.getDataRangeTo());
        
        rengine.assign(ORIGINAL_OUTPUT, Utils.listToArray(dataToUseOutput));
        rengine.eval(OUTPUT + " <- " + ORIGINAL_OUTPUT + "[(1 + " + maxLag + "):length(" + ORIGINAL_OUTPUT + ")]"); //(1+lag):length //TODO are we sure about this? or should be from the beginning?
        rengine.eval(OUTPUT_TRAIN +        " <- " +        OUTPUT + "[1:" + numTrainingEntries + "]");
        rengine.eval(OUTPUT_TEST +        " <- " +        OUTPUT + "[(" + numTrainingEntries + " + 1):length(" +        OUTPUT + ")]");
        
        rengine.eval(SCALED_OUTPUT + " <- MLPtoR.scale(" + ORIGINAL_OUTPUT + ")");
        rengine.eval(SCALED_OUTPUT + " <- " + SCALED_OUTPUT + "[(1 + " + maxLag + "):length(" + SCALED_OUTPUT + ")]"); //(1+lag):length //TODO are we sure about this? or should be from the beginning?
        rengine.eval(SCALED_OUTPUT_TRAIN + " <- " + SCALED_OUTPUT + "[1:" + numTrainingEntries + "]");
        rengine.eval(SCALED_OUTPUT_TEST + " <- " + SCALED_OUTPUT + "[(" + numTrainingEntries + " + 1):length(" + SCALED_OUTPUT + ")]");
        
        rengine.eval(FINAL_OUTPUT_TRAIN + " <- " + ORIGINAL_OUTPUT + "[1:" + numTrainingEntries + "]");
        rengine.eval(FINAL_OUTPUT_TEST + " <- " + ORIGINAL_OUTPUT + "[(" + numTrainingEntries + " + 1):" + 
                "length(" + ORIGINAL_OUTPUT + ")]");
        double[] trainingOutputs = rengine.evalAndReturnArray(FINAL_OUTPUT_TRAIN);
        double[] testingOutputs = rengine.evalAndReturnArray(FINAL_OUTPUT_TEST);
        report.setRealOutputsTrain(trainingOutputs);
        report.setRealOutputsTest(testingOutputs);
        
        
        String optionalParams = getOptionalParams(params);
        rengine.eval(NNETWORK + " <- nnet::nnet(" + SCALED_INPUT_TRAIN + ", " + SCALED_OUTPUT_TRAIN + optionalParams + ", linout = TRUE)");
        //TODO later do not always put linout, customize
        //- but without linout it is just for classification
        rengine.eval(FITTED_VALS + " <- fitted.values(" + NNETWORK + ")");
        rengine.eval(UNSCALED_FITTED_VALS + " <- MLPtoR.unscale(" + FITTED_VALS + ", " + ORIGINAL_OUTPUT + ")");
        
        rengine.eval(FORECAST_VALS + " <- predict(" + NNETWORK + ", data.frame(" + SCALED_INPUT_TEST + "), type='raw')");
        rengine.eval(UNSCALED_FORECAST_VALS + " <- MLPtoR.unscale(" + FORECAST_VALS + ", " + ORIGINAL_OUTPUT + ")");
        
        //and now shift it all by lag:
        //originally: -----fit-----|---forecast--|--nothin--
        //now:        --nothin--|-----fit-----|---forecast--
        rengine.eval(ALL_AUX + " <- c(" + UNSCALED_FITTED_VALS + ", " + UNSCALED_FORECAST_VALS + ")");
        rengine.eval(ALL_AUX + " <- c(rep(NA, " + maxLag + "), " + ALL_AUX + ")");
        rengine.eval(FINAL_UNSCALED_FITTED_VALS + " <- " + ALL_AUX + "[1:" + numTrainingEntries + "]");
        rengine.eval(FINAL_UNSCALED_FORECAST_VALS + " <- " + ALL_AUX + "[(" + numTrainingEntries + " + 1):" + 
                "length(" + ORIGINAL_OUTPUT + ")]");
        
        double[] fittedVals = rengine.evalAndReturnArray(FINAL_UNSCALED_FITTED_VALS);
        double[] forecastVals = rengine.evalAndReturnArray(FINAL_UNSCALED_FORECAST_VALS);
        
        report.setFittedValues(fittedVals);
        report.setForecastValuesTest(forecastVals);
//        report.setForecastValuesFuture(); //nothing yet
        //TODO: it _could_ forecast as long as it does not have expl vars, only lag
        
        
        ErrorMeasuresCrisp errorMeasures = ErrorMeasuresUtils.computeAllErrorMeasuresCrisp(Utils.arrayToList(trainingOutputs),
                Utils.arrayToList(testingOutputs), Utils.arrayToList(fittedVals), Utils.arrayToList(forecastVals), 
                params.getSeasonality());
        report.setErrorMeasures(errorMeasures);
        
        //for now, do not compute forecasts with expl vars; commented out
//        if (params.getNumForecasts() > 0) {
//            //now try to compute future forecasts. it works as follows: we can always compute the 1st forecast, just take
//            //  the lagged expl var (lag at least 1, so it will be at most the last val, and I have that. we can use (lag) number
//            //  of last values for forecasts.
//            //every time we compute a forecast, we append it at the end of real vals and continue until we have all of them
//            //  this way we compute the requested num (they'll be scaled), and then I cut them off, unscale them, and send them out
//            rengine.eval(INPUT + " <- " + ORIGINAL_INPUT); //unscaled
//            rengine.eval(MIN + " <- min(" + INPUT + ")");
//            rengine.eval(MAX + " <- max(" + INPUT + ")");
//            for (int i = 0; i < params.getNumForecasts(); i++) {
//                //take the LAG-th expl var from the end, i.e. values[length(values)+1-lag]
//                //take the input for forecasts from ORIGINAL_INPUT and ORIGINAL_OUTPUT
//                rengine.eval(VAL + " <- " + INPUT + "[length(" + INPUT + ") + 1 - " + params.getLag() + "]");
//                //scale by INPUT. if it's something that's already been there, it will scale the same way; if it hasn't,
//                //  it could be outside 0..1, but whatever. I don't know what else to do.
//                rengine.eval(VAL + " <- (" + VAL + " - " + MIN + ")/(" + MAX + " - " + MIN + ")"); //scale
//
//                //now predict
//                rengine.eval(FORECAST_VALS + " <- predict(" + NNETWORK + ", " + VAL + ", type='raw')");
//                //and unscale based on the same thing that was used to scale it, i.e. on ORIGINAL_INPUT
//                rengine.eval(UNSCALED_FORECAST_VALS + " <- MLPtoR.unscale(" + FORECAST_VALS + ", " + ORIGINAL_INPUT + ")");
//
//                //add the predicted vals to the input
//                rengine.eval(INPUT + " <- c(" + INPUT + ", " + UNSCALED_FORECAST_VALS + ")");
//            }
//            //now cut the number of forecasts from the end of INPUT:
//            rengine.eval(FUTURE_FORECASTS + " <- " + INPUT + "[(length(" + INPUT + ") + 1 - " + params.getNumForecasts() + "):length(" + INPUT + ")]");
//            REXP getFutureForecasts = rengine.eval(FUTURE_FORECASTS);
//            double[] futureForecasts = getFutureForecasts.asDoubleArray();
//            report.setForecastValuesFuture(futureForecasts);
//            report.setPlotCode("plot.ts(c(" + FINAL_UNSCALED_FITTED_VALS + ", " + FINAL_UNSCALED_FORECAST_VALS + ", " + FUTURE_FORECASTS + "))");
//        } else {
            report.setPlotCode("plot.ts(c(" + Utils.arrayToRVectorString(fittedVals) + ", " + Utils.arrayToRVectorString(forecastVals) + "))");
//        }
        
        report.setNnDiagramPlotCode("plot.nnet(" + NNETWORK + ")");
        
        rengine.rm(OUTPUT, SCALED_OUTPUT, ORIGINAL_OUTPUT, INPUT_TRAIN, SCALED_INPUT_TRAIN, INPUT_TEST, SCALED_INPUT_TEST, OUTPUT_TRAIN, SCALED_OUTPUT_TRAIN,
                OUTPUT_TEST, SCALED_OUTPUT_TEST, FORECAST_VALS, FITTED_VALS, UNSCALED_FITTED_VALS, UNSCALED_FORECAST_VALS, ALL_AUX, FINAL_OUTPUT_TRAIN, FINAL_OUTPUT_TEST, NNETWORK,
                FINAL_UNSCALED_FITTED_VALS, FINAL_UNSCALED_FORECAST_VALS);
        
        return report;
    }

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
    
    private List<List<Double>> prepareData(Map<String, List<Double>> dataTableModel, List<CrispVariable> explVars,
                                                                          int from, int to) {
        List<List<Double>> data = new ArrayList<>();
        
        int maximumLag = 0;
        for (CrispVariable var : explVars) {
            List<Double> vals = dataTableModel.get(var.getFieldName()).subList(from, to);
            data.add(IntervalMLPCcode.lagBy(var.getLag(), vals));
            
            maximumLag = Math.max(maximumLag, var.getLag());
        }
        
        this.maxLag = maximumLag;
        
        return IntervalMLPCcode.trimDataToRectangle(data, maximumLag);
    }
}
