package utils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class FieldsParser {
    
    public static List<Integer> parseIntegers(String string) {
        List<Integer> list = new ArrayList<>();
        
        String[] ranges = string.split(",");
        
        for (String r : ranges) {
            List<Integer> integers = Utils.getIntegersOrDefault(r);
            if (! ((integers.size() == 1) && (integers.get(0) == null))) { //if it makes sense
                list.addAll(integers);
            }
        }
        
        if (list.isEmpty()) {
            list.add(null);
        }
        
        return list;
    }
    
    public static List<Integer> parseIntegers(JTextField textField) {
        return parseIntegers(textField.getText());
    }
    
    public static List<Double> parseDoubles(JTextField textField) {
        return parseDoubles(textField.getText());
    }
    
    public static List<Double> parseDoubles(String string) {
        List<Double> list = new ArrayList<>();
        
        String[] ranges = string.split(",");
        
        for (String r : ranges) {
            List<Double> doubles = Utils.getDoublesOrDefault(r);
            if (! ((doubles.size() == 1) && (doubles.get(0) == null))) {
                list.addAll(doubles);
            }
        }
        
        if (list.isEmpty()) {
            list.add(null);
        }
        
        return list;
    }
    
}
