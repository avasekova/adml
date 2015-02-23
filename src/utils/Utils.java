package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import static java.lang.Double.NaN;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JTextField;
import utils.imlp.dist.Distance;
import utils.imlp.Interval;
import utils.imlp.IntervalCentreRadius;
import utils.imlp.IntervalLowerUpper;

public class Utils {
    
    public static final int NUM_DECIMAL_POINTS = 6;
    public static final int REASONABLY_MANY_MODELS = 10;
    
    private static int counter = 0;
    private static int modelID = 0;
    private final static double EPSILON = 0.000000001;
    
    private static final double STEP = 0.01;
    
    public static int getModelID() {
        modelID++;
        return modelID;
    }
    
    public static void resetModelID() {
        modelID = 0;
    }
    
    public static boolean equalsDoubles(double value, double target) {
        return ((value >= target-EPSILON) && (value <= target+EPSILON));
    }
    
    public static List<Double> arrayToList(double[] array) {
        List<Double> listDouble = new ArrayList<>();
        for (double value : array) {
            listDouble.add(value);
        }
        return listDouble;
    }
    
    public static List<Double> matrixToList(double[][] matrix) {
        List<Double> listDouble = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) { //matrix.length gives size in 1 dimension
            for (int j = 0; j < matrix[0].length; j++) {
                listDouble.add(matrix[i][j]);
            }
        }
//        for (double[] line : matrix) {
//            for (double value : line) {
//                listDouble.add(value);
//            }
//        }
        
