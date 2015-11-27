package models;

import models.params.MLPintNnetarParams;
import models.params.NnetarParams;
import models.params.Params;
import utils.*;
import utils.imlp.Interval;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MLPintNnetar implements Forecastable {
    private static final long serialVersionUID = 1L;

    @Override
    public TrainAndTestReport forecast(Map<String, List<Double>> dataTableModel, Params parameters) {
        List<TrainAndTestReportInterval> reports = new ArrayList<>();
        //train some number of networks
        for (int i = 0; i < ((MLPintNnetarParams)parameters).getNumNetsToTrain(); i++) {
            reports.add((TrainAndTestReportInterval)(doTheActualForecast(dataTableModel, parameters)));
        }
        
        //and then determine which one is the best
        TrainAndTestReportInterval bestReport = reports.get(0);
        double bestMeasures = BestModelCriterionInterval.computeCriterion(bestReport, parameters.getCriterion());
        if (reports.size() > 1) {
            for (int i = 1; i < reports.size(); i++) {
                double currentMeasures = BestModelCriterionInterval.computeCriterion(reports.get(i), parameters.getCriterion());
                if (BestModelCriterionInterval.isCurrentBetterThanBest(parameters.getCriterion(), currentMeasures, bestMeasures)) {
                    bestMeasures = currentMeasures;
                    bestReport = reports.get(i);
                }
            }
        }
        
        return bestReport;
    }
    
    public TrainAndTestReport doTheActualForecast(Map<String, List<Double>> dataTableModel, Params parameters) {
        NnetarParams paramsCenter = ((MLPintNnetarParams)parameters).getParamsCenter();
        NnetarParams paramsRadius = ((MLPintNnetarParams)parameters).getParamsRadius();
        
        Nnetar nnetar = new Nnetar();
        TrainAndTestReportCrisp reportCenter = (TrainAndTestReportCrisp) nnetar.forecast(dataTableModel, paramsCenter);
        TrainAndTestReportCrisp reportRadius = (TrainAndTestReportCrisp) nnetar.forecast(dataTableModel, paramsRadius);
        
        //sublistovat tie centers a radii az tu, ked uz to neovplyvni nnetar
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
                realOutputsIntervalTest, fittedVals, forecastsTest, ((MLPintNnetarParams)parameters).getDistance(), 
                paramsCenter.getSeasonality());

        TrainAndTestReportInterval report = new TrainAndTestReportInterval(Model.MLP_INT_NNETAR);
        report.setModelDescription(parameters.toString());
        report.setNumTrainingEntries(reportCenter.getNumTrainingEntries());
        
        
        report.setFittedValues(fittedVals);
        report.setForecastValuesTest(forecastsTest);
        report.setForecastValuesFuture(forecastsFuture);
        
        report.setErrorMeasures(errorMeasures);
        
        //hack, aby sme mohli mat oba ploty v jednej premennej
        report.setNnDiagramPlotCode(reportCenter.getNnDiagramPlotCode() + "; " + reportRadius.getNnDiagramPlotCode());
        
        realOutputsIntervalTrain.addAll(realOutputsIntervalTest);
        report.setRealValues(realOutputsIntervalTrain);
        
        return report;
    }
}
