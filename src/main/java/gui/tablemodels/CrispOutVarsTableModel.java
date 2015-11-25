package gui.tablemodels;

import utils.CrispOutputVariable;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CrispOutVarsTableModel extends AbstractTableModel {

    private List<CrispOutputVariable> variables = new ArrayList<>();
    private final String[] columnNames = new String[]{ "Name", "" };
    
    public void addVariable(CrispOutputVariable var) {
        if (! variables.contains(var)) {
            if ("".equals(var.getName())) {
                var.setName("Variable" + (variables.size() + 1));
            }
            variables.add(var);
            
            this.fireTableRowsInserted(variables.size() - 1, variables.size() - 1);
        }
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
        return columnNames.length;
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
        CrispOutputVariable var = variables.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return var.getName();
            case 1:
                return var.getFieldName();
        }
        
        return "(NA)";
    }
    
    public List<CrispOutputVariable> getVariables() {
        return Collections.unmodifiableList(variables);
    }
}
