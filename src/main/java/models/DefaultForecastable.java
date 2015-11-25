package models;

import gui.tablemodels.DataTableModel;
import models.params.Params;

import java.util.List;
import java.util.Map;

public class DefaultForecastable implements Forecastable {
    private static final long serialVersionUID = 1L;

    @Override
    public TrainAndTestReport forecast(Map<String, List<Double>> dataTableModel, Params parameters) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public String getOptionalParams(Params parameters) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
