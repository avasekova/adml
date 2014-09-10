package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class TableBothHeadersCellColorRenderer extends DefaultTableCellRenderer {
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
 
        super.getTableCellRendererComponent(table, value, isSelected,
                hasFocus, row, column);
        
        if ((column == 0) || (row == 0)) {
            setFont(getFont().deriveFont(Font.BOLD));
        }
            
        return this;
    }
    
}
