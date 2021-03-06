package models;

import models.params.BNNParams;
import models.params.BNNintParams;
import models.params.Params;
import utils.BestModelCriterionInterval;
import utils.ErrorMeasuresInterval;
import utils.ErrorMeasuresUtils;
import utils.Utils;
import utils.imlp.Interval;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BNNint implements Forecastable {
    private static final long serialVersionUID = 1L;
    
    @Override
    public TrainAndTestReport forecast(Map<String, List<Double>> dataTableModel, Params parameters) {
        List<TrainAndTestReportInterval> reports = new ArrayList<>();
        //train some number of networks
        for (int i = 0; i < ((BNNintParams)parameters).getNumNetsToTrain(); i++) {
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
        BNNParams paramsCenter = ((BNNintParams)parameters).getParamsCenter();
        BNNParams paramsRadius = ((BNNintParams)parameters).getParamsRadius();
        
        //regardles of having Center + Radius or LB + UB (i.e. if isCenterRadius is true or false),
        //  we assume it's C+R. so no change to the data.
        
        BNN bnn = new BNN();
        TrainAndTestReportCrisp reportCenter = (TrainAndTestReportCrisp) bnn.forecast(dataTableModel, paramsCenter);
        TrainAndTestReportCrisp reportRadius = (TrainAndTestReportCrisp) bnn.forecast(dataTableModel, paramsRadius);
        
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
                realOutputsIntervalTest, fittedVals, forecastsTest, ((BNNintParams)parameters).getDistance(), 
                paramsCenter.getSeasonality());

        TrainAndTestReportInterval report = new TrainAndTestReportInterval(Model.BNN_INT);
        report.setModelDescription(parameters.toString());
        report.setNumTrainingEntries(reportCenter.getNumTrainingEntries());
        
        
        report.setFittedValues(fittedVals);
        report.setForecastValuesTest(forecastsTest);
        report.setForecastValuesFuture(forecastsFuture);
        
        report.setErrorMeasures(errorMeasures);
        
        //hack, so we could have both plots in one variable
        //TODO produce NN diagram?
//        report.setNnDiagramPlotCode(reportCenter.getNnDiagramPlotCode() + "; " + reportRadius.getNnDiagramPlotCode());
        
        realOutputsIntervalTrain.addAll(realOutputsIntervalTest);
        report.setRealValues(realOutputsIntervalTrain);
        
        return report;
    }
}
