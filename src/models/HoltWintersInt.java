package models;

import gui.tablemodels.DataTableModel;
import java.util.List;
import models.params.HoltWintersIntParams;
import models.params.HoltWintersParams;
import models.params.Params;
import utils.Const;
import utils.ErrorMeasuresInterval;
import utils.ErrorMeasuresUtils;
import utils.Utils;
import utils.imlp.Interval;
import utils.imlp.dist.WeightedEuclideanDistance;

public class HoltWintersInt implements Forecastable {

    //mam pocit, ze (skoro?) vsetky TS(i) su uplne rovnake az na typ tych objektov. mozno vytvorit genericku nadtriedu?
    @Override
    public TrainAndTestReport forecast(DataTableModel dataTableModel, Params parameters) {
        HoltWintersParams paramsCenter = ((HoltWintersIntParams)parameters).getParamsCenter();
        HoltWintersParams paramsRadius = ((HoltWintersIntParams)parameters).getParamsRadius();
        
        //bez ohladu na to, ci mam Center a Radius alebo LB a UB (tj ci isCenterRadius je true alebo false),
        //  pocita sa s tym ako s Center a Radius. takze nijak neupravujem data.
        
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
        
        realOutputsIntervalTrain.addAll(realOutputsIntervalTest);
        
        
        //ak to nie su seasonal data, R skonci s chybou
        if (reportCenter.getFittedValues().length == 0) { //ak neni fit, predpokladam, ze neni nic...
            TrainAndTestReportInterval report = new TrainAndTestReportInterval(Const.HOLT_WINTERS_INT);
            report.setModelDescription("(non-seasonal data!)");
            report.setRealValues(realOutputsIntervalTrain);
            report.setErrorMeasures(new ErrorMeasuresInterval(new WeightedEuclideanDistance(0.5)));
        
            return report;
        }
        
        ErrorMeasuresInterval errorMeasures = ErrorMeasuresUtils.computeAllErrorMeasuresInterval(realOutputsIntervalTrain, 
                realOutputsIntervalTest, fittedVals, forecastsTest, ((HoltWintersIntParams)parameters).getDistance(), 
                paramsCenter.getSeasonality());

        TrainAndTestReportInterval report = new TrainAndTestReportInterval(Const.HOLT_WINTERS_INT);
        report.setModelDescription(parameters.toString());
        report.setNumTrainingEntries(reportCenter.getNumTrainingEntries());
        
        
        report.setFittedValues(fittedVals);
        report.setForecastValuesTest(forecastsTest);
        report.setForecastValuesFuture(forecastsFuture);
        
        report.setErrorMeasures(errorMeasures);
        
        report.setRealValues(realOutputsIntervalTrain);
        
        return report;
    }

    //TODO uz tu metodu fakt musim zmazat...
    @Override
    public String getOptionalParams(Params parameters) {
        return "";
    }
}
