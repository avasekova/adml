package gui.tablemodels;

import models.TrainAndTestReport;
import utils.ErrorMeasuresInterval;

import java.util.List;

public class ErrorMeasuresTableModel_ITS extends ErrorMeasuresTableModel {
    
    public ErrorMeasuresTableModel_ITS(List<TrainAndTestReport> reports) {
        super(reports);
    }
    
    @Override
    public String[] getNamesOfSupportedErrorMeasures() {
        return ErrorMeasuresInterval.namesOfSupportedMeasures();
    }
}
