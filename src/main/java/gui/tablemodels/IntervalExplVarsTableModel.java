package gui.tablemodels;

import utils.IntervalExplanatoryVariable;
import utils.imlp.IntervalNamesCentreRadius;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IntervalExplVarsTableModel extends AbstractTableModel {
    
    private List<IntervalExplanatoryVariable> variables = new ArrayList<>();
    private final String[] columnNames = new String[]{ "Name", "At time", ""};
    
    public void addVariable(IntervalExplanatoryVariable var) {
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
        IntervalExplanatoryVariable var = variables.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return var.getName();
            case 1:
                return "(t-" + var.getLag() + ")";
            case 2:
                if (var.getIntervalNames() instanceof IntervalNamesCentreRadius) {
                    return var.getIntervalNames().toString();
                } else {
                    return var.getIntervalNames().toString();
                }
        }
        
        return "(NA)";
    }
    
    public List<IntervalExplanatoryVariable> getVariables() {
        return Collections.unmodifiableList(variables);
    }
}
