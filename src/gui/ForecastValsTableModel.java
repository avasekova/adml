package gui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import models.TrainAndTestReport;
import models.TrainAndTestReportCrisp;
import models.TrainAndTestReportInterval;
import utils.Utils;
import utils.imlp.Interval;

public class ForecastValsTableModel extends AbstractTableModel {

    private final int numForecasts;
    private final List<TrainAndTestReport> reports;
    private final List<TrainAndTestReport> hiddenReports = new ArrayList<>();
    private final List<String> columnNames;
    
    public ForecastValsTableModel(int numForecasts, List<TrainAndTestReport> reports) {
        this.numForecasts = numForecasts;
        this.reports = reports;
        List<String> colnames = new ArrayList<>();
        for (TrainAndTestReport r : reports) {
            colnames.add(r.getModelName() + r.getModelDescription());
        }
        this.columnNames = colnames;
    }
    
    @Override
    public int getRowCount() {
        return numForecasts;
    }

    @Override
    public int getColumnCount() {
        return reports.size() + 1;
    }
    
    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex == 0) {
            return "";
        } else {
            return columnNames.get(columnIndex - 1);
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return rowIndex + 1;
        } else {
            TrainAndTestReport rep = reports.get(columnIndex - 1);
            if (rep instanceof TrainAndTestReportCrisp) {
                double[] forecastsFuture = ((TrainAndTestReportCrisp) rep).getForecastValuesFuture();
                if (forecastsFuture.length < rowIndex+1) {
                    return "<not available>";
                } else {
                    return Utils.valToDecPoints(forecastsFuture[rowIndex]);
                }
            } else { //instanceOf TTreportInterval
                List<Interval> forecastsFuture = ((TrainAndTestReportInterval) rep).getForecastValuesFuture();
                if (forecastsFuture.size() < rowIndex+1) {
                    return "<not available>";
                } else {
                    return forecastsFuture.get(rowIndex).toString();
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
    
    private int getReportNumber(int selectedCol) {
        if ((selectedCol == 0)) {
            return -1; //line numbers
        } else {
            return selectedCol - 1;
        }
    }

    public void hideColumn(int selectedCol) {
        int reportNumber = getReportNumber(selectedCol);
        if (reportNumber > -1) { //teda ked to nie je nejaky header
            hiddenReports.add(reports.get(reportNumber));
            reports.remove(reportNumber);
            columnNames.remove(reportNumber);
            this.fireTableStructureChanged();
        }
    }
}
