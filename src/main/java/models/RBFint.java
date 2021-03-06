package models;

import models.params.Params;
import models.params.RBFParams;
import models.params.RBFintParams;
import utils.BestModelCriterionInterval;
import utils.ErrorMeasuresInterval;
import utils.ErrorMeasuresUtils;
import utils.Utils;
import utils.imlp.Interval;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RBFint implements Forecastable {
    private static final long serialVersionUID = 1L;
    
    @Override
    public TrainAndTestReport forecast(Map<String, List<Double>> dataTableModel, Params parameters) {
        List<TrainAndTestReportInterval> reports = new ArrayList<>();
        //train some number of networks
        for (int i = 0; i < ((RBFintParams)parameters).getNumNetsToTrain(); i++) {
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

    private TrainAndTestReport doTheActualForecast(Map<String, List<Double>> dataTableModel, Params parameters) {
        RBFParams paramsCenter = ((RBFintParams)parameters).getParamsCenter();
        RBFParams paramsRadius = ((RBFintParams)parameters).getParamsRadius();
        
        //assuming we have C+R data
        
        RBF rbf = new RBF();
        TrainAndTestReportCrisp reportCenter = (TrainAndTestReportCrisp) rbf.forecast(dataTableModel, paramsCenter);
        TrainAndTestReportCrisp reportRadius = (TrainAndTestReportCrisp) rbf.forecast(dataTableModel, paramsRadius);
        
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
                realOutputsIntervalTest, fittedVals, forecastsTest, ((RBFintParams)parameters).getDistance(), 
                paramsCenter.getSeasonality());

        TrainAndTestReportInterval report = new TrainAndTestReportInterval(Model.RBF_INT);
        report.setModelDescription(parameters.toString());
        report.setNumTrainingEntries(reportCenter.getNumTrainingEntries());
        
        
        report.setFittedValues(fittedVals);
        report.setForecastValuesTest(forecastsTest);
        report.setForecastValuesFuture(forecastsFuture);
        
        report.setErrorMeasures(errorMeasures);
        
        //hack, so both plots could be in one var
        //TODO produce NN diagram?
//        report.setNnDiagramPlotCode(reportCenter.getNnDiagramPlotCode() + "; " + reportRadius.getNnDiagramPlotCode());
        
        realOutputsIntervalTrain.addAll(realOutputsIntervalTest);
        report.setRealValues(realOutputsIntervalTrain);
        
        return report;
    }
}
