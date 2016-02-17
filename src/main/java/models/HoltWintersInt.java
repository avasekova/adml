package models;

import models.params.HoltWintersIntParams;
import models.params.HoltWintersParams;
import models.params.Params;
import utils.ErrorMeasuresInterval;
import utils.ErrorMeasuresUtils;
import utils.Utils;
import utils.imlp.Interval;
import utils.imlp.dist.WeightedEuclideanDistance;

import java.util.List;
import java.util.Map;

public class HoltWintersInt implements Forecastable {
    private static final long serialVersionUID = 1L;

    //TODO generic superclass?
    @Override
    public TrainAndTestReport forecast(Map<String, List<Double>> dataTableModel, Params parameters) {
        HoltWintersParams paramsCenter = ((HoltWintersIntParams)parameters).getParamsCenter();
        HoltWintersParams paramsRadius = ((HoltWintersIntParams)parameters).getParamsRadius();

        //regardles of having Center + Radius or LB + UB (i.e. if isCenterRadius is true or false),
        //  we assume it's C+R. so no change to the data.
        
        HoltWinters holtWinters = new HoltWinters();
        TrainAndTestReportCrisp reportCenter = (TrainAndTestReportCrisp) holtWinters.forecast(dataTableModel, paramsCenter);
        TrainAndTestReportCrisp reportRadius = (TrainAndTestReportCrisp) holtWinters.forecast(dataTableModel, paramsRadius);
        
        List<Interval> realOutputsIntervalTrain = Utils.zipCentersRadiiToIntervals(
                Utils.arrayToList(reportCenter.getRealOutputsTrain()), Utils.arrayToList(reportRadius.getRealOutputsTrain()));
        List<Interval> realOutputsIntervalTest = Utils.zipCentersRadiiToIntervals(
                Utils.arrayToList(reportCenter.getRealOutputsTest()), Utils.arrayToList(reportRadius.getRealOutputsTest()));
        List<Interval> fittedVals = Utils.zipCentersRadiiToIntervals(Utils.arrayToList(reportCenter.getFittedValues()),
                Utils.arrayToList(reportRadius.getFittedValues()));
        List<Interval> forecastsTest = Utils.zipCentersRadiiToIntervals(Utils.arrayToList(reportCenter.getForecastValuesTest()),
                Utils.arrayToList(reportRadius.getForecastValuesTest()));
        List<Interval> forecastsFuture = Utils.zipCentersRadiiToIntervals(Utils.arrayToList(reportCenter.getForecastValuesFuture()),
                Utils.arrayToList(reportRadius.getForecastValuesFuture()));
        
        //if this is not seasonal data, R throws an error
        if (reportCenter.getFittedValues().length == 0) { //if there's no fit, we assume there's nothing...
            TrainAndTestReportInterval report = new TrainAndTestReportInterval(Model.HOLT_WINTERS_INT);
            report.setModelDescription("(non-seasonal data!)");
            report.setRealValues(realOutputsIntervalTrain);
            report.setErrorMeasures(new ErrorMeasuresInterval(new WeightedEuclideanDistance(0.5)));
        
            return report;
        }
        
        ErrorMeasuresInterval errorMeasures = ErrorMeasuresUtils.computeAllErrorMeasuresInterval(realOutputsIntervalTrain, 
                realOutputsIntervalTest, fittedVals, forecastsTest, ((HoltWintersIntParams)parameters).getDistance(), 
                paramsCenter.getSeasonality());

        TrainAndTestReportInterval report = new TrainAndTestReportInterval(Model.HOLT_WINTERS_INT);
        report.setModelDescription(parameters.toString());
        report.setNumTrainingEntries(reportCenter.getNumTrainingEntries());
        
        
        report.setFittedValues(fittedVals);
        report.setForecastValuesTest(forecastsTest);
        report.setForecastValuesFuture(forecastsFuture);
        
        report.setErrorMeasures(errorMeasures);
        
        realOutputsIntervalTrain.addAll(realOutputsIntervalTest);
        report.setRealValues(realOutputsIntervalTrain);
        
        return report;
    }
}
