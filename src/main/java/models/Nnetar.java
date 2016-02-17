package models;

import models.params.NnetarParams;
import models.params.Params;
import org.rosuda.JRI.REXP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Nnetar implements Forecastable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(Nnetar.class);

    @Override
    public TrainAndTestReport forecast(Map<String, List<Double>> dataTableModel, Params parameters){
        final String TRAINDATA = Const.TRAINDATA + Utils.getCounter();
        final String NNETWORK = Const.NNETWORK + Utils.getCounter();
        final String FORECAST_MODEL = Const.FORECAST_MODEL + Utils.getCounter();
        final String TEST = Const.TEST + Utils.getCounter();
        final String FIT = Const.FIT + Utils.getCounter();
        final String FORECAST_VALS = Const.FORECAST_VALS + Utils.getCounter();
        final String OUTPUT = Const.OUTPUT + Utils.getCounter();
        final String ORIGINAL_OUTPUT = "original." + OUTPUT;
        final String OUTPUT_TRAIN = Const.OUTPUT + Utils.getCounter();
        final String OUTPUT_TEST = Const.OUTPUT + Utils.getCounter();
        final String FINAL_OUTPUT_TRAIN = "final." + OUTPUT_TRAIN;
        final String FINAL_OUTPUT_TEST = "final." + OUTPUT_TEST;
        
        NnetarParams params = (NnetarParams) parameters;
        TrainAndTestReportCrisp report = new TrainAndTestReportCrisp(Model.NNETAR);
        report.setModelDescription(params.toString());

        logger.info("Params {}", params.getColName());

        List<Double> allData = Collections.unmodifiableList(new ArrayList<>(dataTableModel.get(params.getColName())));
        List<Double> dataToUse = allData.subList((params.getDataRangeFrom() - 1), params.getDataRangeTo());
        
        MyRengine rengine = MyRengine.getRengine();
        rengine.require("forecast");
        int numTrainingEntries = Math.round(((float) params.getPercentTrain()/100)*dataToUse.size());
        report.setNumTrainingEntries(numTrainingEntries);
        
        
        rengine.assign(ORIGINAL_OUTPUT, Utils.listToArray(dataToUse));
        rengine.eval(OUTPUT + " <- " + ORIGINAL_OUTPUT + "[(1 + " + params.getNumNonSeasonalLags() + "):length(" + ORIGINAL_OUTPUT + ")]"); //(1+lag):length
        rengine.eval(OUTPUT_TRAIN +       " <- " +        OUTPUT + "[1:" + numTrainingEntries + "]");
        rengine.eval(OUTPUT_TEST +        " <- " +        OUTPUT + "[(" + numTrainingEntries + " + 1):length(" +        OUTPUT + ")]");
        List<Double> trainingPortionOfData = dataToUse.subList(0, numTrainingEntries);
        List<Double> testingPortionOfData = dataToUse.subList(numTrainingEntries, dataToUse.size());
        //take care of the lag:
        rengine.eval(FINAL_OUTPUT_TRAIN + " <- " + ORIGINAL_OUTPUT + "[1:" + numTrainingEntries + "]");
        rengine.eval(FINAL_OUTPUT_TEST + " <- " + ORIGINAL_OUTPUT + "[(" + numTrainingEntries + " + 1):" + 
                "length(" + ORIGINAL_OUTPUT + ")]");
        double[] trainingOutputs = rengine.evalAndReturnArray(FINAL_OUTPUT_TRAIN);
        double[] testingOutputs = rengine.evalAndReturnArray(FINAL_OUTPUT_TEST);
        report.setRealOutputsTrain(trainingOutputs);
        report.setRealOutputsTest(testingOutputs);
        
        
        
        rengine.assign(TRAINDATA, Utils.listToArray(trainingPortionOfData));
        String optionalParams = getOptionalParams(params);
        rengine.eval(NNETWORK + " <- forecast::nnetar(" + TRAINDATA + optionalParams + ")");
        
        int numForecasts = testingPortionOfData.size() + params.getNumForecasts();
        rengine.eval(FORECAST_MODEL + " <- forecast::forecast(" + NNETWORK + ", " + numForecasts + ")");

        //1. the "forecastedModel" contains many heterogeneous pieces of information, it cannot just be sent to Java as a whole
        //2. so if I want the forecastedValues, I can get them as "forecastedModel$mean[1:N]", where N is their number...
        rengine.eval(FORECAST_VALS + " <- " + FORECAST_MODEL + "$mean[1:" + numForecasts + "]");
        List<Double> allForecastsList = rengine.evalAndReturnList(FORECAST_VALS);
        List<Double> forecastTest = allForecastsList.subList(0, testingPortionOfData.size());
        report.setForecastValuesTest(Utils.listToArray(forecastTest));
        report.setForecastValuesFuture(Utils.listToArray(allForecastsList.subList(testingPortionOfData.size(), allForecastsList.size())));
        
        rengine.assign(TEST, Utils.listToArray(testingPortionOfData));
        //TODO maybe just accuracy(model) instead of accuracy(model, testingData)?
        REXP getAcc = rengine.eval("forecast::accuracy(" + FORECAST_MODEL + ", " + TEST + ")[1:12]");//[1:12] because in the new version,
        // ACF was added and sometimes causes trouble
//        double[] acc = getAcc.asDoubleArray(); //careful with the order of results
        //returns results in columns, i.e. ME train, ME test, RMSE train, RMSE test, MAE, MPE, MAPE, MASE
        //the new version produces ACF1 as well
        
        rengine.eval(FIT + " <- fitted.values(" + NNETWORK + ")");
        double[] fitted = rengine.evalAndReturnArray(FIT);
        report.setFittedValues(fitted);
        
        ErrorMeasuresCrisp errorMeasures = ErrorMeasuresUtils.computeAllErrorMeasuresCrisp(Utils.arrayToList(trainingOutputs), 
                Utils.arrayToList(testingOutputs), Utils.arrayToList(fitted), forecastTest, parameters.getSeasonality());
        report.setErrorMeasures(errorMeasures);
        
        
        
        report.setPlotCode("plot.ts(c(" + Utils.arrayToRVectorString(fitted) + "," + Utils.listToRVectorString(allForecastsList) + "))");
        
        //TODO choose the best and plot that one. for now, plots the first :/
        report.setNnDiagramPlotCode("plot.nnet(" + NNETWORK + "$model[[1]]$wts, struct = " + NNETWORK + "$model[[1]]$n)");
        
        //careful, do not remove anything from the plots
        rengine.rm(TRAINDATA, FORECAST_MODEL, TEST, OUTPUT, ORIGINAL_OUTPUT, OUTPUT_TRAIN, OUTPUT_TEST, FINAL_OUTPUT_TRAIN, FINAL_OUTPUT_TEST, FIT, FORECAST_VALS);
        
        return report;
    }

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
