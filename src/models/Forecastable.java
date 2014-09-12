package models;

import java.util.List;
import java.util.Map;

public interface Forecastable {
    
    TrainAndTestReport forecast(List<Double> allData, Map<String, Integer> params);
    String getOptionalParams(Map<String, Integer> params);
    
}
