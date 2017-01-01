package models;

import models.params.Params;
import models.params.RandomWalkParams;
import utils.ErrorMeasuresCrisp;
import utils.ErrorMeasuresUtils;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RandomWalk implements Forecastable {

    @Override
    public TrainAndTestReport forecast(Map<String, List<Double>> data, Params parameters) {
        RandomWalkParams params = (RandomWalkParams) parameters;
        List<Double> allData = data.get(params.getColName());

        List<Double> dataToUse = allData.subList(params.getDataRangeFrom() - 1, params.getDataRangeTo());
        
        TrainAndTestReportCrisp report = new TrainAndTestReportCrisp(Model.RANDOM_WALK);
        
        int numTrainingEntries = Math.round(((float) 100/100)*dataToUse.size()); //TODO maybe take a different %age - e.g. to agree with the other models when avg
        report.setNumTrainingEntries(numTrainingEntries);
        
        List<Double> trainRealOutputs = new ArrayList<>(dataToUse.subList(0, numTrainingEntries));
        List<Double> testRealOutputs = new ArrayList<>(dataToUse.subList(numTrainingEntries, dataToUse.size()));
        List<Double> trainForecastOutputs = new ArrayList<>();
        trainForecastOutputs.add(Double.NaN);
        trainForecastOutputs.addAll(dataToUse.subList(0, numTrainingEntries-1));
        List<Double> testForecastOutputs = new ArrayList<>(dataToUse.subList(numTrainingEntries-1, dataToUse.size()-1));
        
        report.setRealOutputsTrain(Utils.listToArray(trainRealOutputs));
        report.setRealOutputsTest(Utils.listToArray(testRealOutputs));

        report.setPlotCode("plot.ts(c(" + Utils.arrayToRVectorString(Utils.listToArray(trainForecastOutputs)) + ","
                + Utils.arrayToRVectorString(Utils.listToArray(testForecastOutputs)) + "))");

        report.setFittedValues(Utils.listToArray(trainForecastOutputs));
        report.setForecastValuesTest(Utils.listToArray(testForecastOutputs));
        
        ErrorMeasuresCrisp errorMeasures = ErrorMeasuresUtils.computeAllErrorMeasuresCrisp(trainRealOutputs, testRealOutputs, 
                trainForecastOutputs, testForecastOutputs, params.getSeasonality());
        report.setErrorMeasures(errorMeasures);
        
        return report;
    }
}
