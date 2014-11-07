package models;

import gui.DataTableModel;
import params.Params;

public interface Forecastable {
    
    TrainAndTestReport forecast(DataTableModel dataTableModel, Params parameters);
    String getOptionalParams(Params parameters);
    
}
