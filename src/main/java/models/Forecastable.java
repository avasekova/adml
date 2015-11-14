package models;

import gui.tablemodels.DataTableModel;
import models.params.Params;

public interface Forecastable {
    
    TrainAndTestReport forecast(DataTableModel dataTableModel, Params parameters);
    String getOptionalParams(Params parameters);
    
}
