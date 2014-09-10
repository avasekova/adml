package gui;

import java.util.List;
import javax.swing.table.AbstractTableModel;

public class ErrorMeasuresTableModel extends AbstractTableModel {
    
    private final List<Double> errorMeasures;
    
    public ErrorMeasuresTableModel(List<Double> errorMeasures) {
        this.errorMeasures = errorMeasures;
    }

    @Override
    public int getRowCount() {
        return 3; //TODO zovseobecnit! toto je len na ErrorMeasures z "nnetar"
    }

    @Override
    public int getColumnCount() {
        return 7; //TODO zovseobecnit! toto je len na ErrorMeasures z "nnetar"
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (rowIndex) {
            case 0:
                switch (columnIndex) {
                    case 0: return "";
                    case 1: return "ME";
                    case 2: return "RMSE";
                    case 3: return "MAE";
                    case 4: return "MPE";
                    case 5: return "MAPE";
                    case 6: return "MASE";
                };
                break;
            case 1:
                switch (columnIndex) {
                    case 0: return "Training set";
                    default: return errorMeasures.get((columnIndex - 1)*2);
                }
            case 2:
                switch (columnIndex) {
                    case 0: return "Testing set";
                    default: return errorMeasures.get((columnIndex - 1)*2 + 1);
                }
        }
        return "(NA)";
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        //TODO maybe later make them editable?
        return false;
    }
    
}
