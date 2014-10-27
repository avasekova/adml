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
        for (int i = 0; i < Math.min(trainingIntervals.size(), fittedVals.size()); i++) {
            if (!(((Double) fittedVals.get(i).getCentre()).isNaN()) &&
                !(((Double) fittedVals.get(i).getRadius()).isNaN())) {
                fittedValsWithoutNaN.add(fittedVals.get(i));
                trainingIntervalsWithoutNaN.add(trainingIntervals.get(i));
            }
        }
        
        //TODO nechat vybrat distance!
        List<Double> errorsTrain = Utils.getErrorsForIntervals(trainingIntervalsWithoutNaN, fittedValsWithoutNaN, ((MLPintNnetParams)parameters).getDistanceFunction());
        List<Double> errorsTest = Utils.getErrorsForIntervals(testingIntervals, forecastsTest, ((MLPintNnetParams)parameters).getDistanceFunction());
        
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

        TrainAndTestReportInterval report = new TrainAndTestReportInterval("MLP(i) (nnet)(" + ((MLPintNnetParams)parameters).getDistanceFunction() + ")");
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
