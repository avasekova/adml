package models;

import gui.tablemodels.DataTableModel;
import models.params.NeuralnetParams;
import models.params.Params;

public class Neuralnet implements Forecastable {

    @Override
    public TrainAndTestReportCrisp forecast(DataTableModel dataTableModel, Params parameters) {
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
