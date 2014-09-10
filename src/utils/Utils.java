package utils;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    
    public static final String RSCRIPT_EXE = "C:\\Program Files\\R\\R-3.1.0\\bin\\x64\\Rscript.exe";
    
    public static List<Double> arrayToList(double[] array) {
        List<Double> listDouble = new ArrayList<>();
        for (double value : array) {
            listDouble.add(value);
        }
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
}
