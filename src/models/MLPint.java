package models;

import java.util.ArrayList;
import java.util.List;
import params.NnetarParams;
import params.Params;
import utils.ErrorMeasuresInterval;
import utils.ErrorMeasuresUtils;
import utils.Utils;
import utils.imlp.Interval;
import utils.imlp.WeightedEuclideanDistance;

public class MLPint implements Forecastable {

    @Override
    public TrainAndTestReport forecast(List<Double> allData, Params parameters) {
        List<Double> dataCenter = allData.subList(0, allData.size()/2);
        List<Double> dataRadius = allData.subList(allData.size()/2, allData.size());
        
        NnetarParams params = (NnetarParams) parameters;
        params.setNumForecasts(0); //zatial nechcem forecasty, lebo mi kvoli tomu blbnu vsade indexy... TODO dorobit
        
        Nnetar nnetar = new Nnetar();
        TrainAndTestReportCrisp reportCenter = (TrainAndTestReportCrisp) nnetar.forecast(dataCenter, params);
        TrainAndTestReportCrisp reportRadius = (TrainAndTestReportCrisp) nnetar.forecast(dataRadius, params);
        
        List<Interval> realDataInterval = Utils.zipCentersRadiiToIntervals(dataCenter, dataRadius);
        List<Interval> fittedVals = Utils.zipCentersRadiiToIntervals(Utils.arrayToList(reportCenter.getFittedValues()),
                Utils.arrayToList(reportRadius.getFittedValues()));
        List<Interval> forecastVals = Utils.zipCentersRadiiToIntervals(Utils.arrayToList(reportCenter.getForecastValues()),
                Utils.arrayToList(reportRadius.getForecastValues()));
        List<Interval> trainingIntervals = realDataInterval.subList(0, reportCenter.getNumTrainingEntries());
        List<Interval> testingIntervals = realDataInterval.subList(reportCenter.getNumTrainingEntries(), realDataInterval.size());
        
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
        
        List<Double> errorsTrain = Utils.getErrorsForIntervals(trainingIntervalsWithoutNaN, fittedValsWithoutNaN, new WeightedEuclideanDistance(0.5));
        List<Double> errorsTest = Utils.getErrorsForIntervals(testingIntervals, forecastVals, new WeightedEuclideanDistance(0.5));
        
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
        errorMeasures.setMeanCoverageTest(ErrorMeasuresUtils.meanCoverage(testingIntervals, forecastVals));
        errorMeasures.setMeanEfficiencyTrain(ErrorMeasuresUtils.meanEfficiency(trainingIntervalsWithoutNaN, fittedValsWithoutNaN));
        errorMeasures.setMeanEfficiencyTest(ErrorMeasuresUtils.meanEfficiency(testingIntervals, forecastVals));
        
        TrainAndTestReportInterval report = new TrainAndTestReportInterval("MLP(i)");
        report.setNumTrainingEntries(reportCenter.getNumTrainingEntries());
        report.setRealValues(dataCenter, dataRadius);
        
        report.setFittedValues(fittedVals);
        report.setForecastValues(forecastVals);
        
        report.setErrorMeasures(errorMeasures);
        
        return report;
    }

    @Override
    public String getOptionalParams(Params parameters) {
        return "";
    }
}
