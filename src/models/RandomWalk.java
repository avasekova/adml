package models;

import java.util.ArrayList;
import java.util.List;
import org.rosuda.JRI.Rengine;
import models.params.RandomWalkParams;
import utils.Const;
import utils.ErrorMeasuresCrisp;
import utils.ErrorMeasuresUtils;
import utils.MyRengine;
import utils.Utils;

public class RandomWalk { //TODO implements Forecastable!
    
    public TrainAndTestReport forecast(List<Double> allData, RandomWalkParams params) {
        final String DATA_FIT = Const.INPUT + Utils.getCounter();
        final String DATA_FORECAST = Const.INPUT + Utils.getCounter();
        List<Double> dataToUse = allData.subList(params.getDataRangeFrom() - 1, params.getDataRangeTo());
        
        TrainAndTestReportCrisp report = new TrainAndTestReportCrisp(Const.RANDOM_WALK);
        
        int numTrainingEntries = Math.round(((float) params.getPercentTrain()/100)*dataToUse.size());
        report.setNumTrainingEntries(numTrainingEntries);
        
        List<Double> trainRealOutputs = dataToUse.subList(0, numTrainingEntries);
        List<Double> testRealOutputs = dataToUse.subList(numTrainingEntries, dataToUse.size());
        List<Double> trainForecastOutputs = new ArrayList<>();
        trainForecastOutputs.add(Double.NaN);
        trainForecastOutputs.addAll(dataToUse.subList(0, numTrainingEntries-1));
        List<Double> testForecastOutputs = dataToUse.subList(numTrainingEntries-1, dataToUse.size()-1);
        
        report.setRealOutputsTrain(Utils.listToArray(trainRealOutputs));
        report.setRealOutputsTest(Utils.listToArray(testRealOutputs));
        
        Rengine rengine = MyRengine.getRengine();
        rengine.assign(DATA_FIT, Utils.listToArray(trainForecastOutputs));
        rengine.assign(DATA_FORECAST, Utils.listToArray(testForecastOutputs));
        report.setPlotCode("plot.ts(c(" + DATA_FIT + "," + DATA_FORECAST + "))");
        
        report.setFittedValues(Utils.listToArray(trainForecastOutputs));
        report.setForecastValuesTest(Utils.listToArray(testForecastOutputs));
        
        ErrorMeasuresCrisp errorMeasures = ErrorMeasuresUtils.computeAllErrorMeasuresCrisp(trainRealOutputs, testRealOutputs, 
                trainForecastOutputs, testForecastOutputs, params.getSeasonality());
        report.setErrorMeasures(errorMeasures);
        
        return report;
    }
}
