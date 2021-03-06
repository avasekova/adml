package gui.tablemodels;

import utils.IntervalVariable;
import utils.imlp.IntervalNamesCentreRadius;

public class IntervalOutVarsTableModel extends OutputVariablesTableModel {

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        IntervalVariable var = (IntervalVariable) (getVariables().get(rowIndex));
        switch (columnIndex) {
            case 0:
                return var.getName();
            case 1:
                if (var.getIntervalNames() instanceof IntervalNamesCentreRadius) {
                    return var.getIntervalNames().toString();
                } else {
                    return var.getIntervalNames().toString();
                }
        }
        
        return "(NA)";
    }

}
