package utils;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextField;

public class FieldsParser {
    
    public static List<Integer> parseIntegers(String string) {
        List<Integer> list = new ArrayList<>();
        
        String[] ranges = string.split(",");
        
        for (String r : ranges) {
            List<Integer> integers = Utils.getIntegersOrDefault(r);
            if (! ((integers.size() == 1) && (integers.get(0) == null))) { //ak je tam nieco zmysluplne
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
    
}
