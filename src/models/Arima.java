package models;

import java.util.Arrays;
import java.util.List;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;
import params.ArimaParams;
import params.NnetarParams;
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
        
        report.setForecastValues(fitted); //TODO
        
        report.setFittedValuesPlotCode("plot.ts(" + FIT + ")");
        
        ErrorMeasuresCrisp errorMeasures = new ErrorMeasuresCrisp();
        errorMeasures.setMEtrain(ErrorMeasuresUtils.ME(trainingPortionOfData, Utils.arrayToList(fitted)));
        errorMeasures.setMEtest(0.0);
        errorMeasures.setRMSEtrain(ErrorMeasuresUtils.RMSE(trainingPortionOfData, Utils.arrayToList(fitted)));
        errorMeasures.setRMSEtest(0.0);
        errorMeasures.setMAEtrain(ErrorMeasuresUtils.MAE(trainingPortionOfData, Utils.arrayToList(fitted)));
        errorMeasures.setMAEtest(0.0);
        errorMeasures.setMPEtrain(ErrorMeasuresUtils.MPE(trainingPortionOfData, Utils.arrayToList(fitted)));
        errorMeasures.setMPEtest(0.0);
        errorMeasures.setMAPEtrain(ErrorMeasuresUtils.MAPE(trainingPortionOfData, Utils.arrayToList(fitted)));
        errorMeasures.setMAPEtest(0.0);
        errorMeasures.setMASEtrain(ErrorMeasuresUtils.MASE(trainingPortionOfData, Utils.arrayToList(fitted)));
        errorMeasures.setMASEtest(0.0);
        errorMeasures.setMSEtrain(ErrorMeasuresUtils.MSE(trainingPortionOfData, Utils.arrayToList(fitted)));
        errorMeasures.setMSEtest(0.0);
        errorMeasures.setTheilUtrain(ErrorMeasuresUtils.theilsU(trainingPortionOfData, Utils.arrayToList(fitted)));
        errorMeasures.setTheilUtest(0.0);
        report.setErrorMeasures(errorMeasures);
        
        
        return report;
    }

    @Override
    public String getOptionalParams(Params parameters) {
        return "";
    }
    
}
