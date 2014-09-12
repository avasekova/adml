package models;

import java.util.List;
import java.util.Map;

public class Neuralnet implements Forecastable {

    @Override
    public TrainAndTestReport forecast(List<Double> allData, Map<String, Integer> params) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public String getOptionalParams(Map<String, Integer> params) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
    
}
