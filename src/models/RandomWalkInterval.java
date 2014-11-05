package models;

import java.util.ArrayList;
import java.util.List;
import params.RandomWalkIntervalParams;
import utils.ErrorMeasuresInterval;
import utils.ErrorMeasuresUtils;
import utils.imlp.Interval;
import utils.imlp.IntervalLowerUpper;
import utils.imlp.dist.Distance;

public class RandomWalkInterval {

    public TrainAndTestReport forecast(List<Interval> allData, RandomWalkIntervalParams params) {
        List<Interval> dataToUse = allData.subList(params.getDataRangeFrom(), params.getDataRangeTo());
        
        TrainAndTestReportInterval report = new TrainAndTestReportInterval("random walk (i)");
        
        int numTrainingEntries = Math.round(((float) params.getPercentTrain()/100)*dataToUse.size());
        report.setNumTrainingEntries(numTrainingEntries);
        
        List<Interval> trainRealOutputs = dataToUse.subList(0, numTrainingEntries);
        List<Interval> testRealOutputs = dataToUse.subList(numTrainingEntries, dataToUse.size());
        List<Interval> trainForecastOutputs = new ArrayList<>();
        trainForecastOutputs.add(new IntervalLowerUpper(Double.NaN, Double.NaN));
        trainForecastOutputs.addAll(dataToUse.subList(0, numTrainingEntries-1));
        List<Interval> testForecastOutputs = dataToUse.subList(numTrainingEntries-1, dataToUse.size()-1);
        
        report.setRealValues(dataToUse);
        
        report.setFittedValues(trainForecastOutputs);
        report.setForecastValuesTest(testForecastOutputs);
        
        Distance distance = params.getDistance();
        List<Double> errorsTrain = new ArrayList<>();
        for (int i = 0; i < trainRealOutputs.size(); i++) {
            //real = i; forecast = i-1
            errorsTrain.add(distance.getDistance(trainForecastOutputs.get(i), trainRealOutputs.get(i)));
        }
        
        List<Double> errorsTest = new ArrayList<>();
        for (int i = 0; i < testRealOutputs.size(); i++) {
            //real = i; forecast = i-1
            errorsTest.add(distance.getDistance(testForecastOutputs.get(i), testRealOutputs.get(i)));
        }
        
        ErrorMeasuresInterval errorMeasures = new ErrorMeasuresInterval();
        errorMeasures.setMEtrain(ErrorMeasuresUtils.ME(errorsTrain));
        errorMeasures.setMEtest(ErrorMeasuresUtils.ME(errorsTest));
        errorMeasures.setRMSEtrain(ErrorMeasuresUtils.RMSE(errorsTrain));
        errorMeasures.setRMSEtest(ErrorMeasuresUtils.RMSE(errorsTest));
        errorMeasures.setMAEtrain(ErrorMeasuresUtils.MAE(errorsTrain));
        errorMeasures.setMAEtest(ErrorMeasuresUtils.MAE(errorsTest));
        errorMeasures.setMSEtrain(ErrorMeasuresUtils.MSE(errorsTrain));
        errorMeasures.setMSEtest(ErrorMeasuresUtils.MSE(errorsTest));
        errorMeasures.setMeanCoverageTrain(ErrorMeasuresUtils.meanCoverage(trainRealOutputs, trainForecastOutputs));
        errorMeasures.setMeanCoverageTest(ErrorMeasuresUtils.meanCoverage(testRealOutputs, testForecastOutputs));
        errorMeasures.setMeanEfficiencyTrain(ErrorMeasuresUtils.meanEfficiency(trainRealOutputs, trainForecastOutputs));
        errorMeasures.setMeanEfficiencyTest(ErrorMeasuresUtils.meanEfficiency(testRealOutputs, testForecastOutputs));
        errorMeasures.setTheilsUintervalTrain(ErrorMeasuresUtils.theilsUInterval(trainRealOutputs, trainForecastOutputs));
        errorMeasures.setTheilsUintervalTest(ErrorMeasuresUtils.theilsUInterval(testRealOutputs, testForecastOutputs));
        errorMeasures.setArvIntervalTrain(ErrorMeasuresUtils.ARVinterval(trainRealOutputs, trainForecastOutputs));
        errorMeasures.setArvIntervalTest(ErrorMeasuresUtils.ARVinterval(testRealOutputs, testForecastOutputs));
        report.setErrorMeasures(errorMeasures);
        
        return report;
    }
}
