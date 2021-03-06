package models;

import models.params.IntervalHoltParams;
import models.params.Params;
import utils.*;
import utils.imlp.Interval;
import utils.imlp.dist.IchinoYaguchiDistance;
import utils.imlp.dist.WeightedEuclideanDistance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IntervalHolt implements Forecastable {
    private static final long serialVersionUID = 1L;
    //A.holt(dejta, h=10, alpha=alpha, beta=beta, gamma=FALSE)

    @Override
    public TrainAndTestReport forecast(Map<String, List<Double>> dataTableModel, Params parameters) {
        final String FORECAST_MODEL = Const.FORECAST_MODEL + Utils.getCounter();
        final String INPUT_TRAIN = Const.INPUT + Utils.getCounter();
        final String INPUT_TRAIN_LOWER = INPUT_TRAIN + ".lower";
        final String INPUT_TRAIN_UPPER = INPUT_TRAIN + ".upper";
        final String FIT_LOWER = Const.FIT + Utils.getCounter();
        final String FIT_UPPER = Const.FIT + Utils.getCounter();
        final String FORECAST = Const.FORECAST_VALS + Utils.getCounter();
        final String FORECAST_LOWER = FORECAST + ".lower";
        final String FORECAST_UPPER = FORECAST + ".upper";
        
        IntervalHoltParams params = (IntervalHoltParams) parameters;
        
        //here we create C+R, but iHolt works with L+U, so then transform
        List<Double> allDataCenter = dataTableModel.get(params.getColNameCenter());
        List<Double> allDataRadius = dataTableModel.get(params.getColNameRadius());
        List<Double> dataToUseCenter = new ArrayList<>(allDataCenter.subList((params.getDataRangeFrom() - 1), params.getDataRangeTo()));
        List<Double> dataToUseRadius = new ArrayList<>(allDataRadius.subList((params.getDataRangeFrom() - 1), params.getDataRangeTo()));
        
        int numTrainingEntries = Math.round(((float) params.getPercentTrain()/100)*dataToUseCenter.size());
        
        List<Double> inputTrainCenter = dataToUseCenter.subList(0, numTrainingEntries);
        List<Double> inputTrainRadius = dataToUseRadius.subList(0, numTrainingEntries);
        List<Double> inputTestCenter = dataToUseCenter.subList(numTrainingEntries, dataToUseCenter.size());
        List<Double> inputTestRadius = dataToUseRadius.subList(numTrainingEntries, dataToUseCenter.size());
        
        MyRengine rengine = MyRengine.getRengine();
        rengine.require("forecast");
        
        rengine.assign(INPUT_TRAIN + ".center", Utils.listToArray(inputTrainCenter));
        rengine.assign(INPUT_TRAIN + ".radius", Utils.listToArray(inputTrainRadius));
        rengine.eval(INPUT_TRAIN_LOWER + " <- (" + INPUT_TRAIN + ".center - " + INPUT_TRAIN + ".radius)");
        rengine.eval(INPUT_TRAIN_UPPER + " <- (" + INPUT_TRAIN + ".center + " + INPUT_TRAIN + ".radius)");
        
        //ts(file.df.ok, start = 1, end = file.df.len, frequency = 1)
        rengine.eval(INPUT_TRAIN + " <- data.frame(lowerboundary = " + INPUT_TRAIN_LOWER
                                                + ", upperboundary = " + INPUT_TRAIN_UPPER + ")");
        
        int num4castsTestAndTrain = inputTestCenter.size() + params.getNumForecasts();
        
        double weBeta = 0.5;
        if (params.getDistance() instanceof WeightedEuclideanDistance) {
            weBeta = ((WeightedEuclideanDistance) params.getDistance()).getBeta();
        }
        double iyGama = 0.5;
        if (params.getDistance() instanceof IchinoYaguchiDistance) {
            iyGama = ((IchinoYaguchiDistance) params.getDistance()).getGamma();
        }
        
        String alpha = "";
        if (! params.getAlpha().equals("NULL")) {
            alpha = ", alpha = " + params.getAlpha();
        }
        String beta = "";
        if (! params.getBeta().equals("NULL")) {
            beta = ", beta = " + params.getBeta();
        }
        
        //inv.holt(my.ts, h = 5, alpha = a, beta = b, control = list(distance="E", we.beta = 0.5, iy.gama = 0.5))
        rengine.eval(FORECAST_MODEL + " <- inv.holt(" + INPUT_TRAIN + ", h = " + num4castsTestAndTrain
                       + alpha + beta + ", control = list(distance = \"" + params.getDistanceId() + "\", we.beta = " + weBeta
                       + ", iy.gama = " + iyGama + "))");
        
        //-2, because it does not produce fit for the first two observations
        rengine.eval(FIT_LOWER + " <- " + FORECAST_MODEL + "$model$fitted[1:" + (numTrainingEntries-2) + ",2]");
        rengine.eval(FIT_UPPER + " <- " + FORECAST_MODEL + "$model$fitted[1:" + (numTrainingEntries-2) + ",1]");
        List<Double> fitLower = rengine.evalAndReturnList(FIT_LOWER);
        List<Double> fitUpper = rengine.evalAndReturnList(FIT_UPPER);
        //and add the first two dummy ones:
        fitLower.add(0, Double.NaN);
        fitLower.add(0, Double.NaN);
        fitUpper.add(0, Double.NaN);
        fitUpper.add(0, Double.NaN);
        
        List<Interval> fittedIntervals = Utils.zipLowerUpperToIntervals(fitLower, fitUpper);
        
        rengine.eval(FORECAST_LOWER + " <- " + FORECAST_MODEL + "$mean[1:" + num4castsTestAndTrain + ",2]");
        rengine.eval(FORECAST_UPPER + " <- " + FORECAST_MODEL + "$mean[1:" + num4castsTestAndTrain + ",1]");
        List<Double> forecastLower = rengine.evalAndReturnList(FORECAST_LOWER);
        List<Double> forecastUpper = rengine.evalAndReturnList(FORECAST_UPPER);
        
        List<Interval> forecastIntervalsAll = Utils.zipLowerUpperToIntervals(forecastLower, forecastUpper);
        List<Interval> forecastsTest = new ArrayList<>(forecastIntervalsAll.subList(0, num4castsTestAndTrain - params.getNumForecasts()));
        List<Interval> forecastsFuture = new ArrayList<>(forecastIntervalsAll.subList(num4castsTestAndTrain - params.getNumForecasts(), forecastIntervalsAll.size()));
        
        TrainAndTestReportInterval report = new TrainAndTestReportInterval(Model.INTERVAL_HOLT);
        report.setModelDescription(params.toString());

        report.setFittedValues(fittedIntervals);
        report.setForecastValuesTest(forecastsTest);
        report.setForecastValuesFuture(forecastsFuture);
        report.setNumTrainingEntries(numTrainingEntries);
        report.setRealValues(dataToUseCenter, dataToUseRadius);
        
        ErrorMeasuresInterval errorMeasures = ErrorMeasuresUtils.computeAllErrorMeasuresInterval(
                Utils.zipCentersRadiiToIntervals(inputTrainCenter, inputTrainRadius), 
                Utils.zipCentersRadiiToIntervals(inputTestCenter, inputTestRadius), 
                fittedIntervals, forecastsTest, params.getDistance(), params.getSeasonality());
        report.setErrorMeasures(errorMeasures);
        
        rengine.rm(FORECAST, FORECAST_MODEL, INPUT_TRAIN, INPUT_TRAIN_LOWER, INPUT_TRAIN_UPPER, FIT_LOWER, FIT_UPPER,
                INPUT_TRAIN + ".center", INPUT_TRAIN + ".radius");
        
        return report;
    }
}
