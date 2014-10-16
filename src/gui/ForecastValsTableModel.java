package gui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import models.TrainAndTestReport;
import models.TrainAndTestReportCrisp;
import models.TrainAndTestReportInterval;
import utils.Utils;

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
                return Utils.valToDecPoints(((TrainAndTestReportCrisp) rep).getForecastValuesTest()[rowIndex]);
            } else { //instanceOf TTreportInterval
                return ((TrainAndTestReportInterval) rep).getForecastValuesTest().get(rowIndex).toString();
            }
        }
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        //TODO maybe later make them editable?
        return false;
    }
}
