package utils;

import utils.imlp.Interval;
import utils.imlp.IntervalLowerUpper;
import utils.imlp.dist.Distance;

import java.util.ArrayList;
import java.util.List;

public class ErrorMeasuresUtils {
    
    public static double ME(List<Double> errors) {
        double sum = 0;
        int countNaN = 0;
        
        for (double e : errors) {
            if (Double.isNaN(e)) {
                countNaN++;
            } else {
                sum += e;
            }
        }
        
        return sum/(errors.size() - countNaN);
    }
    
    public static double ME(List<Double> realData, List<Double> forecastData) {
        double sum = 0;
        int countNaN = 0;
        
        for (int i = 0; i < forecastData.size(); i++) {
            if (realData.get(i).isNaN() || forecastData.get(i).isNaN()) {
                countNaN++;
            } else {
                double err = realData.get(i) - forecastData.get(i);
                sum += err;
            }
        }
        
        return sum/(forecastData.size() - countNaN);
    }
    
    public static double MAE(List<Double> errors) {
        double sum = 0;
        int countNaN = 0;
        
        for (double e : errors) {
            if (Double.isNaN(e)) {
                countNaN++;
            } else {
                sum += Math.abs(e);
            }
        }
        
        return sum/(errors.size() - countNaN);
    }
    
    public static double MAE(List<Double> realData, List<Double> forecastData) {
        double sum = 0;
        int countNaN = 0;
        
        for (int i = 0; i < forecastData.size(); i++) {
            if (realData.get(i).isNaN() || forecastData.get(i).isNaN()) {
                countNaN++;
            } else {
                double err = realData.get(i) - forecastData.get(i);
                sum += Math.abs(err);
            }
        }
        
        return sum/(forecastData.size() - countNaN);
    }
    
    public static double MPE(List<Double> realData, List<Double> forecastData) {
        double sum = 0;
        int countNaN = 0;
        
        for (int i = 0; i < forecastData.size(); i++) {
            if (realData.get(i).isNaN() || forecastData.get(i).isNaN()) {
                countNaN++;
            } else {
                double pe = ((realData.get(i) - forecastData.get(i))/realData.get(i))*100;
                sum += pe;
            }
        }
        
        return sum/(forecastData.size() - countNaN);
    }
    
    public static double MAPE(List<Double> realData, List<Double> forecastData) {
        double sum = 0;
        int countNaN = 0;
        
        for (int i = 0; i < forecastData.size(); i++) {
            if (realData.get(i).isNaN() || forecastData.get(i).isNaN()) {
                countNaN++;
            } else {
                double pe = ((realData.get(i) - forecastData.get(i))/realData.get(i))*100;
                sum += Math.abs(pe);
            }
        }
        
        return sum/(forecastData.size() - countNaN);
    }
    
    public static double RMSE(List<Double> errors) {
        return Math.sqrt(MSE(errors));
    }
    
    public static double RMSE(List<Double> realData, List<Double> forecastData) {
        return Math.sqrt(MSE(realData, forecastData));
    }
    
    public static double MASE(List<Double> realData, List<Double> forecastData) {
        double sumNaive = 0;
        for (int i = 1; i < forecastData.size(); i++) {
            sumNaive = Math.abs(realData.get(i) - realData.get(i-1));
        }
        double resultNaive = sumNaive/(forecastData.size() - 1);
        
        double sum = 0;
        int countNaN = 0;
        for (int i = 0; i < forecastData.size(); i++) {
            if (realData.get(i).isNaN() || forecastData.get(i).isNaN()) {
                countNaN++;
            } else {
                double err = realData.get(i) - forecastData.get(i);
                sum += Math.abs(err) / resultNaive;
            }
        }
        
        return sum/(forecastData.size() - countNaN);
    }
    
    public static double MSE(List<Double> errors) {
        double sum = 0;
        int countNaN = 0;
        
        for (double err : errors) {
            if (Double.isNaN(err)) {
                countNaN++;
            } else {
                sum += Math.pow(err,2);
            }
        }
        
        return sum/(errors.size() - countNaN);
    }
    
    public static double MSE(List<Double> realData, List<Double> forecastData) {
        double sum = 0;
        int countNaN = 0;
        
        for (int i = 0; i < forecastData.size(); i++) {
            if (realData.get(i).isNaN() || forecastData.get(i).isNaN()) {
                countNaN++;
            } else {
                double err = realData.get(i) - forecastData.get(i);
                sum += Math.pow(err,2);
            }
        }
        
        return sum/(forecastData.size() - countNaN);
    }
    
