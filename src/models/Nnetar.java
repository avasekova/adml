package models;

import gui.tablemodels.DataTableModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;
import params.NnetarParams;
import params.Params;
import utils.Const;
import utils.ErrorMeasuresCrisp;
import utils.ErrorMeasuresUtils;
import utils.MyRengine;
import utils.Utils;

public class Nnetar implements Forecastable {
    
    @Override
    public TrainAndTestReport forecast(DataTableModel dataTableModel, Params parameters){
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
        TrainAndTestReportCrisp report = new TrainAndTestReportCrisp("nnetar");
        report.setModelDescription("(lagSeas=" + params.getNumSeasonalLags()
         + ",lagNon=" + params.getNumNonSeasonalLags() + ",hid=" + params.getNumNodesHidden() + ")");
        
        List<Double> allData = Collections.unmodifiableList(new ArrayList<>(dataTableModel.getDataForColname(params.getColName())));
        List<Double> dataToUse = allData.subList((params.getDataRangeFrom() - 1), params.getDataRangeTo());
        
        Rengine rengine = MyRengine.getRengine();
        rengine.eval("require(forecast)");
        int numTrainingEntries = Math.round(((float) params.getPercentTrain()/100)*dataToUse.size());
        report.setNumTrainingEntries(numTrainingEntries);
        
        
        rengine.assign(ORIGINAL_OUTPUT, Utils.listToArray(dataToUse));
        rengine.eval(OUTPUT + " <- " + ORIGINAL_OUTPUT + "[(1 + " + params.getNumNonSeasonalLags() + "):length(" + ORIGINAL_OUTPUT + ")]"); //(1+lag):length
        rengine.eval(OUTPUT_TRAIN +       " <- " +        OUTPUT + "[1:" + numTrainingEntries + "]");
        rengine.eval(OUTPUT_TEST +        " <- " +        OUTPUT + "[(" + numTrainingEntries + " + 1):length(" +        OUTPUT + ")]");
        List<Double> trainingPortionOfData = dataToUse.subList(0, numTrainingEntries);
        List<Double> testingPortionOfData = dataToUse.subList(numTrainingEntries, dataToUse.size());
        //vybavit lag:
        rengine.eval(FINAL_OUTPUT_TRAIN + " <- " + ORIGINAL_OUTPUT + "[1:" + numTrainingEntries + "]");
        rengine.eval(FINAL_OUTPUT_TEST + " <- " + ORIGINAL_OUTPUT + "[(" + numTrainingEntries + " + 1):" + 
                "length(" + ORIGINAL_OUTPUT + ")]");
        REXP getTrainingOutputs = rengine.eval(FINAL_OUTPUT_TRAIN);
        double[] trainingOutputs = getTrainingOutputs.asDoubleArray();
        REXP getTestingOutputs = rengine.eval(FINAL_OUTPUT_TEST);
        double[] testingOutputs = getTestingOutputs.asDoubleArray();
        report.setRealOutputsTrain(trainingOutputs);
        report.setRealOutputsTest(testingOutputs);
        
        
        
        rengine.assign(TRAINDATA, Utils.listToArray(trainingPortionOfData));
        String optionalParams = getOptionalParams(params);
        rengine.eval(NNETWORK + " <- forecast::nnetar(" + TRAINDATA + optionalParams + ")");
        
        int numForecasts = testingPortionOfData.size() + params.getNumForecasts();
        rengine.eval(FORECAST_MODEL + " <- forecast::forecast(" + NNETWORK + ", " + numForecasts + ")");
        //skoro ma svihlo, kym som na to prisla, ale:
        //1. vo "forecastedModel" je strasne vela heterogennych informacii, neda sa to len tak poslat cele Jave
        //2. takze ked chcem len tie forecastedValues, ziskam ich ako "forecastedModel$mean[1:8]", kde 8 je ich pocet...
        rengine.eval(FORECAST_VALS + " <- " + FORECAST_MODEL + "$mean[1:" + numForecasts + "]");
        REXP getForecastValsAll = rengine.eval(FORECAST_VALS);
        double[] forecastAll = getForecastValsAll.asDoubleArray();
        List<Double> allForecastsList = Utils.arrayToList(forecastAll);
        List<Double> forecastTest = allForecastsList.subList(0, testingPortionOfData.size());
        report.setForecastValuesTest(Utils.listToArray(forecastTest));
        report.setForecastValuesFuture(Utils.listToArray(allForecastsList.subList(testingPortionOfData.size(), allForecastsList.size())));
        
        rengine.assign(TEST, Utils.listToArray(testingPortionOfData));
        //TODO mozno iba accuracy(model) miesto accuracy(model, testingData)? zistit!!!
        REXP getAcc = rengine.eval("forecast::accuracy(" + FORECAST_MODEL + ", " + TEST + ")[1:12]");//TODO [1:12] preto, ze v novej verzii
        // tam pribudla aj ACF a niekedy robi problemy
//        double[] acc = getAcc.asDoubleArray(); //pozor na poradie vysledkov, ochenta setenta...
        //vrati vysledky po stlpcoch, tj. ME train, ME test, RMSE train, RMSE test, MAE, MPE, MAPE, MASE
        //nova verzia vracia aj ACF1
        
        rengine.eval(FIT + " <- fitted.values(" + NNETWORK + ")");
        REXP getFittedVals = rengine.eval(FIT);
        double[] fitted = getFittedVals.asDoubleArray();
        report.setFittedValues(fitted);
        
        ErrorMeasuresCrisp errorMeasures = ErrorMeasuresUtils.computeAllErrorMeasuresCrisp(Utils.arrayToList(trainingOutputs), 
                Utils.arrayToList(testingOutputs), Utils.arrayToList(fitted), forecastTest, parameters.getSeasonality());
        report.setErrorMeasures(errorMeasures);
        
        
        
        report.setPlotCode("plot.ts(c(" + FIT + "," + FORECAST_VALS + "))");
        
        //TODO neskor vybrat najlepsi a ten naplotovat! zatial plotuje prvy :/
        report.setNnDiagramPlotCode("plot.nnet(" + NNETWORK + "$model[[1]]$wts, struct = " + NNETWORK + "$model[[1]]$n)");
        
        return report;
    }
    
    @Override
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
