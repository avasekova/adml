package utils;

import java.util.ArrayList;
import java.util.List;

public abstract class BestModelCriterionClassic {
    
    public enum MINIMIZE {
        ME, RMSE, MAE, MSE, THEILSU, RMSSE, MPE, MAPE, MASE
    }

    public enum MAXIMIZE {
        //nothing here so far
    }
    
    public static String[] getValues() {
        List<String> values = new ArrayList<>();
        
        for (BestModelCriterionClassic.MAXIMIZE c : BestModelCriterionClassic.MAXIMIZE.class.getEnumConstants()) {
            values.add(c.toString());
        }
        
        for (BestModelCriterionClassic.MINIMIZE c : BestModelCriterionClassic.MINIMIZE.class.getEnumConstants()) {
            values.add(c.toString());
        }
        
        return values.toArray(new String[]{});
    }
}
