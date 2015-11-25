package models;

import models.params.NeuralnetParams;
import models.params.Params;

import java.util.List;
import java.util.Map;

public class Neuralnet implements Forecastable {
    private static final long serialVersionUID = 1L;

    @Override
    public TrainAndTestReportCrisp forecast(Map<String, List<Double>> dataTableModel, Params parameters) {
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
