package models;

import java.util.List;
import params.NeuralnetParams;
import params.Params;

public class Neuralnet implements Forecastable {

    @Override
    public TrainAndTestReport forecast(List<Double> allData, Params parameters) {
        TrainAndTestReport report = new TrainAndTestReport("neuralnet");
        
        return report;
    }

    @Override
    public String getOptionalParams(Params parameters) {
        NeuralnetParams params = (NeuralnetParams) parameters;
        StringBuilder optionalParams = new StringBuilder();
        
        return optionalParams.toString();
    }

    @Override
    public TrainAndTestReport forecastIntervalCenterRadius(List<Double> centerData, List<Double> radiusData, Params parameters) {
        throw new UnsupportedOperationException("Not supported for crisp data.");
    }

    @Override
    public TrainAndTestReport forecastIntervalLowerUpper(List<Double> lowerData, List<Double> upperData, Params parameters) {
        throw new UnsupportedOperationException("Not supported for crisp data.");
    }
    
}
