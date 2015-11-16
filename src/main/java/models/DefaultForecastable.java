package models;

import gui.tablemodels.DataTableModel;
import models.params.Params;

public class DefaultForecastable implements Forecastable {
    private static final long serialVersionUID = 1L;

    @Override
    public TrainAndTestReport forecast(DataTableModel dataTableModel, Params parameters) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public String getOptionalParams(Params parameters) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
