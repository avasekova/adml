package gui.tablemodels;

import gui.ColourService;
import gui.DefaultPlottable;
import gui.LoadDataCustomizerPanel;
import gui.MainFrame;
import gui.PlotDrawer;
import gui.renderers.PlotLegendListCellRenderer;
import gui.Plottable;
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
import utils.ugliez.PlotStateKeeper;
import utils.Utils;
import utils.ugliez.CallParamsDrawPlotGeneral;

public class DataTableModel extends AbstractTableModel {
    //TODO zjednotit vsetky nazvy premennych vsade v kode (hlavne v GUI), najst si system
    //TODO refaktorovat rovnake kusiska kodu (hlavne v plot drawingu a modeloch/metodach) von do metod;
    
    private final Map<String, List<Double>> values = new LinkedHashMap<>();
    private List<String> columnNames = new ArrayList<>();      //ciste pre convenience ucely
    public static final String LABELS_AXIS_X = Const.LABELS + Utils.getCounter();
    
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
    
    //KONVENCIE:
    //interpretuje prvy riadok ako headers, bez ohladu na to, co v nom je (TODO customizable: 1. use the 1st line, 2. input
    //   custom headers for each column now, 3. use placeholder names X1 ... Xn)
    //interpretuje prvy stlpec ako labels pre os X, tj typicky datumy apod.. Berie to hlupo ako String, aby nebol problem.
    public void openFile(File file, LoadDataCustomizerPanel customizer) {
        final String WORKBOOK = Const.WORKBOOK + Utils.getCounter();
        final String DATA = Const.BRENT + Utils.getCounter();
        
        Rengine rengine = MyRengine.getRengine();
        
        rengine.eval("require(XLConnect)");
        String filePathEscaped = file.getPath().replace("\\","/"); //toto je snad lepsie kvoli platformovej prenositelnosti..?
        rengine.eval(WORKBOOK + " <- loadWorkbook(\"" + filePathEscaped + "\")");
        
        //read data
        switch (customizer.getColnamesType()) {
            case FIRST_ROW:
                rengine.eval(DATA + " <- readWorksheet(" + WORKBOOK + ", sheet = 1, header = TRUE)"); //header=TRUE
                break;
            case DUMMY:
                rengine.eval(DATA + " <- readWorksheet(" + WORKBOOK + ", sheet = 1, header = FALSE)"); //header=FALSE
                break;
            case CUSTOM:
                rengine.eval(DATA + " <- readWorksheet(" + WORKBOOK + ", sheet = 1, header = FALSE)"); //header=FALSE
                break;
        }
        
        //read X labels
        if (customizer.isFirstColumnLabelsAxisX()) {
            //vezmi prvy stlpec ako nazvy na osi X
            //pozor, asStringArray ich potrebuje nutne v uvodzovkach :/
            REXP getLabelsAxisX = rengine.eval(DATA + "[,1]"); //1. stlpec
            String[] labelsAxisX = getLabelsAxisX.asStringArray();
            rengine.assign(LABELS_AXIS_X, labelsAxisX);

            rengine.eval(DATA + " <- " + DATA + "[2:length(" + DATA + ")]"); //a odrezem prvy stlpec
        } else {
            rengine.eval(LABELS_AXIS_X + " <- seq(1,length(" + DATA + "[,1]))");
        }
        
        //and get names of columns
        switch (customizer.getColnamesType()) {
            case FIRST_ROW:
                REXP getColnames = rengine.eval("colnames(" + DATA + ")");
                String[] columnNamesArray = getColnames.asStringArray();
                columnNames = new ArrayList<>(Arrays.asList(columnNamesArray));
                break;
            case DUMMY:
                REXP getColnamesDummyNumbers = rengine.eval("seq(1,length(" + DATA + "))");
                int[] columnNamesDummyNumbersArray = getColnamesDummyNumbers.asIntArray();

                columnNames = new ArrayList<>();
                for (int i : columnNamesDummyNumbersArray) {
                    columnNames.add("X" + i);
                }
                
                break;
            case CUSTOM:
                //TODO
                break;
        }
        
        //ciselne data
        int i = 1;
        for (String colName : columnNames) {
            REXP getColumn = rengine.eval(DATA + "[," + i + "]");
            double[] doubleArray = getColumn.asDoubleArray();
            values.put(colName, Utils.arrayToList(doubleArray));
            i++;
        }
    }
    
