package gui.tablemodels;

import models.TrainAndTestReport;
import models.TrainAndTestReportCrisp;
import models.TrainAndTestReportInterval;
import utils.Utils;
import utils.imlp.Interval;

import java.util.ArrayList;
import java.util.List;

public class ForecastValsTableModel extends ReportsTableModel {

    private final int numForecasts;
    private final List<TrainAndTestReport> reports;
    private final List<TrainAndTestReport> hiddenReports = new ArrayList<>();
    private final List<String> columnNames;
    private final List<String> hiddenNames = new ArrayList<>();
    
    public ForecastValsTableModel(int numForecasts, List<TrainAndTestReport> reports) {
        this.numForecasts = numForecasts;
        this.reports = reports;
        List<String> colnames = new ArrayList<>();
        for (TrainAndTestReport r : reports) {
            colnames.add(r.toString());
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

    @Override
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
        if (reportNumber > -1) { //i.e. if this isn't a header
            hiddenReports.add(reports.get(reportNumber));
            reports.remove(reportNumber);
            hiddenNames.add(columnNames.get(reportNumber));
            columnNames.remove(reportNumber);
            this.fireTableStructureChanged();
        }
    }

    public void showAllHiddenColumns() {
        reports.addAll(hiddenReports);
        columnNames.addAll(hiddenNames);
        hiddenReports.clear();
        hiddenNames.clear();
        this.fireTableStructureChanged();
    }

    public void hideNoForecasts() {
        List<TrainAndTestReport> stashCols = new ArrayList<>();
        List<String> stashNames = new ArrayList<>();
        for (int i = 0; i < reports.size(); i++) {
            if (reports.get(i) instanceof TrainAndTestReportCrisp) {
                if (((TrainAndTestReportCrisp)reports.get(i)).getForecastValuesFuture().length == 0) {
                    stashCols.add(reports.get(i));
                    stashNames.add(columnNames.get(i));
                }
            } else if (reports.get(i) instanceof TrainAndTestReportInterval) {
                if (((TrainAndTestReportInterval)reports.get(i)).getForecastValuesFuture().isEmpty()) {
                    stashCols.add(reports.get(i));
                    stashNames.add(columnNames.get(i));
                }
            }
        }
        hiddenReports.addAll(stashCols);
        reports.removeAll(stashCols);
        hiddenNames.addAll(stashNames);
        columnNames.removeAll(stashNames);
        
        this.fireTableStructureChanged();
    }

    public void hideAllButAvg() {
        List<TrainAndTestReport> stashCols = new ArrayList<>();
        List<String> stashNames = new ArrayList<>();
        for (int i = 0; i < reports.size(); i++) {
            if (! reports.get(i).isAverage()) {
                stashCols.add(reports.get(i));
                stashNames.add(columnNames.get(i));
            }
        }
        hiddenReports.addAll(stashCols);
        reports.removeAll(stashCols);
        hiddenNames.addAll(stashNames);
        columnNames.removeAll(stashNames);
        
        this.fireTableStructureChanged();
    }
}
