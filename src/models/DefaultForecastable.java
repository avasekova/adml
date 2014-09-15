package models;

import java.util.List;
import params.Params;

public class DefaultForecastable implements Forecastable {

    @Override
    public TrainAndTestReport forecast(List<Double> allData, Params parameters) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public String getOptionalParams(Params parameters) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
    
}
