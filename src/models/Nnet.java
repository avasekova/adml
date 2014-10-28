package models;

import java.util.Arrays;
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
        final String ORIGINAL_INPUT = "original." + INPUT;
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
        
        final String FUTURE_FORECASTS = "future." + Const.FORECAST_VALS + Utils.getCounter();
        final String VAL = "val" + Utils.getCounter();
        final String MIN = Const.MIN + Utils.getCounter();
        final String MAX = Const.MAX + Utils.getCounter();
        
        NnetParams params = (NnetParams) parameters;
        TrainAndTestReportCrisp report = new TrainAndTestReportCrisp("nnet");
        report.setModelDescription("(lag=" + params.getLag() + ",hid=" + params.getNumNodesHiddenLayer() + ")");
        List<Double> dataToUse = allData.subList((params.getDataRangeFrom() - 1), params.getDataRangeTo());

        Rengine rengine = MyRengine.getRengine();
        rengine.eval("require(nnet)");

        rengine.assign(ORIGINAL_INPUT, Utils.listToArray(dataToUse));
        rengine.assign(ORIGINAL_OUTPUT, Utils.listToArray(dataToUse));
        
        rengine.eval(SCALED_INPUT + " <- MLPtoR.scale(" + ORIGINAL_INPUT + ")");
        rengine.eval(SCALED_OUTPUT + " <- MLPtoR.scale(" + ORIGINAL_OUTPUT + ")");
        
        rengine.eval(INPUT + " <- " + ORIGINAL_INPUT + "[1:(length(" + ORIGINAL_INPUT + ") - " + params.getLag() + ")]"); //1:(length-lag)
        rengine.eval(OUTPUT + " <- " + ORIGINAL_OUTPUT + "[(1 + " + params.getLag() + "):length(" + ORIGINAL_OUTPUT + ")]"); //(1+lag):length
        
        rengine.eval(SCALED_INPUT + " <- " + SCALED_INPUT + "[1:(length(" + SCALED_INPUT + ") - " + params.getLag() + ")]"); //1:(length-lag)
        rengine.eval(SCALED_OUTPUT + " <- " + SCALED_OUTPUT + "[(1 + " + params.getLag() + "):length(" + SCALED_OUTPUT + ")]"); //(1+lag):length
        
        int numTrainingEntries = Math.round(((float) params.getPercentTrain()/100)*dataToUse.size());
        report.setNumTrainingEntries(numTrainingEntries);
        
        rengine.eval(SCALED_INPUT_TRAIN + " <- " + SCALED_INPUT + "[1:" + numTrainingEntries + "]");
        rengine.eval(SCALED_INPUT_TEST + " <- " + SCALED_INPUT + "[(" + numTrainingEntries + " + 1):length(" + SCALED_INPUT + ")]");
        
        rengine.eval(OUTPUT_TRAIN +        " <- " +        OUTPUT + "[1:" + numTrainingEntries + "]");
        rengine.eval(SCALED_OUTPUT_TRAIN + " <- " + SCALED_OUTPUT + "[1:" + numTrainingEntries + "]");
        
        rengine.eval(OUTPUT_TEST +        " <- " +        OUTPUT + "[(" + numTrainingEntries + " + 1):length(" +        OUTPUT + ")]");
        rengine.eval(SCALED_OUTPUT_TEST + " <- " + SCALED_OUTPUT + "[(" + numTrainingEntries + " + 1):length(" + SCALED_OUTPUT + ")]");
        
        //vybavit lag:
        rengine.eval(ALL_AUX + " <- c(" + OUTPUT_TRAIN + ", " + OUTPUT_TEST + ")");
        rengine.eval(ALL_AUX + " <- c(rep(NA, " + params.getLag() + "), " + ALL_AUX + ")");
        rengine.eval(FINAL_OUTPUT_TRAIN + " <- " + ALL_AUX + "[1:" + numTrainingEntries + "]");
        rengine.eval(FINAL_OUTPUT_TEST + " <- " + ALL_AUX + "[(" + numTrainingEntries + " + 1):" + 
                "length(" + ORIGINAL_OUTPUT + ")]");
        REXP getTrainingOutputs = rengine.eval(FINAL_OUTPUT_TRAIN);
        double[] trainingOutputs = getTrainingOutputs.asDoubleArray();
        REXP getTestingOutputs = rengine.eval(FINAL_OUTPUT_TEST);
        double[] testingOutputs = getTestingOutputs.asDoubleArray();
        report.setRealOutputsTrain(trainingOutputs);
        report.setRealOutputsTest(testingOutputs);
        
        
        String optionalParams = getOptionalParams(params);
        rengine.eval(NNETWORK + " <- nnet::nnet(" + SCALED_INPUT_TRAIN + ", " + SCALED_OUTPUT_TRAIN + optionalParams + ", linout = TRUE)");
        //TODO potom tu nemat natvrdo linout!
        //- dovolit vybrat. akurat bez toho je to len na classification, a neni to zrejme z tych moznosti na vyber
        rengine.eval(FITTED_VALS + " <- fitted.values(" + NNETWORK + ")");
        rengine.eval(UNSCALED_FITTED_VALS + " <- MLPtoR.unscale(" + FITTED_VALS + ", " + ORIGINAL_OUTPUT + ")");
        
        rengine.eval(FORECAST_VALS + " <- predict(" + NNETWORK + ", data.frame(" + SCALED_INPUT_TEST + "), type='raw')");
        rengine.eval(UNSCALED_FORECAST_VALS + " <- MLPtoR.unscale(" + FORECAST_VALS + ", " + ORIGINAL_OUTPUT + ")");
        
        //a teraz to cele posuniem o lag, aby to davalo normalne vysledky:
        //povodne: -----fit-----|---forecast--|--nothin--
        //teraz:   --nothin--|-----fit-----|---forecast--
        rengine.eval(ALL_AUX + " <- c(" + UNSCALED_FITTED_VALS + ", " + UNSCALED_FORECAST_VALS + ")");
        rengine.eval(ALL_AUX + " <- c(rep(NA, " + params.getLag() + "), " + ALL_AUX + ")");
        rengine.eval(FINAL_UNSCALED_FITTED_VALS + " <- " + ALL_AUX + "[1:" + numTrainingEntries + "]");
        rengine.eval(FINAL_UNSCALED_FORECAST_VALS + " <- " + ALL_AUX + "[(" + numTrainingEntries + " + 1):" + 
                "length(" + ORIGINAL_INPUT + ")]");
        
        REXP getFittedVals = rengine.eval(FINAL_UNSCALED_FITTED_VALS);
        double[] fittedVals = getFittedVals.asDoubleArray();
        REXP getForecastVals = rengine.eval(FINAL_UNSCALED_FORECAST_VALS);
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
        
        if (params.getNumForecasts() > 0) {
            //now try to compute future forecasts. funguje to takto: prvy forecast viem spocitat vzdy, lebo si vezmem proste
            //  tu expl var lagnutu (lag bude minimalne 1, takze to bude maximalne posledna hodnota, co mam. kolko bude lag,
            //  tolko poslednych hodnot viem vyuzit na forecasty).
            //vzdy ked spocitam jeden forecast, prilepim si ho za realne hodnoty a pokracujem az kym ich nespocitam vsetky
            //  takto si ich tam nasyslim potrebny pocet (budu scaled), a potom si ich odrezem, unscalujem, a poslem von
            rengine.eval(INPUT + " <- " + ORIGINAL_INPUT); //unscaled
            rengine.eval(MIN + " <- min(" + INPUT + ")");
            rengine.eval(MAX + " <- max(" + INPUT + ")");
            for (int i = 0; i < params.getNumForecasts(); i++) {
                //vezmi si hodnotu expl var, tj. tolkatu od konca aktualnych vals, kolko ma hodnotu lag.
                //konkretne pre R plati, ze chcem: values[length(values)+1-lag]
                //v ORIGINAL_INPUT a ORIGINAL_OUTPUT mam vsetky data. z nich si potrebujem vytiahnut input na predikciu
                rengine.eval(VAL + " <- " + INPUT + "[length(" + INPUT + ") + 1 - " + params.getLag() + "]");
                //scale podla INPUT. ak to je nieco, co tam uz bolo, nascaluje sa to rovnako ako predtym; ak to tam este
                //  nebolo, moze to potencialne vybehnut z 0..1, ale co uz. neviem ako inak na to.
                rengine.eval(VAL + " <- (" + VAL + " - " + MIN + ")/(" + MAX + " - " + MIN + ")"); //scale

                //now predict
                rengine.eval(FORECAST_VALS + " <- predict(" + NNETWORK + ", " + VAL + ", type='raw')");
                //and unscale based on the same thing that was used to scale it, i.e. on ORIGINAL_INPUT
                rengine.eval(UNSCALED_FORECAST_VALS + " <- MLPtoR.unscale(" + FORECAST_VALS + ", " + ORIGINAL_INPUT + ")");

                //add the predicted vals to the input
                rengine.eval(INPUT + " <- c(" + INPUT + ", " + UNSCALED_FORECAST_VALS + ")");
            }
            //now cut the number of forecasts from the end of INPUT:
            rengine.eval(FUTURE_FORECASTS + " <- " + INPUT + "[(length(" + INPUT + ") + 1 - " + params.getNumForecasts() + "):length(" + INPUT + ")]");
            REXP getFutureForecasts = rengine.eval(FUTURE_FORECASTS);
            double[] futureForecasts = getFutureForecasts.asDoubleArray();
            report.setForecastValuesFuture(futureForecasts);
            report.setPlotCode("plot.ts(c(" + FINAL_UNSCALED_FITTED_VALS + ", " + FINAL_UNSCALED_FORECAST_VALS + ", " + FUTURE_FORECASTS + "))");
        } else {
            report.setPlotCode("plot.ts(c(" + FINAL_UNSCALED_FITTED_VALS + ", " + FINAL_UNSCALED_FORECAST_VALS + "))");
        }
        
        report.setNnDiagramPlotCode("plot.nnet(" + NNETWORK + ")");
        
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
