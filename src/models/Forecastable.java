package models;

import java.util.List;
import params.Params;

public interface Forecastable {
    
    TrainAndTestReport forecast(List<Double> allData, Params parameters);
    String getOptionalParams(Params parameters);
    
}
