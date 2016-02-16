package gui.tablemodels;

import utils.Variable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class VariablesTableModel extends ReportsTableModel {

    private List<Variable> variables = new ArrayList<>();
    private final String[] columnNames = new String[]{ "Name", "At time", "Data"};

    public void addVariable(Variable var) {
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


    public List<Variable> getVariables() {
        return Collections.unmodifiableList(variables);
    }

}
