package models;

import gui.tablemodels.DataTableModel;
import models.params.Params;

import java.io.Serializable;

public interface Forecastable extends Serializable {
    
    TrainAndTestReport forecast(DataTableModel dataTableModel, Params parameters);
    String getOptionalParams(Params parameters);
    
}
