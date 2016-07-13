package gui.tablemodels;

import models.params.AnalysisConfig;

import java.util.ArrayList;
import java.util.List;

public class AnalysisBatchTableModel extends ReportsTableModel {

    private List<AnalysisConfig> lines = new ArrayList<>();
    
    @Override
    public int getRowCount() {
        return lines.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }
    
    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "";
            case 1:
                return "Model";
            case 2:
                return "Params";
        }
        
        return "";
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return rowIndex + 1;
            case 1:
                return lines.get(rowIndex).getModel();
            case 2:
                return lines.get(rowIndex).getModelParams().toString();
        }
        
        return "";
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        //TODO maybe later make them editable?
        return false;
    }
    
    public void addLine(AnalysisConfig line) {
        lines.add(line);
        fireTableRowsInserted(lines.size()-1, lines.size()-1);
    }
    
    public void removeRows(int[] rows) {
        List<AnalysisConfig> toRemove = new ArrayList<>();
        for (int r : rows) {
            toRemove.add(lines.get(r));
        }
        lines.removeAll(toRemove);
        
        if (rows.length > 0) {
            this.fireTableRowsDeleted(rows[0], rows[rows.length-1]);
        }
    }
    
    public void clear() {
        lines.clear();
    }
    
    public List<AnalysisConfig> getAllLines() {
        return lines;
    }
    
    public void setAllLines(List<AnalysisConfig> lines) {
        this.lines = new ArrayList<>();
        for (AnalysisConfig l : lines) {
            this.lines.add(l);
        }
    }
}
