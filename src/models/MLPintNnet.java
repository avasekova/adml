package models;

import java.util.ArrayList;
import java.util.List;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;
import params.MLPintNnetParams;
import params.NnetParams;
import params.Params;
import utils.Const;
import utils.ErrorMeasuresInterval;
import utils.ErrorMeasuresUtils;
import utils.MyRengine;
import utils.Utils;
import utils.imlp.Interval;

public class MLPintNnet implements Forecastable {
    
    @Override
    public TrainAndTestReport forecast(List<Double> allData, Params parameters) {
        List<TrainAndTestReportInterval> reports = new ArrayList<>();
        //train some number of networks
        for (int i = 0; i < ((MLPintNnetParams)parameters).getNumNetsToTrain(); i++) {
            reports.add((TrainAndTestReportInterval)(doTheActualForecast(allData, parameters)));
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

    private TrainAndTestReport doTheActualForecast(List<Double> allData, Params parameters) {
        NnetParams paramsCenter = ((MLPintNnetParams)parameters).getParamsCenter();
        NnetParams paramsRadius = ((MLPintNnetParams)parameters).getParamsRadius();
        
        List<Double> dataFirst = allData.subList(0, allData.size()/2);
        List<Double> dataSecond = allData.subList(allData.size()/2, allData.size());
        //nesublistovat! urobi sa to este raz v nnet, a potom hadze IndexOUBounds!
        
        //make sure we have data for centers and radii:
        List<Double> dataCenter;
        List<Double> dataRadius;
        if (((MLPintNnetParams)parameters).isCenterRadius()) {
            dataCenter = dataFirst;
            dataRadius = dataSecond;
        } else {
            final String LOWER = Const.INPUT + Utils.getCounter();
            final String UPPER = Const.INPUT + Utils.getCounter();
            
            Rengine rengine = MyRengine.getRengine();
            rengine.assign(LOWER, Utils.listToArray(dataFirst));
            rengine.assign(UPPER, Utils.listToArray(dataSecond));
            REXP getCenter = rengine.eval("(" + UPPER + " + " + LOWER + ")/2");
            REXP getRadius = rengine.eval("(" + UPPER + " - " + LOWER + ")/2");
            double[] centers = getCenter.asDoubleArray();
            double[] radii = getRadius.asDoubleArray();
            dataCenter = Utils.arrayToList(centers);
            dataRadius = Utils.arrayToList(radii);
        }
        
        Nnet nnet = new Nnet();
        TrainAndTestReportCrisp reportCenter = (TrainAndTestReportCrisp) nnet.forecast(dataCenter, paramsCenter);
        TrainAndTestReportCrisp reportRadius = (TrainAndTestReportCrisp) nnet.forecast(dataRadius, paramsRadius);
        
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
        
        List<Double> errorsTrain = Utils.getErrorsForIntervals(realOutputsIntervalTrain, fittedVals, ((MLPintNnetParams)parameters).getDistanceFunction());
        List<Double> errorsTest = Utils.getErrorsForIntervals(realOutputsIntervalTest, forecastsTest, ((MLPintNnetParams)parameters).getDistanceFunction());
        
        ErrorMeasuresInterval errorMeasures = new ErrorMeasuresInterval();
        errorMeasures.setMEtrain(ErrorMeasuresUtils.ME(errorsTrain));
        errorMeasures.setMEtest(ErrorMeasuresUtils.ME(errorsTest));
        errorMeasures.setRMSEtrain(ErrorMeasuresUtils.RMSE(errorsTrain));
        errorMeasures.setRMSEtest(ErrorMeasuresUtils.RMSE(errorsTest));
        errorMeasures.setMAEtrain(ErrorMeasuresUtils.MAE(errorsTrain));
        errorMeasures.setMAEtest(ErrorMeasuresUtils.MAE(errorsTest));
        errorMeasures.setMSEtrain(ErrorMeasuresUtils.MSE(errorsTrain));
        errorMeasures.setMSEtest(ErrorMeasuresUtils.MSE(errorsTest));
        errorMeasures.setMeanCoverageTrain(ErrorMeasuresUtils.meanCoverage(realOutputsIntervalTrain, fittedVals));
        errorMeasures.setMeanCoverageTest(ErrorMeasuresUtils.meanCoverage(realOutputsIntervalTest, forecastsTest));
        errorMeasures.setMeanEfficiencyTrain(ErrorMeasuresUtils.meanEfficiency(realOutputsIntervalTrain, fittedVals));
        errorMeasures.setMeanEfficiencyTest(ErrorMeasuresUtils.meanEfficiency(realOutputsIntervalTest, forecastsTest));
        errorMeasures.setTheilsUintervalTrain(ErrorMeasuresUtils.theilsUInterval(realOutputsIntervalTrain, fittedVals));
        errorMeasures.setTheilsUintervalTest(ErrorMeasuresUtils.theilsUInterval(realOutputsIntervalTest, forecastsTest));
        errorMeasures.setArvIntervalTrain(ErrorMeasuresUtils.ARVinterval(realOutputsIntervalTrain, fittedVals));
        errorMeasures.setArvIntervalTest(ErrorMeasuresUtils.ARVinterval(realOutputsIntervalTest, forecastsTest));

        TrainAndTestReportInterval report = new TrainAndTestReportInterval("MLP(i) (nnet)");
        report.setModelDescription("(" + ((MLPintNnetParams)parameters).getDistanceFunction() + ")");
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
