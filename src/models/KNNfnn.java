package models;

import java.util.List;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;
import params.KNNfnnParams;
import params.Params;
import utils.Const;
import utils.ErrorMeasuresCrisp;
import utils.ErrorMeasuresUtils;
import utils.MyRengine;
import utils.Utils;

public class KNNfnn implements Forecastable {

    @Override
    public TrainAndTestReport forecast(List<Double> allData, Params parameters) {
        final String NBRS_NO_TEST = Const.NEIGHBOURS + Utils.getCounter();
        final String NBRS_WITH_TEST = Const.NEIGHBOURS + Utils.getCounter();
        
        final String INPUT_TRAIN = Const.INPUT + Utils.getCounter();
        final String SCALED_INPUT_TRAIN = "scaled." + INPUT_TRAIN;
        
        final String INPUT_TEST = Const.INPUT + Utils.getCounter();
        final String SCALED_INPUT_TEST = "scaled." + INPUT_TEST;
        
        final String OUTPUT_TRAIN = Const.OUTPUT + Utils.getCounter();
        final String SCALED_OUTPUT_TRAIN = "scaled." + OUTPUT_TRAIN;
        final String UNSCALED_PREDICTED_TRAIN = "predicted." + OUTPUT_TRAIN;
        final String RESIDUALS_TRAIN = "residuals." + OUTPUT_TRAIN;
        
        final String OUTPUT_TEST = Const.OUTPUT + Utils.getCounter();
        final String SCALED_OUTPUT_TEST = "scaled." + OUTPUT_TEST;
        final String UNSCALED_PREDICTED_TEST = "predicted." + OUTPUT_TEST;
        final String RESIDUALS_TEST = "residuals." + OUTPUT_TEST;
        
        final String INPUT = Const.INPUT + Utils.getCounter();
        final String OUTPUT = Const.OUTPUT + Utils.getCounter();
        final String SCALED_INPUT = "scaled." + INPUT;
        final String SCALED_OUTPUT = "scaled." + OUTPUT;
        
        KNNfnnParams params = (KNNfnnParams) parameters;
        TrainAndTestReportCrisp report = new TrainAndTestReportCrisp("kNN (FNN)");
        allData = allData.subList((params.getDataRangeFrom() - 1), params.getDataRangeTo());

        Rengine rengine = MyRengine.getRengine();
        rengine.eval("require(FNN)");
        
        //vyrobit dva subory dat (vstupy, vystupy), lagnuty o prislusny lag (rovnaka dlzka)
        rengine.assign(INPUT, Utils.listToArray(allData));
        rengine.assign(OUTPUT, Utils.listToArray(allData));
        rengine.eval(INPUT + " <- " + INPUT + "[1:(length(" + INPUT + ") - " + params.getLag() + ")]"); //1:(length-lag)
        rengine.eval(OUTPUT + " <- " + OUTPUT + "[(1 + " + params.getLag() + "):length(" + OUTPUT + ")]"); //(1+lag):length
        
        rengine.eval(SCALED_INPUT + " <- MLPtoR.scale(" + INPUT + ")");
        rengine.eval(SCALED_OUTPUT + " <- MLPtoR.scale(" + OUTPUT + ")");
        
        //potom z dlzky tychto suborov vypocitat numTrainingEntries podla percentTrain
        int lengthInputOutput = allData.size() - params.getLag();
        int numTrainingEntries = Math.round(((float) params.getPercentTrain()/100)*lengthInputOutput);
        report.setNumTrainingEntries(numTrainingEntries);
        
        //potom si vyrobit trainingValues, testingValues
        rengine.eval(INPUT_TRAIN +        " <- " +        INPUT + "[1:" + numTrainingEntries + "]");
        rengine.eval(SCALED_INPUT_TRAIN + " <- " + SCALED_INPUT + "[1:" + numTrainingEntries + "]");
        rengine.eval(INPUT_TEST +        " <- " +        INPUT + "[" + (numTrainingEntries+1) + ":length(" +        INPUT + ")]");
        rengine.eval(SCALED_INPUT_TEST + " <- " + SCALED_INPUT + "[" + (numTrainingEntries+1) + ":length(" + SCALED_INPUT + ")]");
        
        rengine.eval(OUTPUT_TRAIN +        " <- " +        OUTPUT + "[1:" + numTrainingEntries + "]");
        rengine.eval(SCALED_OUTPUT_TRAIN + " <- " + SCALED_OUTPUT + "[1:" + numTrainingEntries + "]");
        
        rengine.eval(OUTPUT_TEST +        " <- " +        OUTPUT + "[" + (numTrainingEntries+1) + ":length(" +        OUTPUT + ")]");
        rengine.eval(SCALED_OUTPUT_TEST + " <- " + SCALED_OUTPUT + "[" + (numTrainingEntries+1) + ":length(" + SCALED_OUTPUT + ")]");
        
        //first run it without testing data - will give residuals for training data
        rengine.eval(NBRS_NO_TEST + " <- FNN::knn.reg(" + SCALED_INPUT_TRAIN + ", y = " + SCALED_OUTPUT_TRAIN
                                                    + ", k = " + params.getNumNeighbours() + ")");
        
        //the predicted values are in NBRS_NO_TEST$pred, but they need to be scaled back using OUTPUT_TRAIN scale
        rengine.eval(UNSCALED_PREDICTED_TRAIN + " <- MLPtoR.unscale(" + NBRS_NO_TEST + "$pred, " + OUTPUT + ")");
        REXP getPredictedValsNoTest = rengine.eval(UNSCALED_PREDICTED_TRAIN);
        double[] predictedTrain = getPredictedValsNoTest.asDoubleArray();
        
        //compute residuals for training data manually - the ones in NBRS$res are scaled
        rengine.eval(RESIDUALS_TRAIN + " <- " + OUTPUT_TRAIN + " - " + UNSCALED_PREDICTED_TRAIN);
        REXP getResidualsNoTest = rengine.eval(RESIDUALS_TRAIN);
        double[] residualsTrain = getResidualsNoTest.asDoubleArray();
        
        //then run it with testing data (TODO and later with forecasts)
        rengine.eval(NBRS_WITH_TEST + " <- FNN::knn.reg(train = " + SCALED_INPUT_TRAIN + ", test = data.frame(" + SCALED_INPUT_TEST
                                                   + "), y = " + SCALED_OUTPUT_TRAIN + ", k = " + params.getNumNeighbours() + ")");
        //the predicted values for test data are in NBRS_NO_TEST$pred, but they need to be scaled back using OUTPUT_TEST scale
        rengine.eval(UNSCALED_PREDICTED_TEST + " <- MLPtoR.unscale(" + NBRS_WITH_TEST + "$pred, " + OUTPUT + ")");
        REXP getPredictedValsWithTest = rengine.eval(UNSCALED_PREDICTED_TEST);
        double[] predictedTest = getPredictedValsWithTest.asDoubleArray();
        
        //compute residuals for testing data manually - the ones in NBRS$res are scaled
        rengine.eval(RESIDUALS_TEST + " <- " + OUTPUT_TEST + " - " + UNSCALED_PREDICTED_TEST);
        REXP getResidualsWithTest = rengine.eval(RESIDUALS_TEST);
        double[] residualsTest = getResidualsWithTest.asDoubleArray();
        
        REXP getTrainingOutputs = rengine.eval(OUTPUT_TRAIN);
        double[] trainingOutputs = getTrainingOutputs.asDoubleArray();
        
        REXP getTestingOutputs = rengine.eval(OUTPUT_TEST);
        double[] testingOutputs = getTestingOutputs.asDoubleArray();
        
        //then compute ErrorMeasures
        //TODO check the error measures, pretoze si myslim, ze to napriklad nepocita s tym posunom kvoli lagu. a potom
        // napriklad pre test data mam len par pozorovani, ale priemerne errory delim celkovym poctom testov, a vychadzaju
        // mi velmi male errory. bud je chybny graf alebo pocitanie errorov, pretoze na grafe su vyssie hodnoty
        ErrorMeasuresCrisp errorMeasures = new ErrorMeasuresCrisp();
        errorMeasures.setMAEtrain(ErrorMeasuresUtils.MAE(Utils.arrayToList(residualsTrain)));
        errorMeasures.setMAEtest(ErrorMeasuresUtils.MAE(Utils.arrayToList(testingOutputs), Utils.arrayToList(predictedTest)));
        errorMeasures.setMAPEtrain(ErrorMeasuresUtils.MAPE(Utils.arrayToList(trainingOutputs), Utils.arrayToList(predictedTrain)));
        errorMeasures.setMAPEtest(ErrorMeasuresUtils.MAPE(Utils.arrayToList(testingOutputs), Utils.arrayToList(predictedTest)));
        errorMeasures.setMASEtrain(ErrorMeasuresUtils.MASE(Utils.arrayToList(trainingOutputs), Utils.arrayToList(predictedTrain)));
        errorMeasures.setMASEtest(ErrorMeasuresUtils.MASE(Utils.arrayToList(testingOutputs), Utils.arrayToList(predictedTest)));
        errorMeasures.setMEtrain(ErrorMeasuresUtils.ME(Utils.arrayToList(residualsTrain)));
        errorMeasures.setMEtest(ErrorMeasuresUtils.ME(Utils.arrayToList(residualsTest)));
        errorMeasures.setMPEtrain(ErrorMeasuresUtils.MPE(Utils.arrayToList(trainingOutputs), Utils.arrayToList(predictedTrain)));
        errorMeasures.setMPEtest(ErrorMeasuresUtils.MPE(Utils.arrayToList(testingOutputs), Utils.arrayToList(predictedTest)));
        errorMeasures.setMSEtrain(ErrorMeasuresUtils.MSE(Utils.arrayToList(residualsTrain)));
        errorMeasures.setMSEtest(ErrorMeasuresUtils.MSE(Utils.arrayToList(residualsTest)));
        errorMeasures.setRMSEtrain(ErrorMeasuresUtils.RMSE(Utils.arrayToList(residualsTrain)));
        errorMeasures.setRMSEtest(ErrorMeasuresUtils.RMSE(Utils.arrayToList(residualsTest)));
        errorMeasures.setTheilUtrain(ErrorMeasuresUtils.theilsU(Utils.arrayToList(trainingOutputs), Utils.arrayToList(predictedTrain)));
        errorMeasures.setTheilUtest(ErrorMeasuresUtils.theilsU(Utils.arrayToList(testingOutputs), Utils.arrayToList(predictedTest)));
        
        report.setErrorMeasures(errorMeasures);
        
        //then report.setEverything
        double[] fitted = new double[predictedTrain.length + predictedTest.length];
        System.arraycopy(predictedTrain, 0, fitted, 0, predictedTrain.length);
        System.arraycopy(predictedTest, 0, fitted, predictedTrain.length, predictedTest.length);
        report.setFittedValues(fitted);
        
        report.setForecastValues(fitted); //iba zatial, aby kreslilo plot TODO pridat forecasty
        
        report.setFittedValuesPlotCode("plot.ts(c(rep(NA, " + params.getLag() + "), " + UNSCALED_PREDICTED_TRAIN + ", " + UNSCALED_PREDICTED_TEST + "))");
        
        return report;
    }

    @Override
    public String getOptionalParams(Params parameters) {
        return "";
    }
}
