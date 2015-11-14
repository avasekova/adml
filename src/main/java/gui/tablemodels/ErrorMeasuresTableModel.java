package gui.tablemodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.AbstractTableModel;
import models.TrainAndTestReport;
import utils.ErrorMeasuresCrisp;
import utils.ErrorMeasuresInterval;
import utils.MinMaxTuple;
import utils.Utils;

public abstract class ErrorMeasuresTableModel extends AbstractTableModel {
    
    private final List<TrainAndTestReport> reports;
    private final List<TrainAndTestReport> hiddenReports = new ArrayList<>();
    
    private final Map<String, MinMaxTuple> errorMeasuresMinMaxTrain = new HashMap<>();
    private final Map<String, MinMaxTuple> errorMeasuresMinMaxTest = new HashMap<>();
    
    public ErrorMeasuresTableModel(List<TrainAndTestReport> reports) {
        this.reports = reports;
        
        //and compute which error measures are the best and the worst, so that the renderer can later ask:
        computeMinMaxErrorMeasures();
    }
    
    public abstract String[] getNamesOfSupportedErrorMeasures();
    
    @Override
    public int getRowCount() {
        return (reports.size() * 2) + 2;
    }

    @Override
    public int getColumnCount() {
        return getNamesOfSupportedErrorMeasures().length + 1;
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
                    return "   " + reports.get(rowIndex - 1).toString();
                } else { //rowIndex > reports.size() + 1
                    return "   " + reports.get(rowIndex - (reports.size() + 2)).toString();
                }
            }
        } else {
            if ((rowIndex == 0) || (rowIndex == reports.size() + 1)) {
                return getNamesOfSupportedErrorMeasures()[columnIndex - 1];
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
    
    public void showAllHiddenRows() {
        int sizeBefore = reports.size();
        reports.addAll(hiddenReports);
        hiddenReports.clear();
        this.fireTableRowsInserted(sizeBefore, reports.size()-1);
    }

    public void hideAllButAvg() {
        int originalSize = reports.size();
        List<TrainAndTestReport> stash = new ArrayList<>();
        for (TrainAndTestReport r : reports) {
            if (! r.isAverage()) {
                stash.add(r);
            }
        }
        hiddenReports.addAll(stash);
        reports.removeAll(stash);
        
        this.fireTableRowsDeleted(0, originalSize*2 + 2);
    }
    
    private void computeMinMaxErrorMeasures() {
        Map<String, List<Double>> errorMeasuresAggTrain = new HashMap<>();
        Map<String, List<Double>> errorMeasuresAggTest = new HashMap<>();
        
        String[] names = new String[]{};
        if (! reports.isEmpty()) {
            if (reports.get(0).getErrorMeasures() instanceof ErrorMeasuresCrisp) {
                names = ErrorMeasuresCrisp.namesOfSupportedMeasures();
            } else if (reports.get(0).getErrorMeasures() instanceof ErrorMeasuresInterval) {
                names = ErrorMeasuresInterval.namesOfSupportedMeasures();
            }
        }
        
        for (TrainAndTestReport r : reports) {
            double[] vals = r.getErrorMeasures().serializeToArray(); //vals su po stlpcoch, tj MEtrain, MEtest, RMSEtrain, ...
            
            for (int i = 0; i < vals.length; i++) {
                if (i % 2 == 0) { //parne hodnoty = train
                    errorMeasuresAggTrain = put(errorMeasuresAggTrain, names[i/2], vals[i]);
                } else { //neparne = test
                    errorMeasuresAggTest = put(errorMeasuresAggTest, names[i/2], vals[i]);
                }
            }
        }
        
        //mam nasyslene pre kazdu error measure vsetky aktualne hodnoty. teraz vybrat min a max
        for (String errorMeasure : names) {
            errorMeasuresMinMaxTrain.put(errorMeasure, getMinMax(errorMeasuresAggTrain.get(errorMeasure)));
            errorMeasuresMinMaxTest.put(errorMeasure, getMinMax(errorMeasuresAggTest.get(errorMeasure)));
            
            if (errorMeasure.equals("Mean coverage") || errorMeasure.equals("Mean efficiency")) {
                MinMaxTuple mmTrain = errorMeasuresMinMaxTrain.get(errorMeasure);
                MinMaxTuple mmTest = errorMeasuresMinMaxTest.get(errorMeasure);
                //a teraz to otocit, aby bolo minimum (tj najlepsie) to najvacsie a naopak
                errorMeasuresMinMaxTrain.put(errorMeasure, new MinMaxTuple(mmTrain.getMax(), mmTrain.getMin()));
                errorMeasuresMinMaxTest.put(errorMeasure, new MinMaxTuple(mmTest.getMax(), mmTest.getMin()));
            }
        }
    }
    
    private Map<String, List<Double>> put(Map<String, List<Double>> errorMeasuresAgg, String name, double val) {
        List<Double> list;
        if (errorMeasuresAgg.containsKey(name)) {
            list = errorMeasuresAgg.get(name);
        } else {
            list = new ArrayList<>();
        }
        
        list.add(val);
        errorMeasuresAgg.put(name, list);
        
        return errorMeasuresAgg;
    }
    
    private MinMaxTuple getMinMax(List<Double> list) {
        if (list.isEmpty()) {
            return new MinMaxTuple(Double.NaN, Double.NaN);
        }
        
        double min = list.get(0);
        double max = list.get(0);
        
        for (Double val : list) {
            if (val < min) {
                min = val;
            }
            
            if (val > max) {
                max = val;
            }
        }
        
        return new MinMaxTuple(min, max);
    }
    
    public double getMinTrainFor(String errorMeasure) {
        return errorMeasuresMinMaxTrain.get(errorMeasure).getMin();
    }
    
    public double getMaxTrainFor(String errorMeasure) {
        return errorMeasuresMinMaxTrain.get(errorMeasure).getMax();
    }
    
    public double getMinTestFor(String errorMeasure) {
        return errorMeasuresMinMaxTest.get(errorMeasure).getMin();
    }
    
    public double getMaxTestFor(String errorMeasure) {
        return errorMeasuresMinMaxTest.get(errorMeasure).getMax();
    }
}
