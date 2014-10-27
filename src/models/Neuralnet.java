package models;

import java.util.List;
import params.NeuralnetParams;
import params.Params;

public class Neuralnet implements Forecastable {

    @Override
    public TrainAndTestReportCrisp forecast(List<Double> allData, Params parameters) {
        TrainAndTestReportCrisp report = new TrainAndTestReportCrisp("neuralnet");
        
        return report;
    }

    @Override
    public String getOptionalParams(Params parameters) {
        NeuralnetParams params = (NeuralnetParams) parameters;
        StringBuilder optionalParams = new StringBuilder();
        
        return optionalParams.toString();
    }
}
