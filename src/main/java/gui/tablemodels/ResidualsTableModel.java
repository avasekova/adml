package gui.tablemodels;

import models.TrainAndTestReport;
import models.TrainAndTestReportCrisp;
import models.TrainAndTestReportInterval;
import utils.ErrorMeasuresInterval;
import utils.Utils;
import utils.imlp.Interval;

import java.util.*;

public class ResidualsTableModel extends ReportsTableModel {
    
    private int maxRows;
    private final Map<String, List<Double>> residuals = new LinkedHashMap<>();
    
    public ResidualsTableModel(List<TrainAndTestReport> reports) {
        for (TrainAndTestReport r : reports) {
            if (r instanceof TrainAndTestReportCrisp) {
                int rows = ((TrainAndTestReportCrisp)r).getRealOutputsTrain().length +
                        ((TrainAndTestReportCrisp)r).getRealOutputsTest().length;
                if (rows > maxRows) {
                    maxRows = rows;
                }
                residuals.put(r.toString(), computeResiduals((TrainAndTestReportCrisp)r));
            } else if (r instanceof TrainAndTestReportInterval) {
                int rows = ((TrainAndTestReportInterval)r).getRealValuesLowers().size();
                if (rows > maxRows) {
                    maxRows = rows;
                }
                residuals.put(r.toString(), computeResiduals((TrainAndTestReportInterval)r));
            }
        }
    }
    
    @Override
    public int getRowCount() {
        return maxRows;
    }

    @Override
    public int getColumnCount() {
        return residuals.keySet().size() + 1;
    }
    
    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex == 0) {
            return "";
        } else {
            Set<String> keys = residuals.keySet();
            return keys.toArray(new String[keys.size()])[columnIndex - 1];
        }
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return rowIndex + 1;
        } else {
            Set<String> keys = residuals.keySet();
            List<Double> res = residuals.get(keys.toArray(new String[keys.size()])[columnIndex - 1]);
            if (res.size() < rowIndex+1) {
                return "<not available>";
            } else {
                return Utils.valToDecPoints(res.get(rowIndex));
            }
        }
    }

    @Override
    public boolean isEmpty() {
        return (residuals.isEmpty());
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        //TODO maybe later make them editable?
        return false;
    }
    
    private List<Double> computeResiduals(TrainAndTestReportCrisp report) {
        List<Double> res = new ArrayList<>();
        
        res.addAll(Utils.getErrors(Utils.arrayToList(report.getRealOutputsTrain()), 
                Utils.arrayToList(report.getFittedValues())));
        res.addAll(Utils.getErrors(Utils.arrayToList(report.getRealOutputsTest()), 
                Utils.arrayToList(report.getForecastValuesTest())));
        
        return res;
    }
    
    private List<Double> computeResiduals(TrainAndTestReportInterval report) {
        List<Interval> allTrainTest = new ArrayList<>();
        allTrainTest.addAll(report.getFittedValues());
        allTrainTest.addAll(report.getForecastValuesTest());
        
        List<Interval> allReal = Utils.zipLowerUpperToIntervals(report.getRealValuesLowers(), report.getRealValuesUppers());
        
        return Utils.getErrorsForIntervals(allReal, allTrainTest, 
                ((ErrorMeasuresInterval) report.getErrorMeasures()).getDistanceUsed());
    }
    
    public Map<String, List<Double>> getDataForSelectedCols(int[] selectedCols) {
        Map<String, List<Double>> res = new LinkedHashMap<>();
        for (int col : selectedCols) {
            if (col != 0) { //skip 0 - these are just the line numbers
                Set<String> keys = residuals.keySet();
                String key = keys.toArray(new String[keys.size()])[col-1];
                res.put(key, residuals.get(key));
            }
        }
        
        return res;
    }
}
