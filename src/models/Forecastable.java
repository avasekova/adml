package models;

import java.util.List;
import params.Params;

public interface Forecastable {
    
    TrainAndTestReport forecast(List<Double> allData, Params parameters);
    TrainAndTestReport forecastIntervalCenterRadius(List<Double> centerData, List<Double> radiusData, Params parameters);
    TrainAndTestReport forecastIntervalLowerUpper(List<Double> lowerData, List<Double> upperData, Params parameters);
    String getOptionalParams(Params parameters);
    
}
