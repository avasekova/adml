package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.Double.NaN;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextField;
import models.IntervalMLPCcode;
import utils.imlp.Interval;
import utils.imlp.IntervalCentreRadius;

public class Utils {
    
    private static int counter = 0;
    
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
                System.out.println("[" + i + "," + j + "]: " + matrix[i][j]);
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
    
    public static Integer getIntegerOrDefault(JTextField textField) {
        try {
            return Integer.parseInt(textField.getText());
        } catch (NumberFormatException e) {
            //TODO log? resp. bude sa pouzivat defaultna hodnota
            return null;
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
    
    public static R_Bool booleanToRBool(boolean truefalse) {
        if (truefalse) {
            return R_Bool.TRUE;
        } else {
            return R_Bool.FALSE;
        }
    }
    
    public static double minArray(double[] array) {
        double min = NaN;
        for (int i = 0; i < array.length - 1; i++) {
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
        for (int i = 0; i < array.length - 1; i++) {
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
        
        for (int i = 0; i < list.size() - 1; i++) {
            if (Double.isNaN(min)) {
                min = list.get(i);
            } else {
                if (! Double.isNaN(Math.min(min, list.get(i)))) {
                    min = Math.min(min, list.get(i));
                }
            }
        }
        
        return min;
    }
    
    public static double maxList(List<Double> list) {
        double max = list.get(0);
        
        for (int i = 0; i < list.size() - 1; i++) {
            if (Double.isNaN(max)) {
                max = list.get(i);
            } else {
                if (! Double.isNaN(Math.max(max, list.get(i)))) {
                    max = Math.max(max, list.get(i));
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
        
        boolean success = false; //blee
        while (! success) {
            try (BufferedReader reader = new BufferedReader(new FileReader(outFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("\\s+"); //yields 2 parts - the predicted center and radius
                    double centre = Double.parseDouble(parts[0]);
                    double radius = Double.parseDouble(parts[1]);
                    Interval interval = new IntervalCentreRadius(centre, radius);
                    forecasts.add(interval);
                }
                System.out.println("here");
                success = true;
            } catch (IOException ex) {
                //Logger.getLogger(IntervalMLPCcode.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        System.out.println(forecasts);
        
        return forecasts;
    }
}
