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
        
        KNNfnnParams params = (KNNfnnParams) parameters;
        TrainAndTestReportCrisp report = new TrainAndTestReportCrisp("kNN");

        Rengine rengine = MyRengine.getRengine();
        rengine.eval("require(FNN)");
        
        int numTrainingEntries = Math.round(((float) params.getPercentTrain()/100)*allData.size());
        report.setNumTrainingEntries(numTrainingEntries);
        List<Double> trainingValues = allData.subList(0, numTrainingEntries);
        List<Double> testingValues = allData.subList(numTrainingEntries, allData.size());
        
        //for now. TODO neskor doplnit explanatory variables!!!
        rengine.eval(INPUT_TRAIN + " <- seq(1, " + numTrainingEntries + ")");
        rengine.eval(SCALED_INPUT_TRAIN + " <- MLPtoR.scale(" + INPUT_TRAIN + ")");
        rengine.eval(INPUT_TEST + " <- seq(" + (numTrainingEntries + 1) + ", " + allData.size() + ")");
        rengine.eval(SCALED_INPUT_TEST + " <- MLPtoR.scale(" + INPUT_TEST + ")");
        
        rengine.assign(OUTPUT_TRAIN, Utils.listToArray(trainingValues));
        rengine.eval(SCALED_OUTPUT_TRAIN + " <- MLPtoR.scale(" + OUTPUT_TRAIN + ")");
        
        rengine.assign(OUTPUT_TEST, Utils.listToArray(testingValues));
        rengine.eval(SCALED_OUTPUT_TEST + " <- MLPtoR.scale(" + OUTPUT_TEST + ")");
        
        //first run it without testing data - will give residuals for training data
        rengine.eval(NBRS_NO_TEST + " <- knn.reg(" + SCALED_INPUT_TRAIN + ", y = " + SCALED_OUTPUT_TRAIN
                                                 + ", k = " + params.getNumNeighbours() + ")");
        
        //the predicted values are in NBRS_NO_TEST$pred, but they need to be scaled back using OUTPUT_TRAIN scale
        rengine.eval(UNSCALED_PREDICTED_TRAIN + " <- MLPtoR.unscale(" + NBRS_NO_TEST + "$pred, " + OUTPUT_TRAIN + ")");
        REXP getPredictedValsNoTest = rengine.eval(UNSCALED_PREDICTED_TRAIN);
        double[] predictedTrain = getPredictedValsNoTest.asDoubleArray();
        
        //compute residuals for training data manually - the ones in NBRS$res are scaled
        rengine.eval(RESIDUALS_TRAIN + " <- " + OUTPUT_TRAIN + " - " + UNSCALED_PREDICTED_TRAIN);
        REXP getResidualsNoTest = rengine.eval(RESIDUALS_TRAIN);
        double[] residualsTrain = getResidualsNoTest.asDoubleArray();
        
        //then run it with testing data (TODO and later with forecasts)
        rengine.eval(NBRS_WITH_TEST + " <- knn.reg(" + SCALED_INPUT_TRAIN + ", data.frame(" + SCALED_INPUT_TEST + "), "
                                                   + SCALED_OUTPUT_TRAIN + ", k = " + params.getNumNeighbours() + ")");
        //the predicted values for test data are in NBRS_NO_TEST$pred, but they need to be scaled back using OUTPUT_TEST scale
        rengine.eval(UNSCALED_PREDICTED_TEST + " <- MLPtoR.unscale(" + NBRS_WITH_TEST + "$pred, " + OUTPUT_TEST + ")");
        REXP getPredictedValsWithTest = rengine.eval(UNSCALED_PREDICTED_TEST);
        double[] predictedTest = getPredictedValsWithTest.asDoubleArray();
        
        //compute residuals for testing data manually - the ones in NBRS$res are scaled
        rengine.eval(RESIDUALS_TEST + " <- " + OUTPUT_TEST + " - " + UNSCALED_PREDICTED_TEST);
        REXP getResidualsWithTest = rengine.eval(RESIDUALS_TEST);
        double[] residualsTest = getResidualsWithTest.asDoubleArray();
        
        
        //then compute ErrorMeasures
        ErrorMeasuresCrisp errorMeasures = new ErrorMeasuresCrisp();
        errorMeasures.setMAEtrain(ErrorMeasuresUtils.MAE(Utils.arrayToList(residualsTrain)));
        errorMeasures.setMAEtest(ErrorMeasuresUtils.MAE(Utils.arrayToList(residualsTest)));
        errorMeasures.setMAPEtrain(ErrorMeasuresUtils.MAPE(trainingValues, Utils.arrayToList(predictedTrain)));
        errorMeasures.setMAPEtest(ErrorMeasuresUtils.MAPE(testingValues, Utils.arrayToList(predictedTest)));
        errorMeasures.setMASEtrain(ErrorMeasuresUtils.MASE(trainingValues, Utils.arrayToList(predictedTrain)));
        errorMeasures.setMASEtest(ErrorMeasuresUtils.MASE(testingValues, Utils.arrayToList(predictedTest)));
        errorMeasures.setMEtrain(ErrorMeasuresUtils.ME(Utils.arrayToList(residualsTrain)));
        errorMeasures.setMEtest(ErrorMeasuresUtils.ME(Utils.arrayToList(residualsTest)));
        errorMeasures.setMPEtrain(ErrorMeasuresUtils.MPE(trainingValues, Utils.arrayToList(predictedTrain)));
        errorMeasures.setMPEtest(ErrorMeasuresUtils.MPE(testingValues, Utils.arrayToList(predictedTest)));
        errorMeasures.setMSEtrain(ErrorMeasuresUtils.MSE(Utils.arrayToList(residualsTrain)));
        errorMeasures.setMSEtest(ErrorMeasuresUtils.MSE(Utils.arrayToList(residualsTest)));
        errorMeasures.setRMSEtrain(ErrorMeasuresUtils.RMSE(Utils.arrayToList(residualsTrain)));
        errorMeasures.setRMSEtest(ErrorMeasuresUtils.RMSE(Utils.arrayToList(residualsTest)));
        errorMeasures.setTheilUtrain(ErrorMeasuresUtils.theilsU(trainingValues, Utils.arrayToList(predictedTrain)));
        errorMeasures.setTheilUtest(ErrorMeasuresUtils.theilsU(testingValues, Utils.arrayToList(predictedTest)));
        
        report.setErrorMeasures(errorMeasures);
        
        //then report.setEverything
        double[] fitted = new double[predictedTrain.length + predictedTest.length];
        System.arraycopy(predictedTrain, 0, fitted, 0, predictedTrain.length);
        System.arraycopy(predictedTest, 0, fitted, predictedTrain.length, predictedTest.length);
        report.setFittedValues(fitted);
        
        report.setForecastValues(fitted); //iba zatial, aby kreslilo plot TODO pridat forecasty
        
        report.setFittedValuesPlotCode("plot.ts(c(" + UNSCALED_PREDICTED_TRAIN + ", " + UNSCALED_PREDICTED_TEST + "))");
        
        return report;
    }

    @Override
    public String getOptionalParams(Params parameters) {
        return "";
    }
}
