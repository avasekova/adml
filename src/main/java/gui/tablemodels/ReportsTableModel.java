package gui.tablemodels;


import javax.swing.table.AbstractTableModel;

public abstract class ReportsTableModel extends AbstractTableModel {

    public boolean isEmpty() {
        return getRowCount() == 0;
    }
}
