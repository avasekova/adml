package gui.tablemodels;

import models.TrainAndTestReportCrisp;
import utils.imlp.IntervalLowerUpper;

import java.util.ArrayList;
import java.util.List;

public class PredictionIntsTableModel extends ReportsTableModel {

    private final int numForecastsTestAndFuture;
    private final List<TrainAndTestReportCrisp> reports;
    private final List<String> columnNames;
    
    public PredictionIntsTableModel(List<TrainAndTestReportCrisp> reports) {
        int num4castsTestAndFutureMax = 0;
        this.reports = reports;
        List<String> colnames = new ArrayList<>();
        for (TrainAndTestReportCrisp r : reports) {
            colnames.add(r.toString());
            num4castsTestAndFutureMax = Math.max(num4castsTestAndFutureMax, r.getPredictionIntervalsLowers().length);
        }
        this.columnNames = colnames;
        this.numForecastsTestAndFuture = num4castsTestAndFutureMax;
    }
    
    @Override
    public int getRowCount() {
        return numForecastsTestAndFuture;
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
            TrainAndTestReportCrisp rep = reports.get(columnIndex - 1);
            
            if (rep.getPredictionIntervalsLowers().length < rowIndex+1) {
                return "        -        ";
            } else {
                return new IntervalLowerUpper(rep.getPredictionIntervalsLowers()[rowIndex], 
                                              rep.getPredictionIntervalsUppers()[rowIndex]);
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
}
