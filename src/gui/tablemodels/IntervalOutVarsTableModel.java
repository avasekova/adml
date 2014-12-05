package gui.tablemodels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import utils.imlp.IntervalNamesCentreRadius;
import utils.imlp.IntervalNamesLowerUpper;
import utils.IntervalOutputVariable;

public class IntervalOutVarsTableModel extends AbstractTableModel {

    private List<IntervalOutputVariable> variables = new ArrayList<>();
    private final String[] columnNames = new String[]{ "Name", "" };
    
    public void addVariable(IntervalOutputVariable var) {
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
        IntervalOutputVariable var = variables.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return var.getName();
            case 1:
                if (var.getIntervalNames() instanceof IntervalNamesCentreRadius) {
                    return ((IntervalNamesCentreRadius) var.getIntervalNames()).toString();
                } else {
                    return ((IntervalNamesLowerUpper) var.getIntervalNames()).toString();
                }
        }
        
        return "(NA)";
    }
    
    public List<IntervalOutputVariable> getVariables() {
        return Collections.unmodifiableList(variables);
    }
}