    public static double RMSSE(List<Double> rData, List<Double> fData, int seasonality) {
        List<Double> realData = new ArrayList<>();
        List<Double> forecastData = new ArrayList<>();
        
        //najprv "zmazat" vsetky NaN
        for (int i = 0; i < fData.size(); i++) {
            if (!(rData.get(i).isNaN()) && !(fData.get(i).isNaN())) {
                realData.add(rData.get(i));
                forecastData.add(fData.get(i));
            }
        }
        
        List<Double> errors = new ArrayList<>();
        
        //TODO revise the indices
        double denominator = 0;
        for (int i = (seasonality-1); i < forecastData.size(); i++) {
            double item = realData.get(i-(seasonality-1)) + ((realData.get(i-1) - realData.get(i-(seasonality-1))) 
                                                             - (realData.get(i-2) - realData.get(i-(seasonality-2))));
            
            denominator += Math.pow(item,2);
        }
        
        denominator /= (forecastData.size() - seasonality);
        
        for (int i = 0; i < forecastData.size(); i++) {
            double numerator = Math.pow(realData.get(i)-forecastData.get(i),2);
            errors.add(numerator/denominator);
        }
        
        double mean = 0;
        for (Double e : errors) {
            mean += e;
        }
        
        mean /= errors.size();
        
        return Math.sqrt(mean);
    }
    
    public static double theilsU(List<Double> rData, List<Double> fData) {
        List<Double> realData = new ArrayList<>();
        List<Double> forecastData = new ArrayList<>();
        double numerator = 0;
        double denominator = 0;
        
        //najprv "zmazat" vsetky NaN
        for (int i = 0; i < fData.size(); i++) {
            if (!(rData.get(i).isNaN() || fData.get(i).isNaN())) {
                realData.add(rData.get(i));
                forecastData.add(fData.get(i));
            }
        }
        
        for (int i = 0; i < forecastData.size() - 1; i++) {
            double aux = (forecastData.get(i+1) - realData.get(i+1))/realData.get(i);
            numerator += Math.pow(aux,2);
        }
        
        for (int i = 0; i < forecastData.size() - 1; i++) {
            double aux = (realData.get(i+1) - realData.get(i))/realData.get(i);
            denominator += Math.pow(aux,2);
        }
        
        return Math.sqrt(numerator/denominator);
    }
    
    public static double theilsUInterval(List<Interval> rData, List<Interval> fData) {
        List<Interval> realData = new ArrayList<>();
        List<Interval> forecastData = new ArrayList<>();
        double numerator = 0;
        double denominator = 0;
        
        //najprv "zmazat" vsetky NaN
        for (int i = 0; i < fData.size(); i++) {
            if (!(Double.isNaN(rData.get(i).getLowerBound()) ||
                Double.isNaN(rData.get(i).getUpperBound()) ||
                Double.isNaN(fData.get(i).getLowerBound()) ||
                Double.isNaN(fData.get(i).getUpperBound()))) {
                realData.add(rData.get(i));
                forecastData.add(fData.get(i));
            }
        }
        
        //idem po size-1, hoci oficialny vzorec kazal ist po size.
        //ale v oficialnom vzorci je furt j+1, takze by mi to vyliezlo z rozsahu
        for (int i = 1; i < forecastData.size(); i++) {
            Interval re = realData.get(i);
            Interval fore = forecastData.get(i);
            numerator += Math.pow(re.getUpperBound() - fore.getUpperBound(),2);
            numerator += Math.pow(re.getLowerBound() - fore.getLowerBound(),2);
            
            denominator += Math.pow(re.getUpperBound() - realData.get(i-1).getUpperBound(), 2);
            denominator += Math.pow(re.getLowerBound() - realData.get(i-1).getLowerBound(), 2);
        }
        
        return Math.sqrt(numerator/denominator);
    }
    
    public static double ARVinterval(List<Interval> rData, List<Interval> fData) {
        List<Interval> realData = new ArrayList<>();
        List<Interval> forecastData = new ArrayList<>();
        
        //najprv "zmazat" vsetky NaN
        for (int i = 0; i < fData.size(); i++) {
            if (!(Double.isNaN(rData.get(i).getLowerBound()) ||
                Double.isNaN(rData.get(i).getUpperBound()) ||
                Double.isNaN(fData.get(i).getLowerBound()) ||
                Double.isNaN(fData.get(i).getUpperBound()))) {
                realData.add(rData.get(i));
                forecastData.add(fData.get(i));
            }
        }
        
        //first get the average of upper and lower bounds:
        double avgLower = 0;
        double avgUpper = 0;
        for (Interval i : realData) {
            avgLower += i.getLowerBound();
            avgUpper += i.getUpperBound();
        }
        avgLower /= realData.size();
        avgUpper /= realData.size();
        
        //now compute the ARV
        double numerator = 0;
        double denominator = 0;
        for (int i = 0; i < forecastData.size(); i++) {
            Interval re = realData.get(i);
            Interval fore = forecastData.get(i);
            numerator += Math.pow(re.getUpperBound() - fore.getUpperBound(),2);
            numerator += Math.pow(re.getLowerBound() - fore.getLowerBound(),2);
            
            denominator += Math.pow(re.getUpperBound() - avgUpper, 2);
            denominator += Math.pow(re.getLowerBound() - avgLower, 2);
        }
        
        return Math.sqrt(numerator/denominator);
    }
    
