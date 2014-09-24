package utils;

import java.util.List;

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
    
}
