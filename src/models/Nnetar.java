package models;

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
    public TrainAndTestReport forecast(List<Double> allData, Params parameters){
        final String TRAINDATA = Const.TRAINDATA + Utils.getCounter();
        final String NNETWORK = Const.NNETWORK + Utils.getCounter();
        final String FORECAST_MODEL = Const.FORECAST_MODEL + Utils.getCounter();
        final String TEST = Const.TEST + Utils.getCounter();
        final String FIT = Const.FIT + Utils.getCounter();
        final String FORECAST_VALS = Const.FORECAST_VALS + Utils.getCounter();
        
        NnetarParams params = (NnetarParams) parameters;
        TrainAndTestReportCrisp report = new TrainAndTestReportCrisp("nnetar");
        List<Double> dataToUse = allData.subList((params.getDataRangeFrom() - 1), params.getDataRangeTo());
        
        Rengine rengine = MyRengine.getRengine();
        rengine.eval("require(forecast)");
        int numTrainingEntries = Math.round(((float) params.getPercentTrain()/100)*dataToUse.size());
        report.setNumTrainingEntries(numTrainingEntries);
        List<Double> trainingPortionOfData = dataToUse.subList(0, numTrainingEntries);
        List<Double> testingPortionOfData = dataToUse.subList(numTrainingEntries, dataToUse.size());
        
        rengine.assign(TRAINDATA, Utils.listToArray(trainingPortionOfData));
        String optionalParams = getOptionalParams(params);
        rengine.eval(NNETWORK + " <- forecast::nnetar(" + TRAINDATA + optionalParams + ")");
        
        int numForecasts = testingPortionOfData.size() + params.getNumForecasts();
        rengine.eval(FORECAST_MODEL + " <- forecast::forecast(" + NNETWORK + ", " + numForecasts + ")");
        //skoro ma svihlo, kym som na to prisla, ale:
        //1. vo "forecastedModel" je strasne vela heterogennych informacii, neda sa to len tak poslat cele Jave
        //2. takze ked chcem len tie forecastedValues, ziskam ich ako "forecastedModel$mean[1:8]", kde 8 je ich pocet...
        rengine.eval(FORECAST_VALS + " <- " + FORECAST_MODEL + "$mean[1:" + numForecasts + "]");
        REXP getForecastVals = rengine.eval(FORECAST_VALS);
        double[] forecast = getForecastVals.asDoubleArray();
        report.setForecastValues(forecast);
        
        rengine.assign(TEST, Utils.listToArray(testingPortionOfData));
        //TODO mozno iba accuracy(model) miesto accuracy(model, testingData)? zistit!!!
        REXP getAcc = rengine.eval("forecast::accuracy(" + FORECAST_MODEL + ", " + TEST + ")[1:12]");//TODO [1:12] preto, ze v novej verzii
        // tam pribudla aj ACF a niekedy robi problemy
        double[] acc = getAcc.asDoubleArray(); //pozor na poradie vysledkov, ochenta setenta...
        //vrati vysledky po stlpcoch, tj. ME train, ME test, RMSE train, RMSE test, MAE, MPE, MAPE, MASE
        //nova verzia vracia aj ACF1
        
        rengine.eval(FIT + " <- fitted.values(" + NNETWORK + ")");
        REXP getFittedVals = rengine.eval(FIT);
        double[] fitted = getFittedVals.asDoubleArray();
        report.setFittedValues(fitted);
        
        ErrorMeasuresCrisp errorMeasures = new ErrorMeasuresCrisp();
        errorMeasures.setMEtrain(acc[0]);
        errorMeasures.setMEtest(acc[1]);
        errorMeasures.setRMSEtrain(acc[2]);
        errorMeasures.setRMSEtest(acc[3]);
        errorMeasures.setMAEtrain(acc[4]);
        errorMeasures.setMAEtest(acc[5]);
        errorMeasures.setMPEtrain(acc[6]);
        errorMeasures.setMPEtest(acc[7]);
        errorMeasures.setMAPEtrain(acc[8]);
        errorMeasures.setMAPEtest(acc[9]);
        errorMeasures.setMASEtrain(acc[10]);
        errorMeasures.setMASEtest(acc[11]);
        errorMeasures.setMSEtrain(ErrorMeasuresUtils.MSE(trainingPortionOfData, Utils.arrayToList(fitted)));
        errorMeasures.setMSEtest(ErrorMeasuresUtils.MSE(testingPortionOfData, Utils.arrayToList(forecast)));
        errorMeasures.setTheilUtrain(ErrorMeasuresUtils.theilsU(Collections.unmodifiableList(trainingPortionOfData),
                Collections.unmodifiableList(Utils.arrayToList(fitted))));
        errorMeasures.setTheilUtest(ErrorMeasuresUtils.theilsU(Collections.unmodifiableList(testingPortionOfData),
                Collections.unmodifiableList(Utils.arrayToList(forecast))));
        
        report.setErrorMeasures(errorMeasures);
        
        
        
        //report.setForecastPlotCode("plot(" + FORECAST_MODEL + ")"); //vykresli aj tie modre forecasty
        report.setPlotCode("plot.ts(c(" + FIT + "," + FORECAST_VALS + "))"); //vykresli iba fitted values
        
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
