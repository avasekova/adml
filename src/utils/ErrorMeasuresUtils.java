package utils;

import java.util.ArrayList;
import java.util.List;
import utils.imlp.Interval;
import utils.imlp.IntervalLowerUpper;

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
        int countNaN = 0;
        
        for (int i = 0; i < realData.size(); i++) {
            if (realData.get(i).isNaN() || forecastData.get(i).isNaN()) {
                countNaN++;
            } else {
                double err = realData.get(i) - forecastData.get(i);
                sum += Math.pow(err,2);
            }
        }
        
        return sum/(realData.size() - countNaN);
    }
    
    public static double theilsU(List<Double> rData, List<Double> fData) {
        //no idea why it throws ConcurrentModificationE if I do not do this :/
        List<Double> realData = new ArrayList<>(rData);
        List<Double> forecastData = new ArrayList<>(fData);
        double numerator = 0;
        double denominator = 0;
        
        //najprv zmazat vsetky NaN
        List<Double> toDeleteReal = new ArrayList<>();
        List<Double> toDelete4cast = new ArrayList<>();
        for (int i = 0; i < realData.size(); i++) {
            if (realData.get(i).isNaN() || forecastData.get(i).isNaN()) {
                toDeleteReal.add(realData.get(i));
                toDelete4cast.add(forecastData.get(i));
            }
        }
        
        realData.removeAll(toDeleteReal);
        forecastData.removeAll(toDelete4cast);
        
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
        if (widthReal == 0) {
            if ((real.getUpperBound() < forecast.getLowerBound()) || (real.getUpperBound() > forecast.getUpperBound())) {
                return 0; //ten bod je mimo
            } else {
                return 100; //ten bod je vnutri forecastu, i.e. 100% ho pokryva forecast
            }
        } else {
            return (widthIntersection(real, forecast) / widthReal) * 100;
        }
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
        
        for (int i = 0; i < forecastData.size(); i++) {
            System.out.println(i + ": " + realData.get(i) + ", " + forecastData.get(i) + " = " + coverage(realData.get(i), forecastData.get(i)));
            mean += coverage(realData.get(i), forecastData.get(i));
        }
        
        System.out.println("mean: " + mean + ", /size = " + (mean/forecastData.size()));
        
        return mean/forecastData.size();
    }
    
    public static double meanEfficiency(List<Interval> realData, List<Interval> forecastData) {
        double mean = 0;
        
        for (int i = 0; i < forecastData.size(); i++) {
            mean += efficiency(realData.get(i), forecastData.get(i));
        }
        
        return mean/forecastData.size();
    }
    
    public static double widthIntersection(Interval first, Interval second) {
        Interval lefter;
        Interval righter;
        //first make sure that first is indeed more to the left or at the same point as second (if necessary, swap them):
        if (first.getLowerBound() > second.getLowerBound()) {
            lefter = new IntervalLowerUpper(second.getLowerBound(), second.getUpperBound());
            righter = new IntervalLowerUpper(first.getLowerBound(), first.getUpperBound());
        } else { //they are ok
            lefter = new IntervalLowerUpper(first.getLowerBound(), first.getUpperBound());
            righter = new IntervalLowerUpper(second.getLowerBound(), second.getUpperBound());
        }
        
        //now check if they overlap
        if (lefter.getUpperBound() <= righter.getLowerBound()) { //no overlap
            return 0;
        } else { //get the width of the overlap
            return width(righter.getLowerBound(), Math.min(lefter.getUpperBound(), righter.getUpperBound()));
        }
    }
    
    public static double widthUnion(Interval first, Interval second) {
        if ((first.getUpperBound() <= second.getLowerBound()) ||
            (second.getUpperBound() <= first.getLowerBound())) { //non-overlapping intervals
            return width(first) + width(second);
        }
        
        else {
            return width(first) + width(second) - widthIntersection(first, second);
        }
    }
    
    public static double width(double start, double end) {
        return Math.abs(end - start);
    }
    
    public static double width(Interval interval) {
        return width(interval.getLowerBound(), interval.getUpperBound());
    }
}
