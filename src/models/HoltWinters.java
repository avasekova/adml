package models;

import gui.tablemodels.DataTableModel;
import java.util.List;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;
import params.HoltWintersParams;
import params.Params;
import utils.Const;
import utils.ErrorMeasuresCrisp;
import utils.ErrorMeasuresUtils;
import utils.MyRengine;
import utils.Utils;

public class HoltWinters implements Forecastable {

    @Override
    public TrainAndTestReport forecast(DataTableModel dataTableModel, Params parameters) {
        final String FORECAST_MODEL = Const.FORECAST_MODEL + Utils.getCounter();
        final String INPUT_TRAIN = Const.INPUT + Utils.getCounter();
        final String FIT = Const.FIT + Utils.getCounter();
        final String FORECAST = Const.FORECAST_VALS + Utils.getCounter();
        
        HoltWintersParams params = (HoltWintersParams) parameters;
        
        List<Double> allData = dataTableModel.getDataForColname(params.getColName());
        List<Double> dataToUse = allData.subList((params.getDataRangeFrom() - 1), params.getDataRangeTo());
        
        Rengine rengine = MyRengine.getRengine();
        rengine.eval("require(forecast)");
        int numTrainingEntries = Math.round(((float) params.getPercentTrain()/100)*dataToUse.size());
        
        List<Double> inputTrain = dataToUse.subList(0, numTrainingEntries);
        List<Double> inputTest = dataToUse.subList(numTrainingEntries, dataToUse.size());
        
        rengine.assign(INPUT_TRAIN, Utils.listToArray(inputTrain));
        
        //musim si z toho spravit seasonal data
        rengine.eval(INPUT_TRAIN + " <- ts(" + INPUT_TRAIN + ", " + params.getFrequency() + ")");
        
        int num4castsTestAndFuture = inputTest.size() + params.getNumForecasts();
        rengine.eval(FORECAST_MODEL + " <- forecast::hw(" + INPUT_TRAIN + ", h=" + num4castsTestAndFuture + 
                ", alpha=" + params.getAlpha() + ", beta=" + params.getBeta() + ", gamma=" + params.getGamma()
                + ", seasonal=\"" + params.getSeasonalityAddMult() + "\""
                + ", damped=" + params.getDamped() + ")");
        
        //ak to nie su seasonal data, R skonci s chybou
        if (rengine.eval(FORECAST_MODEL) == null) {
            TrainAndTestReportCrisp report = new TrainAndTestReportCrisp(Const.HOLT_WINTERS);
            report.setModelDescription("(non-seasonal data!)");
            report.setRealOutputsTrain(Utils.listToArray(inputTrain));
            report.setRealOutputsTest(Utils.listToArray(inputTest));
            report.setPlotCode("plot.ts(c())"); //dummy
            report.setErrorMeasures(new ErrorMeasuresCrisp());
        
            return report;
        }
        
        rengine.eval(FIT + " <- fitted(" + FORECAST_MODEL + ")[1:" + inputTrain.size() + "]");
        REXP getFittedVals = rengine.eval(FIT);
        double[] fittedVals = getFittedVals.asDoubleArray();
        
        rengine.eval(FORECAST + " <- data.frame(" + FORECAST_MODEL + ")[\"Point.Forecast\"][1:" + num4castsTestAndFuture + ",]"); //if somebody renames this field in the next version, well...
        REXP getForecastTestAndFuture = rengine.eval(FORECAST);
        List<Double> forecastTestAndFuture = Utils.arrayToList(getForecastTestAndFuture.asDoubleArray());
        List<Double> forecastTest = forecastTestAndFuture.subList(0, inputTest.size());
        List<Double> forecastFuture = forecastTestAndFuture.subList(inputTest.size(), forecastTestAndFuture.size());
        
        REXP getFinalAlpha = rengine.eval(FORECAST_MODEL + "$model$par[\"alpha\"]");
        double finalAlpha = getFinalAlpha.asDoubleArray()[0];
        REXP getFinalBeta = rengine.eval(FORECAST_MODEL + "$model$par[\"beta\"]");
        double finalBeta = getFinalBeta.asDoubleArray()[0];
        REXP getFinalGamma = rengine.eval(FORECAST_MODEL + "$model$par[\"gamma\"]");
        double finalGamma = getFinalGamma.asDoubleArray()[0];
        
        TrainAndTestReportCrisp report = new TrainAndTestReportCrisp(Const.HOLT_WINTERS);
        report.setModelDescription("(alpha=" + Utils.valToDecPoints(finalAlpha) + ",beta=" + Utils.valToDecPoints(finalBeta)
                + ",gamma=" + Utils.valToDecPoints(finalGamma) + ")");
        
        report.setNumTrainingEntries(numTrainingEntries);
        report.setFittedValues(fittedVals);
        report.setForecastValuesTest(Utils.listToArray(forecastTest));
        report.setForecastValuesFuture(Utils.listToArray(forecastFuture));
        report.setPlotCode("plot.ts(c(" + FIT + "," + FORECAST + "))");
        report.setRealOutputsTrain(Utils.listToArray(inputTrain));
        report.setRealOutputsTest(Utils.listToArray(inputTest));
        
        ErrorMeasuresCrisp errorMeasures = ErrorMeasuresUtils.computeAllErrorMeasuresCrisp(inputTrain, inputTest, 
                Utils.arrayToList(fittedVals), forecastTest, params.getSeasonality());
        report.setErrorMeasures(errorMeasures);
        
        return report;
    }

    @Override
    public String getOptionalParams(Params parameters) {
        return "";
    }
}
