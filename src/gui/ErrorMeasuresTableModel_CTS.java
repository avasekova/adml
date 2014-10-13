package gui;

import java.util.List;
import java.util.Locale;
import javax.swing.table.AbstractTableModel;
import models.TrainAndTestReportCrisp;
import utils.ErrorMeasuresCrisp;

public class ErrorMeasuresTableModel_CTS extends AbstractTableModel {
    
    private final List<TrainAndTestReportCrisp> reports;
    
    public ErrorMeasuresTableModel_CTS(List<TrainAndTestReportCrisp> reports) {
        this.reports = reports;
    }

    @Override
    public int getRowCount() {
        return (reports.size() * 2) + 2;
    }

    @Override
    public int getColumnCount() {
        return ErrorMeasuresCrisp.numberOfSupportedMeasures() + 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            if (rowIndex == 0) {
                return "TRAIN";
            } else if (rowIndex == reports.size() + 1) {
                return "TEST";
            } else {
                if (rowIndex < reports.size() + 1) {
                    return "   " + reports.get(rowIndex - 1).getModelName(); //a stupid way to indent, but... whatever
                } else { //rowIndex > reports.size() + 1
                    return "   " + reports.get(rowIndex - (reports.size() + 2)).getModelName();
                }
            }
        } else {
            if ((rowIndex == 0) || (rowIndex == reports.size() + 1)) {
                return ErrorMeasuresCrisp.namesOfSupportedMeasures()[columnIndex - 1];
            } else {
                if (rowIndex < reports.size() + 1) {
                    return String.format(Locale.UK, "%.2f", reports.get(rowIndex - 1).getErrorMeasures().serializeToArray()[(columnIndex - 1)*2]);
                } else { //rowIndex > reports.size() + 1
                    return String.format(Locale.UK, "%.2f", reports.get(rowIndex - (reports.size() + 2)).getErrorMeasures().serializeToArray()[(columnIndex - 1)*2 + 1]);
                }
            }
        }
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        //TODO maybe later make them editable?
        return false;
    }
    
}
