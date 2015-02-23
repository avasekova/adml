package gui.tablemodels;

import java.util.List;
import models.TrainAndTestReport;
import utils.ErrorMeasuresCrisp;

public class ErrorMeasuresTableModel_CTS extends ErrorMeasuresTableModel {
    
    public ErrorMeasuresTableModel_CTS(List<TrainAndTestReport> reports) {
        super(reports);
    }
    
    @Override
    public String[] getNamesOfSupportedErrorMeasures() {
        return ErrorMeasuresCrisp.namesOfSupportedMeasures();
    }
}