    public static double coverage(Interval real, Interval forecast) {
        //NaN checks:
        if (Double.isNaN(real.getLowerBound()) ||
            Double.isNaN(real.getUpperBound()) ||
            Double.isNaN(forecast.getLowerBound()) ||
            Double.isNaN(forecast.getUpperBound())) {
            return -1;
        }
        
        double widthReal = real.getUpperBound() - real.getLowerBound();
        if (widthReal == 0) {
            if ((real.getUpperBound() < forecast.getLowerBound()) || (real.getLowerBound() > forecast.getUpperBound())) {
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
        //NaN checks:
        if (Double.isNaN(real.getLowerBound()) ||
            Double.isNaN(real.getUpperBound()) ||
            Double.isNaN(forecast.getLowerBound()) ||
            Double.isNaN(forecast.getUpperBound())) {
            return -1;
        }
        
        //kvoli problemom s delenim nulou, ak je forecastInterval len cislo, tj radius nula:
        if (forecast.getUpperBound() == forecast.getLowerBound()) {
            if ((real.getLowerBound() <= forecast.getLowerBound()) &&
                (real.getUpperBound() >= forecast.getUpperBound())) { //takze forecast je obsiahnuty
                return 100;
            } else {
                return 0;
            }
        }
        
        //realInterval len cislo, tj radius nula:
        double widthReal = real.getUpperBound() - real.getLowerBound();
        if (widthReal == 0) {
            return 0;
        }
        
        double widthForecast = forecast.getUpperBound() - forecast.getLowerBound();
        return (widthIntersection(real, forecast) / widthForecast) * 100;
    }
    
    public static double meanCoverage(List<Interval> realData, List<Interval> forecastData) {
        double mean = 0;
        int countNaN = 0;
        
        for (int i = 0; i < forecastData.size(); i++) {
            if (coverage(realData.get(i), forecastData.get(i)) == -1) {
                countNaN++;
            } else {
                mean += coverage(realData.get(i), forecastData.get(i));
            }
        }
        
        return mean/(forecastData.size() - countNaN);
    }
    
    public static double meanEfficiency(List<Interval> realData, List<Interval> forecastData) {
        double mean = 0;
        int countNaN = 0;
        
        for (int i = 0; i < forecastData.size(); i++) {
            if (efficiency(realData.get(i), forecastData.get(i)) == -1) {
                countNaN++;
            } else {
                mean += efficiency(realData.get(i), forecastData.get(i));
            }
        }
        
        return mean/(forecastData.size() - countNaN);
    }
    
    public static double widthIntersection(Interval firstMaybeWrong, Interval secondMaybeWrong) {
        //for some reason, and I have yet to figure out why, the forecast intervals may have LB > UB
        //so just to be sure, create new, correct ones:
        Interval first = new IntervalLowerUpper(firstMaybeWrong.getLowerBound(), firstMaybeWrong.getUpperBound());
        Interval second = new IntervalLowerUpper(secondMaybeWrong.getLowerBound(), secondMaybeWrong.getUpperBound());
        
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
        } else {
            //if one of them has width = 0, they do not overlap
            if ((lefter.getUpperBound() == lefter.getLowerBound()) || (righter.getUpperBound() == righter.getLowerBound())) {
                return 0;
            } else { //get the width of the overlap
                return width(righter.getLowerBound(), Math.min(lefter.getUpperBound(), righter.getUpperBound()));
            }
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
        return end - start;
    }
    
    public static double width(Interval interval) {
        return width(interval.getLowerBound(), interval.getUpperBound());
    }
    
    public static ErrorMeasuresCrisp computeAllErrorMeasuresCrisp(List<Double> realDataTrain, List<Double> realDataTest,
                                                                  List<Double> fittedTrain, List<Double> forecastsTest, 
                                                                  Integer seasonality) {
        ErrorMeasuresCrisp errorMeasures = new ErrorMeasuresCrisp();
        errorMeasures.setMEtrain(ME(realDataTrain, fittedTrain));
        errorMeasures.setMEtest(ME(realDataTest, forecastsTest));
        errorMeasures.setRMSEtrain(RMSE(realDataTrain, fittedTrain));
        errorMeasures.setRMSEtest(RMSE(realDataTest, forecastsTest));
        errorMeasures.setMAEtrain(MAE(realDataTrain, fittedTrain));
        errorMeasures.setMAEtest(MAE(realDataTest, forecastsTest));
        errorMeasures.setMPEtrain(MPE(realDataTrain, fittedTrain));
        errorMeasures.setMPEtest(MPE(realDataTest, forecastsTest));
        errorMeasures.setMAPEtrain(MAPE(realDataTrain, fittedTrain));
        errorMeasures.setMAPEtest(MAPE(realDataTest, forecastsTest));
        errorMeasures.setMASEtrain(MASE(realDataTrain, fittedTrain));
        errorMeasures.setMASEtest(MASE(realDataTest, forecastsTest));
        errorMeasures.setMSEtrain(MSE(realDataTrain, fittedTrain));
        errorMeasures.setMSEtest(MSE(realDataTest, forecastsTest));
        errorMeasures.setTheilUtrain(theilsU(realDataTrain, fittedTrain));
        errorMeasures.setTheilUtest(theilsU(realDataTest, forecastsTest));
        if (seasonality == 0) {
            errorMeasures.setRMSSEtrain(0);
            errorMeasures.setRMSSEtest(0);
        } else {
            errorMeasures.setRMSSEtrain(RMSSE(realDataTrain, fittedTrain, seasonality));
            errorMeasures.setRMSSEtest(RMSSE(realDataTest, forecastsTest, seasonality));
        }
        
        return errorMeasures;
    }
    
    public static ErrorMeasuresInterval computeAllErrorMeasuresInterval(List<Interval> realDataTrain, List<Interval> realDataTest,
                                                                        List<Interval> fittedTrain, List<Interval> forecastsTest,
                                                                        Distance distance, Integer seasonality) {
        ErrorMeasuresInterval errorMeasures = new ErrorMeasuresInterval(distance);
        
        List<Double> errorsTrain = Utils.getErrorsForIntervals(realDataTrain, fittedTrain, distance);
        List<Double> errorsTest = Utils.getErrorsForIntervals(realDataTest, forecastsTest, distance);
        
        errorMeasures.setMDEtrain(ErrorMeasuresUtils.ME(errorsTrain));
        errorMeasures.setMDEtest(ErrorMeasuresUtils.ME(errorsTest));
        errorMeasures.setRMSEtrain(ErrorMeasuresUtils.RMSE(errorsTrain));
        errorMeasures.setRMSEtest(ErrorMeasuresUtils.RMSE(errorsTest));
        errorMeasures.setMSEtrain(ErrorMeasuresUtils.MSE(errorsTrain));
        errorMeasures.setMSEtest(ErrorMeasuresUtils.MSE(errorsTest));
        errorMeasures.setMeanCoverageTrain(meanCoverage(realDataTrain, fittedTrain));
        errorMeasures.setMeanCoverageTest(meanCoverage(realDataTest, forecastsTest));
        errorMeasures.setMeanEfficiencyTrain(meanEfficiency(realDataTrain, fittedTrain));
        errorMeasures.setMeanEfficiencyTest(meanEfficiency(realDataTest, forecastsTest));
        errorMeasures.setTheilsUintervalTrain(theilsUInterval(realDataTrain, fittedTrain));
        errorMeasures.setTheilsUintervalTest(theilsUInterval(realDataTest, forecastsTest));
        errorMeasures.setArvIntervalTrain(ARVinterval(realDataTrain, fittedTrain));
        errorMeasures.setArvIntervalTest(ARVinterval(realDataTest, forecastsTest));
        List<List<Double>> splitRealDataTrain = Utils.getFirstSecondFromIntervals(realDataTrain);
        List<List<Double>> splitRealDataTest = Utils.getFirstSecondFromIntervals(realDataTest);
        List<List<Double>> splitFitTrain = Utils.getFirstSecondFromIntervals(fittedTrain);
        List<List<Double>> splitForecastTest = Utils.getFirstSecondFromIntervals(forecastsTest);
        if (seasonality == 0) {
            errorMeasures.setRMSSEtrain_1(0);
            errorMeasures.setRMSSEtrain_2(0);
            errorMeasures.setRMSSEtest_1(0);
            errorMeasures.setRMSSEtest_2(0);
        } else {
            errorMeasures.setRMSSEtrain_1(RMSSE(splitRealDataTrain.get(0), splitFitTrain.get(0), seasonality));
            errorMeasures.setRMSSEtrain_2(RMSSE(splitRealDataTrain.get(1), splitFitTrain.get(1), seasonality));
            errorMeasures.setRMSSEtest_1(RMSSE(splitRealDataTest.get(0), splitForecastTest.get(0), seasonality));
            errorMeasures.setRMSSEtest_2(RMSSE(splitRealDataTest.get(1), splitForecastTest.get(1), seasonality));
        }
        
        return errorMeasures;
    }
    
}
