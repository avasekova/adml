package models;

import java.util.Arrays;
import java.util.List;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;
import params.ArimaParams;
import params.Params;
import utils.Const;
import utils.ErrorMeasuresCrisp;
import utils.ErrorMeasuresUtils;
import utils.MyRengine;
import utils.Utils;

public class Arima implements Forecastable {

    @Override
    public TrainAndTestReport forecast(List<Double> allData, Params parameters) {
        final String TRAINDATA = Const.TRAINDATA + Utils.getCounter();
        final String MODEL = Const.MODEL + Utils.getCounter();
        final String TEST = Const.TEST + Utils.getCounter();
        final String FIT = Const.FIT + Utils.getCounter();
        final String FORECAST_TEST = Const.FORECAST_VALS + Utils.getCounter();
        final String FORECAST_TEST_VALS = FORECAST_TEST + ".vals";
        
        ArimaParams params = (ArimaParams) parameters;
        TrainAndTestReportCrisp report = new TrainAndTestReportCrisp("ARIMA");

        Rengine rengine = MyRengine.getRengine();
        int numTrainingEntries = Math.round(((float) params.getPercentTrain()/100)*allData.size());
        report.setNumTrainingEntries(numTrainingEntries);
        List<Double> trainingPortionOfData = allData.subList(0, numTrainingEntries);
        List<Double> testingPortionOfData = allData.subList(numTrainingEntries, allData.size());
        
        rengine.assign(TRAINDATA, Utils.listToArray(trainingPortionOfData));
        rengine.eval(MODEL + " <- arima(" + TRAINDATA + ", order = c(" + params.getNonSeasP() + ", "
                                                                       + params.getNonSeasD() + ", "
                                                                       + params.getNonSeasQ()
                                     + "), seasonal = list(order = c(" + params.getSeasP() + ", "
                                                                       + params.getSeasD() + ", "
                                                                       + params.getNonSeasQ() + "), period = NA))");
        
//        REXP getResiduals = rengine.eval(MODEL + "$residuals");
//        double[] residuals = getResiduals.asDoubleArray();
        
        rengine.eval(FIT + " <- " + TRAINDATA + " - " + MODEL + "$residuals");
        REXP getFittedValues = rengine.eval(FIT);
        double[] fitted = getFittedValues.asDoubleArray();
        report.setFittedValues(fitted);
        
        //"forecast" testing data
        rengine.eval("require(forecast)");
        rengine.eval(FORECAST_TEST + " <- predict(" + MODEL + ", " + testingPortionOfData.size() + ")");
        rengine.eval(FORECAST_TEST_VALS + " <- " + FORECAST_TEST + "$pred[1:" + testingPortionOfData.size() + "]");
        REXP getForecastsTest = rengine.eval(FORECAST_TEST_VALS);
        double[] forecastsTest = getForecastsTest.asDoubleArray();
        report.setForecastValues(forecastsTest);
        
        //TODO pridat aj pocet forecastov do buducnosti
        report.setFittedValuesPlotCode("plot.ts(" + FIT + ")");
        
        ErrorMeasuresCrisp errorMeasures = new ErrorMeasuresCrisp();
        errorMeasures.setMEtrain(ErrorMeasuresUtils.ME(trainingPortionOfData, Utils.arrayToList(fitted)));
        errorMeasures.setMEtest(ErrorMeasuresUtils.ME(testingPortionOfData, Utils.arrayToList(forecastsTest)));
        errorMeasures.setRMSEtrain(ErrorMeasuresUtils.RMSE(trainingPortionOfData, Utils.arrayToList(fitted)));
        errorMeasures.setRMSEtest(ErrorMeasuresUtils.RMSE(testingPortionOfData, Utils.arrayToList(forecastsTest)));
        errorMeasures.setMAEtrain(ErrorMeasuresUtils.MAE(trainingPortionOfData, Utils.arrayToList(fitted)));
        errorMeasures.setMAEtest(ErrorMeasuresUtils.MAE(testingPortionOfData, Utils.arrayToList(forecastsTest)));
        errorMeasures.setMPEtrain(ErrorMeasuresUtils.MPE(trainingPortionOfData, Utils.arrayToList(fitted)));
        errorMeasures.setMPEtest(ErrorMeasuresUtils.MPE(testingPortionOfData, Utils.arrayToList(forecastsTest)));
        errorMeasures.setMAPEtrain(ErrorMeasuresUtils.MAPE(trainingPortionOfData, Utils.arrayToList(fitted)));
        errorMeasures.setMAPEtest(ErrorMeasuresUtils.MAPE(testingPortionOfData, Utils.arrayToList(forecastsTest)));
        errorMeasures.setMASEtrain(ErrorMeasuresUtils.MASE(trainingPortionOfData, Utils.arrayToList(fitted)));
        errorMeasures.setMASEtest(ErrorMeasuresUtils.MASE(testingPortionOfData, Utils.arrayToList(forecastsTest)));
        errorMeasures.setMSEtrain(ErrorMeasuresUtils.MSE(trainingPortionOfData, Utils.arrayToList(fitted)));
        errorMeasures.setMSEtest(ErrorMeasuresUtils.MSE(testingPortionOfData, Utils.arrayToList(forecastsTest)));
        errorMeasures.setTheilUtrain(ErrorMeasuresUtils.theilsU(trainingPortionOfData, Utils.arrayToList(fitted)));
        errorMeasures.setTheilUtest(ErrorMeasuresUtils.theilsU(testingPortionOfData, Utils.arrayToList(forecastsTest)));
        report.setErrorMeasures(errorMeasures);
        
        
        return report;
    }

    @Override
    public String getOptionalParams(Params parameters) {
        return "";
    }
    
}
