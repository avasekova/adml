package gui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import utils.imlp.ExplanatoryVariable;

public class ExplVarsTableModel extends AbstractTableModel {
    
    private List<ExplanatoryVariable> variables = new ArrayList<>();
    private final String[] columnNames = new String[]{ "Name", "At time", "", "" };
    
    public void addVariable(ExplanatoryVariable var) {
        if (! variables.contains(var)) {
            if ("".equals(var.getName())) {
                var.setName("Variable" + (variables.size() + 1));
            }
            variables.add(var);
        }
        
        this.fireTableRowsInserted(variables.size() - 1, variables.size() - 1);
    }
    
    public void removeRow(int row) {
        if (row > -1) {
            variables.remove(row);
            this.fireTableRowsDeleted(row, row);
        }
    }

    @Override
    public int getRowCount() {
        return variables.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }
    
    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ExplanatoryVariable var = variables.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return var.getName();
            case 1:
                return "(t-" + var.getLag() + ")";
            case 2:
                return var.getFirst();
            case 3:
                return var.getSecond();
        }
        
        return "(NA)";
    }
    
    
}
