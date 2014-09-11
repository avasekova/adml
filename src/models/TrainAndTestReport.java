package models;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

public class TrainAndTestReport {
    
    private List<Double> errorMeasures = new ArrayList<>();
    private List<Double> trainData = new ArrayList<>();
    private List<Double> testData = new ArrayList<>();
    private List<Double> forecastData = new ArrayList<>();
    private ImageIcon forecastPlot = new ImageIcon();
    
    public TrainAndTestReport() { }
    
    public TrainAndTestReport(List<Double> errorMeasures, List<Double> trainData,
                              List<Double> testData, List<Double> forecastData, ImageIcon forecastPlot) {
        this.errorMeasures = errorMeasures;
        this.trainData = trainData;
        this.testData = testData;
        this.forecastData = forecastData;
        this.forecastPlot = forecastPlot;
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

    public ImageIcon getForecastPlot() {
        return forecastPlot;
    }

    public void setForecastPlot(ImageIcon forecastPlot) {
        this.forecastPlot = forecastPlot;
    }
    
}
