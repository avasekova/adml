package gui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import utils.imlp.ExplanatoryVariable;

public class ExplVarsTableModel extends AbstractTableModel {
    
    private List<ExplanatoryVariable> variables = new ArrayList<>();
    
    public void addVariable(ExplanatoryVariable var) {
        if (! variables.contains(var)) {
            variables.add(var);
        }
    }
    
    public void removeVariable(ExplanatoryVariable var) {
        variables.remove(var);
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
                return var.getFirst();
            case 2:
                return var.getSecond();
            case 3:
                return var.getLag();
        }
        
        return "(NA)";
    }
    
    
}
