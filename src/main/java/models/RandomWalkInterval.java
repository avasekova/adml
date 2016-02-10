package models;

import models.params.Params;
import models.params.RandomWalkIntervalParams;
import utils.ErrorMeasuresInterval;
import utils.ErrorMeasuresUtils;
import utils.Utils;
import utils.imlp.Interval;
import utils.imlp.IntervalLowerUpper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RandomWalkInterval implements Forecastable {

    public TrainAndTestReport forecast(Map<String, List<Double>> data, Params parameters) {
        RandomWalkIntervalParams params = (RandomWalkIntervalParams) parameters;
        List<Interval> allData = Utils.zipCentersRadiiToIntervals(data.get(params.getColnameCenter()), data.get(params.getColnameRadius()));

        List<Interval> dataToUse = allData.subList(params.getDataRangeFrom() - 1, params.getDataRangeTo());
        
        TrainAndTestReportInterval report = new TrainAndTestReportInterval(Model.RANDOM_WALK_INT);
        
        int numTrainingEntries = Math.round(((float) 100/100)*dataToUse.size()); //TODO vymysliet, kolko % brat - napr. nech sedi pri avg
        report.setNumTrainingEntries(numTrainingEntries);
        
        List<Interval> trainRealOutputs = new ArrayList<>(dataToUse.subList(0, numTrainingEntries));
        List<Interval> testRealOutputs = new ArrayList<>(dataToUse.subList(numTrainingEntries, dataToUse.size()));
        List<Interval> trainForecastOutputs = new ArrayList<>();
        trainForecastOutputs.add(new IntervalLowerUpper(Double.NaN, Double.NaN));
        trainForecastOutputs.addAll(dataToUse.subList(0, numTrainingEntries-1));
        List<Interval> testForecastOutputs = new ArrayList<>(dataToUse.subList(numTrainingEntries-1, dataToUse.size()-1));
        
        report.setRealValues(dataToUse);
        
        report.setFittedValues(trainForecastOutputs);
        report.setForecastValuesTest(testForecastOutputs);
        
        ErrorMeasuresInterval errorMeasures = ErrorMeasuresUtils.computeAllErrorMeasuresInterval(trainRealOutputs, testRealOutputs, 
                trainForecastOutputs, testForecastOutputs, params.getDistance(), params.getSeasonality());
        report.setErrorMeasures(errorMeasures);
        
        return report;
    }
}
