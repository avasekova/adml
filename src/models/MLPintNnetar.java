package models;

import java.util.ArrayList;
import java.util.List;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;
import params.MLPintNnetarParams;
import params.NnetarParams;
import params.Params;
import utils.Const;
import utils.ErrorMeasuresInterval;
import utils.ErrorMeasuresUtils;
import utils.MyRengine;
import utils.Utils;
import utils.imlp.Interval;

public class MLPintNnetar implements Forecastable {

    @Override
    public TrainAndTestReport forecast(List<Double> allData, Params parameters) {
        NnetarParams paramsCenter = ((MLPintNnetarParams)parameters).getParamsCenter();
        NnetarParams paramsRadius = ((MLPintNnetarParams)parameters).getParamsRadius();
        
        List<Double> dataFirst = allData.subList(0, allData.size()/2);
        List<Double> dataSecond = allData.subList(allData.size()/2, allData.size());
        //nesublistovat! urobi sa to este raz v nnetar, a potom hadze IndexOUBounds!
        
        //make sure we have data for centers and radii:
        List<Double> dataCenter;
        List<Double> dataRadius;
        if (((MLPintNnetarParams)parameters).isCenterRadius()) {
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
        
        Nnetar nnetar = new Nnetar();
        TrainAndTestReportCrisp reportCenter = (TrainAndTestReportCrisp) nnetar.forecast(dataCenter, paramsCenter);
        TrainAndTestReportCrisp reportRadius = (TrainAndTestReportCrisp) nnetar.forecast(dataRadius, paramsRadius);
        
        //sublistovat tie centers a radii az tu, ked uz to neovplyvni nnetar
        List<Interval> realDataInterval = Utils.zipCentersRadiiToIntervals(
                dataCenter.subList((paramsCenter.getDataRangeFrom() - 1), paramsCenter.getDataRangeTo()),
                dataRadius.subList((paramsRadius.getDataRangeFrom() - 1), paramsRadius.getDataRangeTo()));
        List<Interval> fittedVals = Utils.zipCentersRadiiToIntervals(Utils.arrayToList(reportCenter.getFittedValues()),
                Utils.arrayToList(reportRadius.getFittedValues()));
        
        List<Interval> trainingIntervals = realDataInterval.subList(0, reportCenter.getNumTrainingEntries());
        List<Interval> testingIntervals = realDataInterval.subList(reportCenter.getNumTrainingEntries(), realDataInterval.size());
        
        List<Interval> forecastsTest = Utils.zipCentersRadiiToIntervals(Utils.arrayToList(reportCenter.getForecastValuesTest()),
                Utils.arrayToList(reportRadius.getForecastValuesTest()));
        List<Interval> forecastsFuture = Utils.zipCentersRadiiToIntervals(Utils.arrayToList(reportCenter.getForecastValuesFuture()),
                Utils.arrayToList(reportRadius.getForecastValuesFuture()));
        
        //trim to rectangle:
        List<Interval> fittedValsWithoutNaN = new ArrayList<>();
        List<Interval> trainingIntervalsWithoutNaN = new ArrayList<>();
        for (int i = 0; i < fittedVals.size(); i++) {
            if (!(((Double) fittedVals.get(i).getCentre()).isNaN()) &&
                !(((Double) fittedVals.get(i).getRadius()).isNaN())) {
                fittedValsWithoutNaN.add(fittedVals.get(i));
                trainingIntervalsWithoutNaN.add(trainingIntervals.get(i));
            }
        }
        
        //TODO nechat vybrat distance!
        List<Double> errorsTrain = Utils.getErrorsForIntervals(trainingIntervalsWithoutNaN, fittedValsWithoutNaN, ((MLPintNnetarParams)parameters).getDistanceFunction());
        List<Double> errorsTest = Utils.getErrorsForIntervals(testingIntervals, forecastsTest, ((MLPintNnetarParams)parameters).getDistanceFunction());
        
        ErrorMeasuresInterval errorMeasures = new ErrorMeasuresInterval();
        errorMeasures.setMEtrain(ErrorMeasuresUtils.ME(errorsTrain));
        errorMeasures.setMEtest(ErrorMeasuresUtils.ME(errorsTest));
        errorMeasures.setRMSEtrain(ErrorMeasuresUtils.RMSE(errorsTrain));
        errorMeasures.setRMSEtest(ErrorMeasuresUtils.RMSE(errorsTest));
        errorMeasures.setMAEtrain(ErrorMeasuresUtils.MAE(errorsTrain));
        errorMeasures.setMAEtest(ErrorMeasuresUtils.MAE(errorsTest));
        errorMeasures.setMSEtrain(ErrorMeasuresUtils.MSE(errorsTrain));
        errorMeasures.setMSEtest(ErrorMeasuresUtils.MSE(errorsTest));
        errorMeasures.setMeanCoverageTrain(ErrorMeasuresUtils.meanCoverage(trainingIntervalsWithoutNaN, fittedValsWithoutNaN));
        errorMeasures.setMeanCoverageTest(ErrorMeasuresUtils.meanCoverage(testingIntervals, forecastsTest));
        errorMeasures.setMeanEfficiencyTrain(ErrorMeasuresUtils.meanEfficiency(trainingIntervalsWithoutNaN, fittedValsWithoutNaN));
        errorMeasures.setMeanEfficiencyTest(ErrorMeasuresUtils.meanEfficiency(testingIntervals, forecastsTest));
        errorMeasures.setTheilsUintervalTrain(ErrorMeasuresUtils.theilsUInterval(trainingIntervals, fittedValsWithoutNaN));
        errorMeasures.setTheilsUintervalTest(ErrorMeasuresUtils.theilsUInterval(testingIntervals, forecastsTest));
        errorMeasures.setArvIntervalTrain(ErrorMeasuresUtils.ARVinterval(trainingIntervals, fittedValsWithoutNaN));
        errorMeasures.setArvIntervalTest(ErrorMeasuresUtils.ARVinterval(testingIntervals, forecastsTest));

        TrainAndTestReportInterval report = new TrainAndTestReportInterval("MLP(i) (nnetar)(" + ((MLPintNnetarParams)parameters).getDistanceFunction() + ")");
        report.setNumTrainingEntries(reportCenter.getNumTrainingEntries());
        
        
        //mam realDataInterval, chcem z toho centers a radii
        report.setRealValues(realDataInterval);
        
        report.setFittedValues(fittedVals);
        report.setForecastValuesTest(forecastsTest);
        report.setForecastValuesFuture(forecastsFuture);
        
        report.setErrorMeasures(errorMeasures);
        
        //hack, aby sme mohli mat oba ploty v jednej premennej
        report.setNnDiagramPlotCode(reportCenter.getNnDiagramPlotCode() + "; " + reportRadius.getNnDiagramPlotCode());
        
        return report;
    }

    @Override
    public String getOptionalParams(Params parameters) {
        return "";
    }
}
