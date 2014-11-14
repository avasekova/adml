package models;

import gui.DataTableModel;
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
    public TrainAndTestReport forecast(DataTableModel dataTableModel, Params parameters) {
        final String INPUT = Const.INPUT + Utils.getCounter();
        final String SCALED_INPUT = "scaled." + INPUT;
        final String INPUT_TRAIN = Const.INPUT + Utils.getCounter();
        final String SCALED_INPUT_TRAIN = "scaled." + INPUT_TRAIN;
        final String INPUT_TEST = Const.INPUT + Utils.getCounter();
        final String SCALED_INPUT_TEST = "scaled." + INPUT_TEST;
        final String FORECAST_VALS = Const.FORECAST_VALS + Utils.getCounter();
        final String UNSCALED_FORECAST_VALS = "unscaled." + FORECAST_VALS;
        final String FITTED_VALS = Const.FIT + Utils.getCounter();
        final String UNSCALED_FITTED_VALS = "unscaled." + FITTED_VALS;
        
        final String MODEL = Const.MODEL + Utils.getCounter();
        
        ArimaParams params = (ArimaParams) parameters;
        String arimaDescription;
        if (params.isOptimize()) {
            arimaDescription = "(optimized)";
        } else {
            arimaDescription = "(" + params.getNonSeasPotato() + "," + 
                params.getNonSeasDonkey() + "," + params.getNonSeasQuark() + ")(" + params.getSeasPotato() + "," +
                params.getSeasDonkey() + "," + params.getSeasQuark() + ")";
            if (params.isWithConstant()) {
                arimaDescription += ",const.";
            }
        }
        
        List<Double> allData = dataTableModel.getDataForColname(params.getColName());
        List<Double> dataToUse = allData.subList((params.getDataRangeFrom() - 1), params.getDataRangeTo());

        Rengine rengine = MyRengine.getRengine();
        rengine.eval("require(forecast)");
        
        rengine.assign(INPUT, Utils.listToArray(dataToUse));
        rengine.eval(SCALED_INPUT + " <- MLPtoR.scale(" + INPUT + ")");
        
        int numTrainingEntries = Math.round(((float) params.getPercentTrain()/100)*dataToUse.size());
        
        rengine.eval(INPUT_TRAIN +        " <- " +        INPUT + "[1:" + numTrainingEntries + "]");
        rengine.eval(SCALED_INPUT_TRAIN + " <- " + SCALED_INPUT + "[1:" + numTrainingEntries + "]");
        rengine.eval(INPUT_TEST +         " <- " +        INPUT + "[" + (numTrainingEntries+1) + ":" + dataToUse.size() + "]");
        rengine.eval(SCALED_INPUT_TEST +  " <- " + SCALED_INPUT + "[" + (numTrainingEntries+1) + ":" + dataToUse.size() + "]");
        
        if (params.isOptimize()) {
            rengine.eval(MODEL + " <- forecast::auto.arima(" + SCALED_INPUT_TRAIN + ")");
        } else {
            rengine.eval(MODEL + " <- forecast::Arima(" + SCALED_INPUT_TRAIN + ", order = c(" + params.getNonSeasPotato() + ", "
                                                                       + params.getNonSeasDonkey() + ", "
                                                                       + params.getNonSeasQuark()
                                     + "), seasonal = list(order=c(" + params.getSeasPotato() + ", "
                                                                       + params.getSeasDonkey() + ", "
                                                                       + params.getNonSeasQuark()
                                     + "), period=NA), include.constant = " + Utils.booleanToRBool(params.isWithConstant()) + ", "
                                     + "method=\"ML\")");
        }
        
        rengine.eval(FITTED_VALS + " <- fitted.values(" + MODEL + ")");
        rengine.eval(UNSCALED_FITTED_VALS + " <- MLPtoR.unscale(" + FITTED_VALS + ", " + INPUT + ")");
        REXP getFittedValues = rengine.eval(UNSCALED_FITTED_VALS);
        if (getFittedValues == null) {
            return null;
        }
        double[] fitted = getFittedValues.asDoubleArray();
        
        //"forecast" testing data
        int numForecasts = dataToUse.size() - numTrainingEntries + params.getNumForecasts();
        rengine.eval(FORECAST_VALS + " <- forecast::forecast(" + MODEL + ", h = " + numForecasts + ")"); //predict all
                                        
        //vziat vsetky forecasted vals (cast je z test data, cast je z future)
        rengine.eval(FORECAST_VALS + " <- " + FORECAST_VALS + "$mean[1:" + numForecasts + "]");
        rengine.eval(UNSCALED_FORECAST_VALS + " <- MLPtoR.unscale(" + FORECAST_VALS + ", " + INPUT + ")");
        REXP getAllForecasts = rengine.eval(UNSCALED_FORECAST_VALS);
        double[] allForecasts = getAllForecasts.asDoubleArray();
        //TODO avoid this conversion array<->list whenever possible - moze to byt spomalovak pre velke mnozstva dat
        List<Double> allForecastsList = Utils.arrayToList(allForecasts);
        
        //error measures pocitat len z testu, z buducich sa neda
        REXP getTrainingPortionOfData = rengine.eval(INPUT_TRAIN);
        double[] trainingPortionOfDataArray = getTrainingPortionOfData.asDoubleArray();
        List<Double> trainingPortionOfData = Utils.arrayToList(trainingPortionOfDataArray);
        REXP getTestingPortionOfData = rengine.eval(INPUT_TEST);
        double[] testingPortionOfDataArray = getTestingPortionOfData.asDoubleArray();
        List<Double> testingPortionOfData = Utils.arrayToList(testingPortionOfDataArray);
        
        double[] forecastsTest = Arrays.copyOf(allForecasts, testingPortionOfData.size());
        ErrorMeasuresCrisp errorMeasures = ErrorMeasuresUtils.computeAllErrorMeasuresCrisp(trainingPortionOfData, 
                testingPortionOfData, Utils.arrayToList(fitted), Utils.arrayToList(forecastsTest), params.getSeasonality());
        
        TrainAndTestReportCrisp report = new TrainAndTestReportCrisp("ARIMA");
        report.setModelDescription(arimaDescription);
        report.setNumTrainingEntries(numTrainingEntries);
        report.setFittedValues(fitted);
        
        report.setRealOutputsTrain(trainingPortionOfDataArray);
        report.setRealOutputsTest(testingPortionOfDataArray);
        
        report.setForecastValuesTest(Utils.listToArray(allForecastsList.subList(0, dataToUse.size() - numTrainingEntries)));
        report.setForecastValuesFuture(Utils.listToArray(allForecastsList.subList(dataToUse.size() - numTrainingEntries, allForecastsList.size())));
        
        report.setPlotCode("plot.ts(c(" + UNSCALED_FITTED_VALS + ", " + UNSCALED_FORECAST_VALS + "))");
        
        report.setErrorMeasures(errorMeasures);
        
        
        return report;
    }

    @Override
    public String getOptionalParams(Params parameters) {
        return "";
    }
    
}
