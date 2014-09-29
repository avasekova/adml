package utils;

import java.util.List;
import utils.imlp.Interval;

public class ErrorMeasures {
    
    public static double ME(List<Double> errors) {
        double sum = 0;
        
        for (double e : errors) {
            sum += e;
        }
        
        return sum/errors.size();
    }
    
    public static double ME(List<Double> realData, List<Double> forecastData) {
        double sum = 0;
        
        for (int i = 0; i < realData.size(); i++) {
            double err = realData.get(i) - forecastData.get(i);
            sum += err;
        }
        
        return sum/realData.size();
    }
    
    public static double MAE(List<Double> errors) {
        double sum = 0;
        
        for (double e : errors) {
            sum += Math.abs(e);
        }
        
        return sum/errors.size();
    }
    
    public static double MAE(List<Double> realData, List<Double> forecastData) {
        double sum = 0;
        
        for (int i = 0; i < realData.size(); i++) {
            double err = realData.get(i) - forecastData.get(i);
            sum += Math.abs(err);
        }
        
        return sum/realData.size();
    }
    
    public static double MPE(List<Double> realData, List<Double> forecastData) {
        double sum = 0;
        
        for (int i = 0; i < realData.size(); i++) {
            double pe = ((realData.get(i) - forecastData.get(i))/realData.get(i))*100;
            sum += pe;
        }
        
        return sum/realData.size();
    }
    
    public static double MAPE(List<Double> realData, List<Double> forecastData) {
        double sum = 0;
        
        for (int i = 0; i < realData.size(); i++) {
            double pe = ((realData.get(i) - forecastData.get(i))/realData.get(i))*100;
            sum += Math.abs(pe);
        }
        
        return sum/realData.size();
    }
    
    public static double RMSE(List<Double> errors) {
        double sum = 0;
        
        for (double err : errors) {
            sum += Math.pow(err,2);
        }
        
        return Math.sqrt(sum/errors.size());
    }
    
    public static double RMSE(List<Double> realData, List<Double> forecastData) {
        double sum = 0;
        
        for (int i = 0; i < realData.size(); i++) {
            double err = realData.get(i) - forecastData.get(i);
            sum += Math.pow(err,2);
        }
        
        return Math.sqrt(sum/realData.size());
    }
    
    public static double MASE(List<Double> realData, List<Double> forecastData) {
        double sumNaive = 0;
        for (int i = 1; i < realData.size(); i++) {
            sumNaive = Math.abs(realData.get(i) - realData.get(i-1));
        }
        double resultNaive = sumNaive/(realData.size() - 1);
        
        double sum = 0;
        
        for (int i = 0; i < realData.size(); i++) {
            double err = realData.get(i) - forecastData.get(i);
            sum += Math.abs(err) / resultNaive;
        }
        
        return sum/realData.size();
    }
    
    public static double coverage(Interval real, Interval forecast) {
        double widthReal = real.getUpperBound() - real.getLowerBound();
        return (widthIntersection(real, forecast) / widthReal) * 100;
    }
    
    public static double efficiency(Interval real, Interval forecast) {
        double widthForecast = forecast.getUpperBound() - forecast.getLowerBound();
        return (widthIntersection(real, forecast) / widthForecast) * 100;
    }
    
    public static double meanCoverage(List<Interval> realData, List<Interval> forecastData) {
        double mean = 0;
        
        for (int i = 0; i < realData.size(); i++) {
            mean += coverage(realData.get(i), forecastData.get(i));
        }
        
        return mean/realData.size();
    }
    
    public static double meanEfficiency(List<Interval> realData, List<Interval> forecastData) {
        double mean = 0;
        
        for (int i = 0; i < realData.size(); i++) {
            mean += efficiency(realData.get(i), forecastData.get(i));
        }
        
        return mean/realData.size();
    }
    
    private static double widthIntersection(Interval first, Interval second) {
        System.out.print("intersection width for " + first.toString() + " and " + second.toString() + " = ");
        
        if ((first.getUpperBound() <= second.getLowerBound()) ||
            (second.getUpperBound() <= first.getLowerBound())) { //non-overlapping intervals
            System.out.println("0");
            return 0;
        }
        
        else if ((first.getLowerBound() >= second.getLowerBound()) && (first.getUpperBound() <= second.getUpperBound())) {
            System.out.println((first.getUpperBound() - first.getLowerBound()));
            return first.getUpperBound() - first.getLowerBound(); //the width of the 1st, because it is contained in the 2nd
        }
        
        else if ((second.getLowerBound() >= first.getLowerBound()) && (second.getUpperBound() <= first.getUpperBound())) {
            System.out.println((second.getUpperBound() - second.getLowerBound()));
            return second.getUpperBound() - second.getLowerBound(); //the width of the 2nd, because it is contained in the 1st
        }
        
        else if ((first.getLowerBound() <= second.getLowerBound()) && (first.getUpperBound() <= second.getUpperBound())) {
            System.out.println((first.getUpperBound() - second.getLowerBound()));
            return first.getUpperBound() - second.getLowerBound(); //overlap, 1st "more to the left"
        }
        
        else { //(second.getLowerBound() <= first.getLowerBound()) && (second.getUpperBound() <= first.getUpperBound())
            System.out.println((second.getUpperBound() - first.getLowerBound()));
            return second.getUpperBound() - first.getLowerBound(); //overlap, 2nd "more to the left"
        }
    }
    
}
