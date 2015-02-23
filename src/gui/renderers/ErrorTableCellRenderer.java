package gui.renderers;

import java.awt.Component;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ErrorTableCellRenderer extends DefaultTableCellRenderer { //TODO pouzit aj na stredny riadok
    //TODO vyrobit len Renderer na hrubsie pismo, a potom ho aplikovat nie na celu tabulku, ale na niektore riadky a stlpce
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
 
        super.getTableCellRendererComponent(table, value, isSelected,
                hasFocus, row, column);
        
        //output headers in bold
        if ((column == 0) || (row == 0) || (row == table.getRowCount()/2)) {
            setFont(getFont().deriveFont(Font.BOLD));
        }
        
        
        
            
        return this;
    }
    
}
