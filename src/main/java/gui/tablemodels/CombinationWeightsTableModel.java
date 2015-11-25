package gui.tablemodels;

import javax.swing.table.AbstractTableModel;
import java.util.Map;

public class CombinationWeightsTableModel extends AbstractTableModel {
    
    private final Map<String, Double> weights;
    
    public CombinationWeightsTableModel(Map<String, Double> weights) {
        this.weights = weights;
    }
    
    @Override
    public int getRowCount() {
        return weights.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }
    
    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Model";
            case 1:
                return "Weight";
        }
        
        return "";
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return weights.keySet().toArray(new String[]{})[rowIndex];
            case 1:
                return weights.values().toArray(new Double[]{})[rowIndex];
        }
        
        return "";
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        //TODO maybe later make them editable?
        return false;
    }
}
