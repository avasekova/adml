package models;

import java.util.ArrayList;
import java.util.List;
import utils.Utils;
import utils.imlp.Interval;

public class TrainAndTestReportInterval extends TrainAndTestReport {
    
    private List<Interval> fittedValues;
    private List<Interval> forecastValues = new ArrayList<>();

    public TrainAndTestReportInterval(String modelName) {
        super(modelName);
    }
    
    public double[] getFittedValuesLowers() { //ciste pre plotovacie ucely!
        return getValuesLowers(fittedValues);
    }
    
    public double[] getFittedValuesUppers() { //ciste pre plotovacie ucely!
        return getValuesUppers(fittedValues);
    }
    
    public double[] getForecastValuesLowers() { //ciste pre plotovacie ucely!
        return getValuesLowers(forecastValues);
    }
    
    public double[] getForecastValuesUppers() { //ciste pre plotovacie ucely!
        return getValuesUppers(forecastValues);
    }

    public List<Interval> getFittedValues() {
        return fittedValues;
    }

    public void setFittedValues(List<Interval> fittedValues) {
        this.fittedValues = fittedValues;
    }

    public List<Interval> getForecastValues() {
        return forecastValues;
    }

    public void setForecastValues(List<Interval> forecastValues) {
        this.forecastValues = forecastValues;
    }
    
    private double[] getValuesLowers(List<Interval> baseData) {
        List<Double> lowers = new ArrayList<>();
        
        for (Interval i : baseData) {
            lowers.add(i.getLowerBound());
        }
        
        return Utils.listToArray(lowers);
    }
    
    private double[] getValuesUppers(List<Interval> baseData) {
        List<Double> uppers = new ArrayList<>();
        
        for (Interval i : baseData) {
            uppers.add(i.getUpperBound());
        }
        
        return Utils.listToArray(uppers);
    }
    
}
