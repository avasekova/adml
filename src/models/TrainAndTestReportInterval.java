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
        super(modelName);
    }
    
    public double[] getFittedValuesLowers() { //ciste pre plotovacie ucely!
        return getValuesLowers(fittedValues);
    }
    
    public double[] getFittedValuesUppers() { //ciste pre plotovacie ucely!
        return getValuesUppers(fittedValues);
    }
    
    public double[] getForecastValuesTestLowers() { //ciste pre plotovacie ucely!
        return getValuesLowers(forecastValuesTest);
    }
    
    public double[] getForecastValuesTestUppers() { //ciste pre plotovacie ucely!
        return getValuesUppers(forecastValuesTest);
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

    public double[] getForecastValuesFutureLowers() { //ciste pre plotovacie ucely!
        return getValuesLowers(forecastValuesFuture);
    }
    
    public double[] getForecastValuesFutureUppers() { //ciste pre plotovacie ucely!
        return getValuesUppers(forecastValuesFuture);
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
    
}
