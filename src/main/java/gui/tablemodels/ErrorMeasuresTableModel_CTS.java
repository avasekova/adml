package gui.tablemodels;

import models.TrainAndTestReport;
import utils.ErrorMeasuresCrisp;

import java.util.List;

public class ErrorMeasuresTableModel_CTS extends ErrorMeasuresTableModel {
    
    public ErrorMeasuresTableModel_CTS(List<TrainAndTestReport> reports) {
        super(reports);
    }
    
    @Override
    public String[] getNamesOfSupportedErrorMeasures() {
        return ErrorMeasuresCrisp.namesOfSupportedMeasures();
    }
}
