package models;

import models.params.Params;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface Forecastable extends Serializable {
    
    TrainAndTestReport forecast(Map<String, List<Double>> data, Params parameters);
    String getOptionalParams(Params parameters);

}
