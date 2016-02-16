package gui.tablemodels;


public abstract class OutputVariablesTableModel extends VariablesTableModel {

    private final String[] columnNames = new String[]{ "Name", "" };

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

}
