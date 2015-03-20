package gui.renderers;

import gui.tablemodels.ErrorMeasuresTableModel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import utils.Utils;

public class ErrorTableCellRenderer extends DefaultTableCellRenderer { //TODO pouzit aj na stredny riadok
    //TODO vyrobit len Renderer na hrubsie pismo, a potom ho aplikovat nie na celu tabulku, ale na niektore riadky a stlpce
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
 
        super.getTableCellRendererComponent(table, value, isSelected,
                hasFocus, row, column);
        
        setForeground(Color.BLACK); //aby sa sem neprenasala nejaka nastavena farba z minula
        
        //output headers in bold
        if ((column == 0) || (row == 0) || (row == table.getRowCount()/2)) {
            setFont(getFont().deriveFont(Font.BOLD));
        } else { //inak nie som v headeroch
            String[] namesErrorMeasures = ((ErrorMeasuresTableModel) table.getModel()).getNamesOfSupportedErrorMeasures();
            double min;
            double max;
            if (row < table.getRowCount()/2) { //je to train
                min = ((ErrorMeasuresTableModel) table.getModel()).getMinTrainFor(namesErrorMeasures[column - 1]);
                max = ((ErrorMeasuresTableModel) table.getModel()).getMaxTrainFor(namesErrorMeasures[column - 1]);
            } else { //je to test
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
