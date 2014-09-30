package gui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import utils.imlp.OutputVariable;

public class OutVarsTableModel extends AbstractTableModel {

    private List<OutputVariable> variables = new ArrayList<>();
    
    public void addVariable(OutputVariable var) {
        if (! variables.contains(var)) {
            variables.add(var);
        }
    }
    
    public void removeVariable(OutputVariable var) {
        variables.remove(var);
    }
    
    @Override
    public int getRowCount() {
        return variables.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        OutputVariable var = variables.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return var.getName();
            case 1:
                return var.getFirst();
            case 2:
                return var.getSecond();
        }
        
        return "(NA)";
    }
    
}
