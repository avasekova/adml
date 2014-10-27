package models;

import gui.DataTableModel;
import params.Params;

public interface ForecastableIntervals {
    
    TrainAndTestReport forecast(DataTableModel dataTableModel, Params parameters);
    
}
