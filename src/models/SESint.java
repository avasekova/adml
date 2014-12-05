package models;

import gui.tablemodels.DataTableModel;
import java.util.List;
import params.Params;
import params.SESParams;
import params.SESintParams;
import utils.Const;
import utils.ErrorMeasuresInterval;
import utils.ErrorMeasuresUtils;
import utils.Utils;
import utils.imlp.Interval;

public class SESint implements Forecastable {

    //mam pocit, ze (skoro?) vsetky TS(i) su uplne rovnake az na typ tych objektov. mozno vytvorit genericku nadtriedu?
    @Override
    public TrainAndTestReport forecast(DataTableModel dataTableModel, Params parameters) {
        SESParams paramsCenter = ((SESintParams)parameters).getParamsCenter();
        SESParams paramsRadius = ((SESintParams)parameters).getParamsRadius();
        
        //bez ohladu na to, ci mam Center a Radius alebo LB a UB (tj ci isCenterRadius je true alebo false),
        //  pocita sa s tym ako s Center a Radius. takze nijak neupravujem data.
        
        SES ses = new SES();
        TrainAndTestReportCrisp reportCenter = (TrainAndTestReportCrisp) ses.forecast(dataTableModel, paramsCenter);
        TrainAndTestReportCrisp reportRadius = (TrainAndTestReportCrisp) ses.forecast(dataTableModel, paramsRadius);
        
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
        
        ErrorMeasuresInterval errorMeasures = ErrorMeasuresUtils.computeAllErrorMeasuresInterval(realOutputsIntervalTrain, 
                realOutputsIntervalTest, fittedVals, forecastsTest, ((SESintParams)parameters).getDistance(), 
                paramsCenter.getSeasonality());

        TrainAndTestReportInterval report = new TrainAndTestReportInterval(Const.SES_INT);
        report.setModelDescription("(" + ((SESintParams)parameters).getDistance() + "," + 
                reportCenter.getModelDescription() + "," + reportRadius.getModelDescription() + ")");
        report.setNumTrainingEntries(reportCenter.getNumTrainingEntries());
        
        
        report.setFittedValues(fittedVals);
        report.setForecastValuesTest(forecastsTest);
        report.setForecastValuesFuture(forecastsFuture);
        
        report.setErrorMeasures(errorMeasures);
        
        realOutputsIntervalTrain.addAll(realOutputsIntervalTest);
        report.setRealValues(realOutputsIntervalTrain);
        
        return report;
    }

    //TODO uz tu metodu fakt musim zmazat...
    @Override
    public String getOptionalParams(Params parameters) {
        return "";
    }
    
}
