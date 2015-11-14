package models;

import java.util.ArrayList;
import java.util.List;
import models.params.RandomWalkIntervalParams;
import utils.Const;
import utils.ErrorMeasuresInterval;
import utils.ErrorMeasuresUtils;
import utils.imlp.Interval;
import utils.imlp.IntervalLowerUpper;

public class RandomWalkInterval { //TODO implements Forecastable!

    public TrainAndTestReport forecast(List<Interval> allData, RandomWalkIntervalParams params) {
        List<Interval> dataToUse = allData.subList(params.getDataRangeFrom() - 1, params.getDataRangeTo());
        
        TrainAndTestReportInterval report = new TrainAndTestReportInterval(Const.RANDOM_WALK_INT);
        
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
        
        ErrorMeasuresInterval errorMeasures = ErrorMeasuresUtils.computeAllErrorMeasuresInterval(trainRealOutputs, testRealOutputs, 
                trainForecastOutputs, testForecastOutputs, params.getDistance(), params.getSeasonality());
        report.setErrorMeasures(errorMeasures);
        
        return report;
    }
}
