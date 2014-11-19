package models;

import gui.DataTableModel;
import java.util.ArrayList;
import java.util.List;
import params.MLPintNnetParams;
import params.NnetParams;
import params.Params;
import utils.Const;
import utils.ErrorMeasuresInterval;
import utils.ErrorMeasuresUtils;
import utils.Utils;
import utils.imlp.Interval;

public class MLPintNnet implements Forecastable {
    
    @Override
    public TrainAndTestReport forecast(DataTableModel dataTableModel, Params parameters) {
        List<TrainAndTestReportInterval> reports = new ArrayList<>();
        //train some number of networks
        for (int i = 0; i < ((MLPintNnetParams)parameters).getNumNetsToTrain(); i++) {
            reports.add((TrainAndTestReportInterval)(doTheActualForecast(dataTableModel, parameters)));
        }
        
        //and then determine which one is the best
        //TODO for now coverage+efficiency, later allow to customize
        TrainAndTestReportInterval bestReport = reports.get(0);
        double bestMeasures = computeCriterion(bestReport);
        if (reports.size() > 1) {
            for (int i = 1; i < reports.size(); i++) {
                double currentMeasures = computeCriterion(reports.get(i));
                if (currentMeasures > bestMeasures) {
                    bestMeasures = currentMeasures;
                    bestReport = reports.get(i);
                }
            }
        }
        
        return bestReport;
    }
    
    private double computeCriterion(TrainAndTestReportInterval report) {
        ErrorMeasuresInterval m = (ErrorMeasuresInterval)(report.getErrorMeasures());
        //sum of coverages and efficiencies
        return m.getMeanCoverageTest() + m.getMeanCoverageTrain() + m.getMeanEfficiencyTest() + m.getMeanEfficiencyTrain();
    }

    private TrainAndTestReport doTheActualForecast(DataTableModel dataTableModel, Params parameters) {
        NnetParams paramsCenter = ((MLPintNnetParams)parameters).getParamsCenter();
        NnetParams paramsRadius = ((MLPintNnetParams)parameters).getParamsRadius();
        
        Nnet nnet = new Nnet();
        TrainAndTestReportCrisp reportCenter = (TrainAndTestReportCrisp) nnet.forecast(dataTableModel, paramsCenter);
        TrainAndTestReportCrisp reportRadius = (TrainAndTestReportCrisp) nnet.forecast(dataTableModel, paramsRadius);
        
        //sublistovat tie centers a radii az tu, ked uz to neovplyvni nnet
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
                realOutputsIntervalTest, fittedVals, forecastsTest, ((MLPintNnetParams)parameters).getDistance(), 
                paramsCenter.getSeasonality());

        TrainAndTestReportInterval report = new TrainAndTestReportInterval(Const.MLP_INT_NNET);
        report.setModelDescription("(" + ((MLPintNnetParams)parameters).getDistance() + ")");
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

    @Override
    public String getOptionalParams(Params parameters) {
        return "";
    }
}