        return listDouble;
    }
    
    public static double[] listToArray(List<Double> list) {
        double[] arrayDouble = new double[list.size()];
        int i = 0;
        for (Double value : list) {
            arrayDouble[i] = value;
            i++;
        }
        return arrayDouble;
    }
    
    public static List<Integer> getIntegersOrDefault(JTextField textField) {
        return getIntegersOrDefault(textField.getText());
    }
    
    public static List<Integer> getIntegersOrDefault(String text) {
        List<Integer> list = new ArrayList<>();
        try {
            String[] split = text.split("\\.\\.\\.");
            if (split.length == 1) {
                list.add(Integer.parseInt(text));
            } else { //predpokladam vyraz v tvare LB...UB
                for (int i = Integer.parseInt(split[0]); i <= Integer.parseInt(split[1]); i++) { //bleee, to je ohavne
                    list.add(i);
                }
            }
            return list;
        } catch (NumberFormatException e) {
            //TODO log? resp. bude sa pouzivat defaultna hodnota
            list.add(null);
            return list;
        }
    }
    
    public static Double getDoubleOrDefault(JTextField textField) {
        try {
            return Double.parseDouble(textField.getText());
        } catch (NumberFormatException e) {
            //TODO log? resp. bude sa pouzivat defaultna hodnota
            return null;
        }
    }
    
    public static Double getDoubleOrDefault(String text) {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            //TODO log? resp. bude sa pouzivat defaultna hodnota
            return null;
        }
    }
    
    public static List<Double> getDoublesOrDefault(JTextField textField) {
        return getDoublesOrDefault(textField.getText());
    }
    
    public static List<Double> getDoublesOrDefault(String text) { //TODO refaktorovat trochu? (tie vynimky)
        List<Double> list = new ArrayList<>();
        try {
            String[] split = text.split("\\.\\.\\.");
            if (split.length == 1) {
                list.add(Double.parseDouble(text));
            } else { //predpokladam vyraz v tvare LB...UB
                try {
                    int lowerB = Integer.parseInt(split[0]);
                    int upperB = Integer.parseInt(split[1]);
                    //if it comes here, they are both integers:
                    for (int i = lowerB; i <= upperB; i++) {
                        list.add(new Double(i));
                    }
                } catch (NumberFormatException e) {
                    //ak to vyhodilo vynimku, aspon jedno z nich je double, takze beriem obe ako double a krokujem ich ako double
                    //tj krok 0.01 by default //TODO neskor dat vybrat uzivatelovi?
                    double lowerB = Double.parseDouble(split[0]);
                    double upperB = Double.parseDouble(split[1]);
                    
                    while (lowerB <= upperB) {
                        list.add(lowerB);
                        lowerB += STEP;
                    }
                }
            }
            return list;
        } catch (NumberFormatException e) {
            //TODO log? resp. bude sa pouzivat defaultna hodnota
            list.add(null);
            return list;
        }
    }
    
    public static R_Bool booleanToRBool(boolean truefalse) {
        if (truefalse) {
            return R_Bool.TRUE;
        } else {
            return R_Bool.FALSE;
        }
    }
    
    public static String booleanToHumanString(boolean truefalse) {
        if (truefalse) {
            return "yes";
        } else {
            return "no";
        }
    }
    
    public static String booleanToHumanString(R_Bool truefalse) {
        if (truefalse == R_Bool.TRUE) {
            return "yes";
        } else {
            return "no";
        }
    }
    
    public static double minArray(double[] array) {
        double min = NaN;
        for (int i = 0; i < array.length; i++) {
            if (Double.isNaN(min)) {
                min = array[i];
            } else {
                if (! Double.isNaN(Math.min(min, array[i]))) {
                    min = Math.min(min, array[i]);
                }
            }
        }
        
        return min;
    }
    
    public static double maxArray(double[] array) {
        double max = NaN;
        for (int i = 0; i < array.length; i++) {
            if (Double.isNaN(max)) {
                max = array[i];
            } else {
                if (! Double.isNaN(Math.max(max, array[i]))) {
                    max = Math.max(max, array[i]);
                }
            }
        }
        
        return max;
    }
    
    public static double minList(List<Double> list) {
        double min = list.get(0);
        
        for (Double item : list) {
            if (Double.isNaN(min)) {
                min = item;
            } else {
                if (!Double.isNaN(Math.min(min, item))) {
                    min = Math.min(min, item);
                }
            }
        }
        
        return min;
    }
    
    public static double maxList(List<Double> list) {
        double max = list.get(0);
        
        for (Double item : list) {
            if (Double.isNaN(max)) {
                max = item;
            } else {
                if (!Double.isNaN(Math.max(max, item))) {
                    max = Math.max(max, item);
                }
            }
        }
        
        return max;
    }
    
    public static int getCounter() {
        counter++;
        return counter;
    }
    
    public static List<Interval> getForecastsFromOutFile(File outFile) {
        List<Interval> forecasts = new ArrayList<>();
        
        while (! isCompletelyWritten(outFile)) {
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(outFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+"); //yields 2 parts - the predicted center and radius
                double centre = Double.parseDouble(parts[0]);
                double radius = Double.parseDouble(parts[1]);
                Interval interval = new IntervalCentreRadius(centre, radius);
                forecasts.add(interval);
            }
        } catch (IOException ex) {
            //Logger.getLogger(IntervalMLPCcode.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return forecasts;
    }
    
    public static List<Double> getErrors(List<Double> real, List<Double> forecasts) {
        if (real.size() != forecasts.size()) {
            throw new IndexOutOfBoundsException("real.size = " + real.size() + ", forecasts.size = " + forecasts.size());
        }
        
        List<Double> errors = new ArrayList<>();
        
        for (int i = 0; i < forecasts.size(); i++) {
            double error = real.get(i) - forecasts.get(i);
            errors.add(error);
        }
        
        return errors;
    }
    
    public static List<Double> getErrorsForIntervals(List<Interval> real, List<Interval> forecasts, Distance distanceMeasure) {
        if (real.size() != forecasts.size()) {
            throw new IndexOutOfBoundsException("real.size = " + real.size() + ", forecasts.size = " + forecasts.size());
        }
        
        List<Double> errors = new ArrayList<>();
        
        for (int i = 0; i < forecasts.size(); i++) {
            double error = distanceMeasure.getDistance(forecasts.get(i), real.get(i));
            errors.add(error);
        }
        
        return errors;
    }
    
    public static List<Interval> zipCentersRadiiToIntervals(List<Double> centers, List<Double> radii) {
        List<Interval> intervals = new ArrayList<>();
        
        int lastIndexCenters = centers.size() - 1;
        int lastIndexRadii = radii.size() - 1;
        
        //kvoli lagom to cele zarovna "doprava" a odreze predok, kde trci
        while ((lastIndexCenters >= 0) && (lastIndexRadii >= 0)) {
            Interval interval;
            if (centers.get(lastIndexCenters).isNaN() || radii.get(lastIndexRadii).isNaN()) {
                interval = new IntervalCentreRadius(NaN, NaN);
            } else {
                interval = new IntervalCentreRadius(centers.get(lastIndexCenters), radii.get(lastIndexRadii));
            }
            
            intervals.add(interval);
            lastIndexCenters--;
            lastIndexRadii--;
        }
        
        //teraz este doplnit tie NaN na zaciatok:
        for (int i = 0; i <= Math.max(lastIndexCenters, lastIndexRadii); i++) {
            intervals.add(new IntervalCentreRadius(NaN, NaN));
        }
        
        Collections.reverse(intervals);
        
        return intervals;
    }
    
    public static List<Interval> zipLowerUpperToIntervals(List<Double> lowers, List<Double> uppers) {
        List<Interval> intervals = new ArrayList<>();
        
        int lastIndexLowers = lowers.size() - 1;
        int lastIndexUppers = uppers.size() - 1;
        
        //kvoli lagom to cele zarovna "doprava" a odreze predok, kde trci
        while ((lastIndexLowers >= 0) && (lastIndexUppers >= 0)) {
            Interval interval = new IntervalLowerUpper(lowers.get(lastIndexLowers), uppers.get(lastIndexUppers));
            intervals.add(interval);
            lastIndexLowers--;
            lastIndexUppers--;
        }
        
        //teraz este doplnit tie NaN na zaciatok:
        for (int i = 0; i <= Math.max(lastIndexLowers, lastIndexUppers); i++) {
            intervals.add(new IntervalLowerUpper(NaN, NaN));
        }
        
        Collections.reverse(intervals);
        
        return intervals;
    }
    
    public static List<List<Double>> getFirstSecondFromIntervals(List<Interval> intervals) {
        List<Double> first = new ArrayList<>();
        List<Double> second = new ArrayList<>();
        
        for (Interval i : intervals) {
            if (i instanceof IntervalCentreRadius) {
                first.add(i.getCentre());
                second.add(i.getRadius());
            } else { //LB+UB
                first.add(i.getLowerBound());
                second.add(i.getUpperBound());
            }
        }
        
        List<List<Double>> resultEeew = new ArrayList<>();
        resultEeew.add(first);
        resultEeew.add(second);
        
        return resultEeew;
    }
    
    //riadne skareda, vrati v jednom dlhom liste najprv lower, potom upper, a treba ho sublistnut na prvu a druhu polku
    public static List<Double> getLowersUppersFromCentersRadii(List<Double> centers, List<Double> radii) {
        List<Double> lowers = new ArrayList<>();
        List<Double> uppers = new ArrayList<>();

        for (int i = 0; i < centers.size(); i++) {
            lowers.add(centers.get(i) - radii.get(i));
            uppers.add(centers.get(i) + radii.get(i));
        }
        
        lowers.addAll(uppers);
        
        return lowers;
    }
    
    //hack from http://stackoverflow.com/a/11242648
    private static boolean isCompletelyWritten(File file) {
        try (RandomAccessFile stream = new RandomAccessFile(file, "rw")) {
            return true;
        } catch (IOException e) {
            //TODO log
        }
        
        return false;
    }
    
    public static double valToDecPoints(double value) {
        return Math.round(value * (Math.pow(10, Utils.NUM_DECIMAL_POINTS)))/Math.pow(10, Utils.NUM_DECIMAL_POINTS);
    }

    public static String arrayToRVectorString(double[] array) {
        List<Double> list = Utils.arrayToList(array);
        
        return listToRVectorString(list);
    }
    
    public static String listToRVectorString(List<Double> list) {
        StringBuilder string = new StringBuilder(list.toString());
        
        string.replace(0, 1, "c(");
        string.replace(string.length()-1, string.length(), ")");
        
        return string.toString();
    }
    
    public static List<String> doublesToStringsEew(List<Double> doubles) {
        List<String> strings = new ArrayList<>();
        for (Double d : doubles) {
            strings.add("" + d);
        }
        
        return strings;
    }
}
