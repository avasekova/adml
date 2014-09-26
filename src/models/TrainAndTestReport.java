package models;

import java.util.ArrayList;
import java.util.List;

public class TrainAndTestReport {
    
    private final String modelName;
    private List<Double> errorMeasures = new ArrayList<>(); //TODO Map<String, Double> fixne metriky pre vsetkych
    private int numTrainingEntries;
    private String fittedValuesPlotCode = ""; //TODO toto zmazat a kreslit to rucne, ale
    private double[] fittedValues;
    private double[] forecastValues = new double[] {};
    
    public TrainAndTestReport(String modelName) { 
        this.modelName = modelName;
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

    public int getNumTrainingEntries() {
        return numTrainingEntries;
    }

    public void setNumTrainingEntries(int numTrainingEntries) {
        this.numTrainingEntries = numTrainingEntries;
    }
    
    public String getFittedValuesPlotCode() {
        return fittedValuesPlotCode;
    }

    public void setFittedValuesPlotCode(String forecastPlot) {
        this.fittedValuesPlotCode = forecastPlot;
    }

    public double[] getFittedValues() {
        return fittedValues;
    }

    public void setFittedValues(double[] fittedValues) {
        this.fittedValues = fittedValues;
    }

    public double[] getForecastValues() {
        return forecastValues;
    }

    public void setForecastValues(double[] forecastValues) {
        this.forecastValues = forecastValues;
    }
}
