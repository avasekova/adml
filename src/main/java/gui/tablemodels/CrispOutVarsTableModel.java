package gui.tablemodels;

import utils.CrispVariable;

public class CrispOutVarsTableModel extends OutputVariablesTableModel {

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        CrispVariable var = (CrispVariable) (getVariables().get(rowIndex));
        switch (columnIndex) {
            case 0:
                return var.getName();
            case 1:
                return var.getFieldName();
        }
        
        return "(NA)";
    }

}
