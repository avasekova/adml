package gui;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import models.TrainAndTestReport;

public class ErrorMeasuresTableModel extends AbstractTableModel {
    
    private final List<TrainAndTestReport> reports;
    
    public ErrorMeasuresTableModel(List<TrainAndTestReport> reports) {
        this.reports = reports;
    }

    @Override
    public int getRowCount() {
        return (reports.size() * 2) + 2;
    }

    @Override
    public int getColumnCount() {
        return 7; //TODO zovseobecnit! toto je len na ErrorMeasures z "nnetar" (ME, RMSE, MAE, MPE, MAPE, MASE)
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        System.out.print("row: " + rowIndex + ", column: " + columnIndex + " = ");
        if (columnIndex == 0) {
            if (rowIndex == 0) {
                System.out.println("train");
                return "TRAIN";
            } else if (rowIndex == reports.size() + 1) {
                System.out.println("test");
                return "TEST";
            } else {
                if (rowIndex < reports.size() + 1) {
                    System.out.println("reports.get(" + (rowIndex - 1) + "): " + reports.get(rowIndex - 1).getModelName());
                    return reports.get(rowIndex - 1).getModelName();
                } else { //rowIndex > reports.size() + 1
                    System.out.println("reports.get(" + (rowIndex - (reports.size() + 2)) + "): " + reports.get(rowIndex - (reports.size() + 2)).getModelName());
                    return reports.get(rowIndex - (reports.size() + 2)).getModelName();
                }
            }
        } else {
            if ((rowIndex == 0) || (rowIndex == reports.size() + 1)) {
                System.out.println("measure");
                switch (columnIndex) {
                    case 1: return "ME";
                    case 2: return "RMSE";
                    case 3: return "MAE";
                    case 4: return "MPE";
                    case 5: return "MAPE";
                    case 6: return "MASE";
                }
            } else {
                if (rowIndex < reports.size() + 1) {
                    System.out.println("reports.getMeasures(" + (rowIndex - 1) + "), : get(" + (columnIndex - 1)*2 + "): " + reports.get(rowIndex - 1).getErrorMeasures().get((columnIndex - 1)*2));
                    return reports.get(rowIndex - 1).getErrorMeasures().get((columnIndex - 1)*2); 
                } else { //rowIndex > reports.size() + 1
                    System.out.print("reports.getMeasures(" + (rowIndex - 1) + "), : get(" + ((columnIndex - 1)*2 + 1) + "): ");
                    System.out.println(reports.get(rowIndex - (reports.size() + 2)).getErrorMeasures().get((columnIndex - 1)*2 + 1));
                    return reports.get(rowIndex - (reports.size() + 2)).getErrorMeasures().get((columnIndex - 1)*2 + 1);
                }
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
