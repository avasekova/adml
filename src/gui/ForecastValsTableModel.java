package gui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import models.TrainAndTestReport;
import models.TrainAndTestReportCrisp;
import models.TrainAndTestReportInterval;

public class ForecastValsTableModel extends AbstractTableModel {

    private final int numForecasts;
    private final List<TrainAndTestReport> reports;
    private final List<String> columnNames;
    
    public ForecastValsTableModel(int numForecasts, List<TrainAndTestReport> reports) {
        this.numForecasts = numForecasts;
        this.reports = reports;
        List<String> colnames = new ArrayList<>();
        for (TrainAndTestReport r : reports) {
            colnames.add(r.getModelName());
        }
        this.columnNames = colnames;
    }
    
    @Override
    public int getRowCount() {
        return numForecasts;
    }

    @Override
    public int getColumnCount() {
        return reports.size();
    }
    
    @Override
    public String getColumnName(int columnIndex) {
        return columnNames.get(columnIndex);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        TrainAndTestReport rep = reports.get(columnIndex);
        if (rep instanceof TrainAndTestReportCrisp) {
            return ((TrainAndTestReportCrisp) rep).getForecastValues()[rowIndex];
        } else { //instanceOf TTreportInterval
            return ((TrainAndTestReportInterval) rep).getForecastValues().get(rowIndex).toString();
        }
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        //TODO maybe later make them editable?
        return false;
    }
}
