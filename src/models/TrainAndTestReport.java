package models;

import java.util.ArrayList;
import java.util.List;

public class TrainAndTestReport {
    
    private String modelName = "";
    private List<Double> errorMeasures = new ArrayList<>(); //TODO Map<String, Double> fixne metriky pre vsetkych
    private List<Double> trainData = new ArrayList<>();
    private List<Double> testData = new ArrayList<>();
    private List<Double> forecastData = new ArrayList<>();
    private String forecastPlotCode = "";
    private double rangeMin;
    private double rangeMax;
    
    public TrainAndTestReport(String modelName) { 
        this.modelName = modelName;
    }
    
    public TrainAndTestReport(String modelName, List<Double> errorMeasures, List<Double> trainData,
                              List<Double> testData, List<Double> forecastData, String forecastPlot) {
        this.modelName = modelName;
        this.errorMeasures = errorMeasures;
        this.trainData = trainData;
        this.testData = testData;
        this.forecastData = forecastData;
        this.forecastPlotCode = forecastPlot;
    }

    public String getModelName() {
        return modelName;
    }
    
    public List<Double> getErrorMeasures() {
        return errorMeasures;
    }

    public void setErrorMeasures(List<Double> errorMeasures) {
        this.errorMeasures = errorMeasures;
    }

    public List<Double> getTrainData() {
        return trainData;
    }

    public void setTrainData(List<Double> trainData) {
        this.trainData = trainData;
    }

    public List<Double> getTestData() {
        return testData;
    }

    public void setTestData(List<Double> testData) {
        this.testData = testData;
    }

    public List<Double> getForecastData() {
        return forecastData;
    }

    public void setForecastData(List<Double> forecastData) {
        this.forecastData = forecastData;
    }

    public String getForecastPlotCode() {
        return forecastPlotCode;
    }

    public void setForecastPlotCode(String forecastPlot) {
        this.forecastPlotCode = forecastPlot;
    }

    public double getRangeMin() {
        return rangeMin;
    }

    public void setRangeMin(double rangeMin) {
        this.rangeMin = rangeMin;
    }

    public double getRangeMax() {
        return rangeMax;
    }

    public void setRangeMax(double rangeMax) {
        this.rangeMax = rangeMax;
    }
}
