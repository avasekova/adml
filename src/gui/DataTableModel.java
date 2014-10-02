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
        final String DATAFILE = Const.BRENT + Utils.getCounter();
        
        Rengine rengine = MyRengine.getRengine();
        
        rengine.eval("require(XLConnect)");
        String filePathEscaped = file.getPath().replace("\\","/"); //toto je snad lepsie kvoli platformovej prenositelnosti..?
        rengine.eval(DATAFILE + " <- readWorksheetFromFile(\"" + filePathEscaped + "\", sheet = 1)");
        //pozor, intepretuje prvy riadok ako headers, bez ohladu na to, co v nom je!
        
        REXP getColnames = rengine.eval("colnames(" + DATAFILE + ")");
        String[] columnNamesArray = getColnames.asStringArray();
        
        columnNames = new ArrayList<>(Arrays.asList(columnNamesArray));

        for (String colName : columnNames) {
            REXP getColumn = rengine.eval(DATAFILE + "$" + colName);
            double[] doubleArray = getColumn.asDoubleArray();
            values.put(colName, Utils.arrayToList(doubleArray));
        }
    }
    
    //TODO mozno refaktor a vyhodit do PlotDrawera - aby tam bolo vsetko kreslenie grafov
    public void producePlotGeneral(int width, int height, String colname, String plotFunction, String additionalArgs) {
        final String TRAINDATA = Const.TRAINDATA + Utils.getCounter();
        
        Rengine rengine = MyRengine.getRengine();
            
        rengine.assign(TRAINDATA, Utils.listToArray(values.get(colname)));

        rengine.eval("require(JavaGD)");
        rengine.eval("JavaGD()");
        rengine.eval(plotFunction + "(" + TRAINDATA + additionalArgs + ")");
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
