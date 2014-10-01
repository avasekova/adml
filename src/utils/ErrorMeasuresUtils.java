package utils;

import java.util.List;
import utils.imlp.Interval;

public class ErrorMeasuresUtils {
    
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
        return Math.sqrt(MSE(errors));
    }
    
    public static double RMSE(List<Double> realData, List<Double> forecastData) {
        return Math.sqrt(MSE(realData, forecastData));
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
    
    public static double MSE(List<Double> errors) {
        double sum = 0;
        
        for (double err : errors) {
            sum += Math.pow(err,2);
        }
        
        return sum/errors.size();
    }
    
    public static double MSE(List<Double> realData, List<Double> forecastData) {
        double sum = 0;
        
        for (int i = 0; i < realData.size(); i++) {
            double err = realData.get(i) - forecastData.get(i);
            sum += Math.pow(err,2);
        }
        
        return sum/realData.size();
    }
    
    public static double theilsU(List<Double> realData, List<Double> forecastData) {
        double numerator = 0;
        double denominator = 0;
        
        for (int i = 0; i < realData.size() - 1; i++) {
            double aux = (forecastData.get(i+1) - realData.get(i+1))/realData.get(i);
            numerator += Math.pow(aux,2);
        }
        
        for (int i = 0; i < realData.size() - 1; i++) {
            double aux = (realData.get(i+1) - realData.get(i))/realData.get(i);
            denominator += Math.pow(aux,2);
        }
        
        return Math.sqrt(numerator/denominator);
    }
    
    public static double coverage(Interval real, Interval forecast) {
        double widthReal = real.getUpperBound() - real.getLowerBound();
        return (widthIntersection(real, forecast) / widthReal) * 100;
        //vracia nulu, ak je forecast bodovy (ale aj ak lezi v realnom intervale) - co je ale feature, nie bug.
    }
    
    public static double efficiency(Interval real, Interval forecast) {
        //kvoli problemom s delenim nulou, ak je forecastInterval len cislo, tj radius nula:
        if (Utils.equalsDoubles(forecast.getUpperBound(), forecast.getLowerBound())) {
            if ((real.getLowerBound() <= forecast.getLowerBound()) &&
                (real.getUpperBound() >= forecast.getLowerBound())) { //takze forecast je obsiahnuty
                return 100;
            } else {
                return 0;
            }
        }
        
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
        if ((first.getUpperBound() <= second.getLowerBound()) ||
            (second.getUpperBound() <= first.getLowerBound())) { //non-overlapping intervals
            return 0;
        }
        
        else if ((first.getLowerBound() >= second.getLowerBound()) && (first.getUpperBound() <= second.getUpperBound())) {
            return first.getUpperBound() - first.getLowerBound(); //the width of the 1st, because it is contained in the 2nd
        }
        
        else if ((second.getLowerBound() >= first.getLowerBound()) && (second.getUpperBound() <= first.getUpperBound())) {
            return second.getUpperBound() - second.getLowerBound(); //the width of the 2nd, because it is contained in the 1st
        }
        
        else if ((first.getLowerBound() <= second.getLowerBound()) && (first.getUpperBound() <= second.getUpperBound())) {
            return first.getUpperBound() - second.getLowerBound(); //overlap, 1st "more to the left"
        }
        
        else { //(second.getLowerBound() <= first.getLowerBound()) && (second.getUpperBound() <= first.getUpperBound())
            return second.getUpperBound() - first.getLowerBound(); //overlap, 2nd "more to the left"
        }
    }
    
}
