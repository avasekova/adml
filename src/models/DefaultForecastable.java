package models;

import gui.tablemodels.DataTableModel;
import params.Params;

public class DefaultForecastable implements Forecastable {

    @Override
    public TrainAndTestReport forecast(DataTableModel dataTableModel, Params parameters) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public String getOptionalParams(Params parameters) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
