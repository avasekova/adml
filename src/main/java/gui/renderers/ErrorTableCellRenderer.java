package gui.renderers;

import gui.tablemodels.ErrorMeasuresTableModel;
import utils.Utils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ErrorTableCellRenderer extends DefaultTableCellRenderer {
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
 
        super.getTableCellRendererComponent(table, value, isSelected,
                hasFocus, row, column);

        setForeground(Color.BLACK); //so as to overwrite any colours left from the past run
        
        //output headers in bold
        if ((column == 0) || (row == 0) || (row == table.getRowCount()/2)) {
            setFont(getFont().deriveFont(Font.BOLD));
        } else { //else I am not in headers
            String[] namesErrorMeasures = ((ErrorMeasuresTableModel) table.getModel()).getNamesOfSupportedErrorMeasures();
            double min;
            double max;
            if (row < table.getRowCount()/2) { //train
                min = ((ErrorMeasuresTableModel) table.getModel()).getMinTrainFor(namesErrorMeasures[column - 1]);
                max = ((ErrorMeasuresTableModel) table.getModel()).getMaxTrainFor(namesErrorMeasures[column - 1]);
            } else { //test
                min = ((ErrorMeasuresTableModel) table.getModel()).getMinTestFor(namesErrorMeasures[column - 1]);
                max = ((ErrorMeasuresTableModel) table.getModel()).getMaxTestFor(namesErrorMeasures[column - 1]);
            }
            
            if (Utils.equalsDoubles((Double) value, Utils.valToDecPoints(min))) {
                setForeground(Color.GREEN);
            } else {
                if (Utils.equalsDoubles((Double) value, Utils.valToDecPoints(max))) {
                    setForeground(Color.RED);
                }
            }
        }
        
            
        return this;
    }
    
}
