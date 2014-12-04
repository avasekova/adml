package models;

import java.util.ArrayList;
import java.util.List;
import utils.Utils;
import utils.imlp.Interval;

public class TrainAndTestReportInterval extends TrainAndTestReport {
    
    private List<Interval> fittedValues = new ArrayList<>();
    private List<Interval> forecastValuesTest = new ArrayList<>();
    private List<Interval> forecastValuesFuture = new ArrayList<>();
    private List<Double> realValuesLowers = new ArrayList<>();
    private List<Double> realValuesUppers = new ArrayList<>();
    
    public TrainAndTestReportInterval(String modelName) {
        this(modelName, true);
    }
    
    public TrainAndTestReportInterval(String modelName, boolean canBeInvisible) {
        super(modelName, canBeInvisible);
    }
    
    public double[] getFittedValuesLowers() {
        return getValuesLowers(fittedValues);
    }
    
    public double[] getFittedValuesUppers() {
        return getValuesUppers(fittedValues);
    }
    
    public double[] getFittedValuesCenters() {
        return getValuesCenters(fittedValues);
    }
    
    public double[] getFittedValuesRadii() {
        return getValuesRadii(fittedValues);
    }
    
    public double[] getForecastValuesTestLowers() {
        return getValuesLowers(forecastValuesTest);
    }
    
    public double[] getForecastValuesTestUppers() {
        return getValuesUppers(forecastValuesTest);
    }
    
    public double[] getForecastValuesTestCenters() {
        return getValuesCenters(forecastValuesTest);
    }
    
    public double[] getForecastValuesTestRadii() {
        return getValuesRadii(forecastValuesTest);
    }
    
    public List<Interval> getFittedValues() {
        return fittedValues;
    }

    public void setFittedValues(List<Interval> fittedValues) {
        this.fittedValues = fittedValues;
    }

    public List<Interval> getForecastValuesTest() {
        return forecastValuesTest;
    }

    public void setForecastValuesTest(List<Interval> forecastValuesTest) {
        this.forecastValuesTest = forecastValuesTest;
    }

    public List<Interval> getForecastValuesFuture() {
        return forecastValuesFuture;
    }

    public void setForecastValuesFuture(List<Interval> forecastValuesFuture) {
        this.forecastValuesFuture = forecastValuesFuture;
    }

    public double[] getForecastValuesFutureLowers() {
        return getValuesLowers(forecastValuesFuture);
    }
    
    public double[] getForecastValuesFutureUppers() {
        return getValuesUppers(forecastValuesFuture);
    }
    
    public double[] getForecastValuesFutureCenters() {
        return getValuesCenters(forecastValuesFuture);
    }
    
    public double[] getForecastValuesFutureRadii() {
        return getValuesRadii(forecastValuesFuture);
    }
    
    public List<Double> getRealValuesLowers() {
        return realValuesLowers;
    }
    
    public List<Double> getRealValuesUppers() {
        return realValuesUppers;
    }

    public void setRealValues(List<Double> realValuesCenters, List<Double> realValuesRadii) {
        List<Double> lowersUppers = Utils.getLowersUppersFromCentersRadii(realValuesCenters, realValuesRadii);
        this.realValuesLowers = lowersUppers.subList(0, lowersUppers.size()/2);
        this.realValuesUppers = lowersUppers.subList(lowersUppers.size()/2, lowersUppers.size());
    }
    
    public void setRealValues(List<Interval> realValues) {
        this.realValuesLowers = Utils.arrayToList(getValuesLowers(realValues));
        this.realValuesUppers = Utils.arrayToList(getValuesUppers(realValues));
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
    
    private double[] getValuesCenters(List<Interval> baseData) {
        List<Double> centers = new ArrayList<>();
        
        for (Interval i : baseData) {
            centers.add(i.getCentre());
        }
        
        return Utils.listToArray(centers);
    }
    
    private double[] getValuesRadii(List<Interval> baseData) {
        List<Double> radii = new ArrayList<>();
        
        for (Interval i : baseData) {
            radii.add(i.getRadius());
        }
        
        return Utils.listToArray(radii);
    }
}
