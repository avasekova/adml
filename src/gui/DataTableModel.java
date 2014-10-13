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
import params.BasicStats;
import utils.Const;
import utils.MyRengine;
import utils.Utils;

public class DataTableModel extends AbstractTableModel {
    //TODO zjednotit vsetky nazvy premennych vsade v kode (hlavne v GUI), najst si system
    //TODO refaktorovat rovnake kusiska kodu (hlavne v plot drawingu a modeloch/metodach) von do metod;
    
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
        final String WORKBOOK = Const.WORKBOOK + Utils.getCounter();
        final String DATA = Const.BRENT + Utils.getCounter();
        
        Rengine rengine = MyRengine.getRengine();
        
        rengine.eval("require(XLConnect)");
        String filePathEscaped = file.getPath().replace("\\","/"); //toto je snad lepsie kvoli platformovej prenositelnosti..?
        rengine.eval(WORKBOOK + " <- loadWorkbook(\"" + filePathEscaped + "\")");
        rengine.eval(DATA + " <- readWorksheet(" + WORKBOOK + ", sheet = 1, header = TRUE)");
        //pozor, intepretuje prvy riadok ako headers, bez ohladu na to, co v nom je!
        
        REXP getColnames = rengine.eval("colnames(" + DATA + ")");
        String[] columnNamesArray = getColnames.asStringArray();
        
        columnNames = new ArrayList<>(Arrays.asList(columnNamesArray));

        for (String colName : columnNames) {
            REXP getColumn = rengine.eval(DATA + "$" + colName);
            double[] doubleArray = getColumn.asDoubleArray();
            values.put(colName, Utils.arrayToList(doubleArray));
        }
    }
    
    //TODO mozno refaktor a vyhodit do PlotDrawera - aby tam bolo vsetko kreslenie grafov
    public List<BasicStats> producePlotGeneral(int width, int height, List<String> colnames, String plotFunction, String additionalArgs) {
        Rengine rengine = MyRengine.getRengine();
        rengine.eval("require(JavaGD)");
        rengine.eval("JavaGD()");
        
        List<BasicStats> basicStatss = new ArrayList<>();
        
        boolean next = false;
        int colourNumber = 0;
        List<String> names = new ArrayList<>();
        List<String> colours = new ArrayList<>();
        for (String col : colnames) {
            if (next) {
                rengine.eval("par(new=TRUE)");
            } else {
                next = true;
            }
            
            List<Double> data = values.get(col);
            final String TRAINDATA = Const.TRAINDATA + Utils.getCounter();
            rengine.assign(TRAINDATA, Utils.listToArray(data));
            rengine.eval(plotFunction + "(" + TRAINDATA + additionalArgs + ", lwd=2, col=\"" + PlotDrawer.COLOURS[colourNumber] + "\")");
            names.add(col);
            colours.add(PlotDrawer.COLOURS[colourNumber]);
            colourNumber++;
            
            //and compute basic statistics of the data:
            REXP getMean = rengine.eval("mean(" + TRAINDATA + ")");
            double mean = getMean.asDoubleArray()[0];
            REXP getStdDev = rengine.eval("sd(" + TRAINDATA + ")");
            double stDev = getStdDev.asDoubleArray()[0];
            REXP getMedian = rengine.eval("median(" + TRAINDATA + ")");
            double median = getMedian.asDoubleArray()[0];
            BasicStats basicStats = new BasicStats(col);
            basicStats.setMean(mean);
            basicStats.setStdDev(stDev);
            basicStats.setMedian(median);
            
            basicStatss.add(basicStats);
        }
        
        //add legend
        rengine.eval("legend(\"topleft\", "      
                            + "inset = c(0,-0.11), "
                            + "legend = " + PlotDrawer.getRString(names) + ", "
                            + "fill = " + PlotDrawer.getRString(colours) + ", "
                            + "horiz = TRUE, "
                            + "box.lty = 0, "
                            + "cex = 0.8, "
                            + "text.width = 3, " //TODO pohrat sa s tymto, a urobit to nejak univerzalne, aby tam vzdy vosli vsetky nazvy
                            + "xpd = TRUE)");
        
        // R always draws a plot of a default size to the JavaGD device.
        // But our GDCanvas is supposed to have a different size, so
        // we have to resize it back to the size we want it to have.
        MainFrame.gdCanvas.setSize(new Dimension(width, height)); //TODO nechce sa zmensit pod urcitu velkost, vymysliet
        MainFrame.gdCanvas.initRefresh();
        
        return basicStatss;
    }
    
    public List<String> getColnames() {
        return columnNames;
    }
    
    public List<Double> getDataForColname(String colname) {
        return values.get(colname);
    }
}
