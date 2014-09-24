package models;

import java.util.List;
import params.Params;

public class IntervalMLPCcode implements Forecastable {

    @Override
    public TrainAndTestReport forecast(List<Double> allData, Params parameters) {
        throw new UnsupportedOperationException("Not supported for interval data.");
    }

    @Override
    public TrainAndTestReport forecastIntervalCenterRadius(List<Double> centerData, List<Double> radiusData, Params parameters) {
        return null;
    }

    @Override
    public TrainAndTestReport forecastIntervalLowerUpper(List<Double> lowerData, List<Double> upperData, Params parameters) {
        return null;
    }

    @Override
    public String getOptionalParams(Params parameters) {
        return null;
    }
    
}
