package gui;

import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.AbstractTableModel;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;
import utils.Const;
import utils.MyRengine;
import utils.Utils;

public class DataTableModel extends AbstractTableModel {
    
    private final Map<String, List<Double>> values = new LinkedHashMap<>();
    private List<String> columnNames = new ArrayList<>();      //ciste pre convenience ucely
    
    @Override
    public int getRowCount() {
        int maxRows = 0;
        for (List<Double> vals : values.values()) {
            if (vals.size() > maxRows) {
                maxRows = vals.size();
            }
        }
        
        return maxRows;
    }

    @Override
    public int getColumnCount() {
        return values.keySet().size();
    }
    
    @Override
    public String getColumnName(int columnIndex) {
        return columnNames.get(columnIndex);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return values.get(columnNames.get(columnIndex)).get(rowIndex);
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        //TODO maybe later make them editable?
        return false;
    }
    
    
    public void openFile(File file) {
        Rengine rengine = MyRengine.getRengine();
        
        rengine.eval("library(gdata)");
        String filePathEscaped = file.getPath().replace("\\","\\\\");
        rengine.eval(Const.BRENT + " <- read.xls(\"" + filePathEscaped + "\", sheet = 1, header = TRUE, stringsAsFactors = FALSE)");
        
        REXP getColnames = rengine.eval("colnames(" + Const.BRENT + ")");
        String[] columnNamesArray = getColnames.asStringArray();
        
        columnNames = new ArrayList<>(Arrays.asList(columnNamesArray));

        for (String colName : columnNames) {
            REXP getColumn = rengine.eval(Const.BRENT + "$" + colName);
            double[] doubleArray = getColumn.asDoubleArray();
            values.put(colName, Utils.arrayToList(doubleArray));
        }
    }
    
    public void producePlotGeneral(int width, int height, String colname, String plotFunction, String additionalArgs) {
        Rengine rengine = MyRengine.getRengine();
            
        rengine.assign(Const.TRAINDATA, Utils.listToArray(values.get(colname)));

        rengine.eval("library(JavaGD)");
        rengine.eval("JavaGD()");
        rengine.eval(plotFunction + "(" + Const.TRAINDATA + additionalArgs + ")");
        // R always draws a plot of a default size to the JavaGD device.
        // But our GDCanvas is supposed to have a different size, so
        // we have to resize it back to the size we want it to have.
        MainFrame.gdCanvas.setSize(new Dimension(width, height)); //TODO nechce sa zmensit pod urcitu velkost, vymysliet
        MainFrame.gdCanvas.initRefresh();
    }
    
    public List<String> getColnames() {
        return columnNames;
    }
    
    public List<Double> getDataForColname(String colname) {
        return values.get(colname);
    }
}
