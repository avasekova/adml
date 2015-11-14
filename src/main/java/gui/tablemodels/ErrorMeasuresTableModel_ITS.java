package gui.tablemodels;

import java.util.List;
import models.TrainAndTestReport;
import utils.ErrorMeasuresInterval;

public class ErrorMeasuresTableModel_ITS extends ErrorMeasuresTableModel {
    
    public ErrorMeasuresTableModel_ITS(List<TrainAndTestReport> reports) {
        super(reports);
    }
    
    @Override
    public String[] getNamesOfSupportedErrorMeasures() {
        return ErrorMeasuresInterval.namesOfSupportedMeasures();
    }
}
