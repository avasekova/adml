package utils;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextField;
import rcaller.RCaller;

public class Utils {
    
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
    
    public static RCaller getCleanRCaller() {//TODO: zistit nejaky menej nechutny sposob, ako tam spustat viac veci v jednom.
        //ono to totiz uzavrie callera po runAndReturnResult a vyhadzuje IllegalThreadStateException...
        RCaller caller = new RCaller();
        caller.setRscriptExecutable(Const.RSCRIPT_EXE);
        
        return caller;
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
}