    //TODO mozno refaktor a vyhodit do PlotDrawera - aby tam bolo vsetko kreslenie grafov
    public List<BasicStats> drawPlotGeneral(boolean drawNew, CallParamsDrawPlotGeneral par) {
        //get the Y range first (assuming X is the same)
        StringBuilder rangeYStringBuilder = new StringBuilder("range(c(");
        boolean next = false;
        for (String col : par.getColnames()) {
            for (Double d : values.get(col)) {
                if (next) {
                    rangeYStringBuilder.append(", ");
                } else {
                    next = true;
                }
                rangeYStringBuilder.append(d);
            }
        }
        rangeYStringBuilder.append("))");
        String rangeY = rangeYStringBuilder.toString();
        String rangeX = "range(c(0, " + getRowCount() + "))";
        
        if ("acf".equals(par.getPlotFunction()) || "pacf".equals(par.getPlotFunction())) {
            rangeY = "range(c(-1,1))";
            
            //String rangeX = "range(c(0, " + getRowCount() + "))";
            //default lagmax: 10*log10(N)
            rangeX = "range(c(0,10*log10(" + getRowCount() + ")))";
        }
        
        return drawPlotGeneral(drawNew, par, rangeX, rangeY);
    }
    
    public List<BasicStats> drawPlotGeneral(boolean drawNew, CallParamsDrawPlotGeneral par, String rangeX, String rangeY) {
        MainFrame.drawNowToThisGDBufferedPanel = par.getCanvasToUse();
        
        Rengine rengine = MyRengine.getRengine();
        rengine.eval("require(JavaGD)");
        rengine.eval("JavaGD()");
        
        ColourService.getService().resetCounter();
        
        List<BasicStats> basicStatss = new ArrayList<>();
        
        boolean next = false;
        List<Plottable> plots = new ArrayList<>();
        for (String col : par.getColnames()) {
            String colour = ColourService.getService().getNewColour();
            List<Double> data = values.get(col);
            final String TRAINDATA = Const.TRAINDATA + Utils.getCounter();
            rengine.assign(TRAINDATA, Utils.listToArray(data));
            if (next) {
                rengine.eval("par(new=TRUE)");
                rengine.eval(par.getPlotFunction() + "(" + TRAINDATA + par.getAdditionalArgs() + ", "
                        + "axes=FALSE, ann=FALSE, "
                        + "xlim=" + rangeX + ", ylim=" + rangeY + ", lwd=2, col=\"" + colour + "\")");
            } else {
                next = true;
                String plot = par.getPlotFunction() + "(" + TRAINDATA + par.getAdditionalArgs() + ", " + "ylab=NULL, ";
                if ((! par.getPlotFunction().equals("acf")) && (! par.getPlotFunction().equals("pacf"))) {
                    plot += "xaxt=\"n\", "; //suppress X axis
                }
                plot += "xlim=" + rangeX + ", ylim=" + rangeY + ", lwd=2, col=\"" + colour + "\")";
                
                rengine.eval(plot);
                
                if ((! par.getPlotFunction().equals("acf")) && (! par.getPlotFunction().equals("pacf"))) {
                    rengine.eval("axis(1,at=seq(1,length(" + LABELS_AXIS_X + ")),labels=" + LABELS_AXIS_X + ",las=2)");
                }
            }
            
            plots.add(new DefaultPlottable(colour, col));
            
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
        PlotDrawer.drawLegend(par.getListPlotLegend(), plots, new PlotLegendListCellRenderer());
        
        REXP getX = rengine.eval(rangeX);
        double[] ranX = getX.asDoubleArray();
        REXP getY = rengine.eval(rangeY);
        double[] ranY = getY.asDoubleArray();
        PlotStateKeeper.setLastDrawnCrispXmin(ranX[0]);
        PlotStateKeeper.setLastDrawnCrispXmax(ranX[1]);
        PlotStateKeeper.setLastDrawnCrispYmin(ranY[0]);
        PlotStateKeeper.setLastDrawnCrispYmax(ranY[1]);
        
        if (drawNew) {
            PlotStateKeeper.setCrispXmax(ranX[1]);
            PlotStateKeeper.setCrispYmax(ranY[1]);
        }
        
        PlotStateKeeper.setLastCallParams(par);
        
        // R always draws a plot of a default size to the JavaGD device.
        // But our GDBufferedPanel is supposed to have a different size, so
        // we have to resize it back to the size we want it to have.
        MainFrame.drawNowToThisGDBufferedPanel.setSize(new Dimension(par.getWidth(), par.getHeight())); //TODO nechce sa zmensit pod urcitu velkost, vymysliet
        MainFrame.drawNowToThisGDBufferedPanel.initRefresh();
        
        return basicStatss;
    }
    
    public List<String> getColnames() {
        return columnNames;
    }
    
    public List<Double> getDataForColname(String colname) {
        return values.get(colname);
    }
}