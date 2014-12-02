package gui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import models.TrainAndTestReportCrisp;
import utils.ErrorMeasuresCrisp;
import utils.Utils;

public class ErrorMeasuresTableModel_CTS extends AbstractTableModel {
    
    private final List<TrainAndTestReportCrisp> reports;
    private final List<TrainAndTestReportCrisp> hiddenReports = new ArrayList<>();
    
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
                    //a stupid way to indent, but... whatever
                    return "   " + reports.get(rowIndex - 1).getModelName() + reports.get(rowIndex - 1).getModelDescription();
                } else { //rowIndex > reports.size() + 1
                    return "   " + reports.get(rowIndex - (reports.size() + 2)).getModelName() + reports.get(rowIndex - (reports.size() + 2)).getModelDescription();
                }
            }
        } else {
            if ((rowIndex == 0) || (rowIndex == reports.size() + 1)) {
                return ErrorMeasuresCrisp.namesOfSupportedMeasures()[columnIndex - 1];
            } else {
                if (rowIndex < reports.size() + 1) {
                    return Utils.valToDecPoints(reports.get(rowIndex - 1).getErrorMeasures().serializeToArray()[(columnIndex - 1)*2]);
                } else { //rowIndex > reports.size() + 1
                    return Utils.valToDecPoints(reports.get(rowIndex - (reports.size() + 2)).getErrorMeasures().serializeToArray()[(columnIndex - 1)*2 + 1]);
                }
            }
        }
    }
    
    public boolean isEmpty() {
        return (reports.isEmpty());
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        //TODO maybe later make them editable?
        return false;
    }
    
    public void hideRow(int rowIndex) {
        int reportNumber = getReportNumber(rowIndex);
        if (reportNumber > -1) { //teda ked to nie je nejaky header
            hiddenReports.add(reports.get(reportNumber));
            reports.remove(reportNumber);
            this.fireTableRowsDeleted(rowIndex, rowIndex);
        }
    }
    
    public void showAllHiddenRows() {
        reports.addAll(hiddenReports);
        hiddenReports.clear();
    }

    //translates selected row to report number because first row = header, then train reports, header again, test reports...
    private int getReportNumber(int selectedRow) {
        if ((selectedRow == 0) || (selectedRow == reports.size() + 1)) {
            return -1; //header
        } else if ((selectedRow >= 1) && (selectedRow <= reports.size())) {
            return selectedRow - 1; //train data
        } else /*if ((selectedRow >= reports.size() + 2) && (selectedRow <= reports.size()*2 + 1))*/ {
            return selectedRow - reports.size() - 2;
        }
    }
}
