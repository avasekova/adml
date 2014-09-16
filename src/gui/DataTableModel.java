package gui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;
import rcaller.RCaller;
import rcaller.RCode;
import utils.Const;
import utils.RCodeSession;
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
        RCaller caller = new RCaller();
        caller.setRscriptExecutable(Const.RSCRIPT_EXE);
        
        RCode code = RCodeSession.INSTANCE.getRCode();
        
        String filePathEscaped = file.getPath().replace("\\","\\\\");
        
        code.R_require("gdata");
        final String data = Const.DATA + RCodeSession.INSTANCE.getCounter();
        code.addRCode(data + " <- read.xls(\"" + filePathEscaped + "\", sheet = 1, header = TRUE, stringsAsFactors = FALSE)");
        
        caller.setRCode(code);
        
        caller.runAndReturnResult(data); //pozor, prvy riadok sa berie ako nazov stlpca, a ak tam nie je slovo, vyrobi sa dummy nazov (takze to zahodi hodnoty!)
        
        columnNames = caller.getParser().getNames();

//getNames() z nejakeho dovodu vracia nazvy stlpcov, t.j. [Month_Year, Upper_bound, Lower_bound, Center, Radius] a nie "brent" premennu ako taku        
        for (String colName : columnNames) {
            double[] doubleArray = caller.getParser().getAsDoubleArray(colName);
            values.put(colName, Utils.arrayToList(doubleArray));
        }
        
        //int[] dimensions = caller.getParser().getDimensions("Center");
        
        RCodeSession.INSTANCE.setRCode(code);
    }
    
    public ImageIcon producePlotGeneral(String colname, String plotFunction, String additionalArgs) {
        try {
            RCaller caller = new RCaller();
            caller.setRscriptExecutable(Const.RSCRIPT_EXE);

            RCode code = RCodeSession.INSTANCE.getRCode();
            
            final String trainData = Const.TRAINDATA + RCodeSession.INSTANCE.getCounter(); //to have a unique name
            code.addDoubleArray(trainData, Utils.listToArray(values.get(colname)));
            
            File plotFile = code.startPlot();
            System.out.println("Plot will be saved to: " + plotFile);
            code.addRCode(plotFunction + "(" + trainData + additionalArgs + ")"); //plot.ts
            code.endPlot();

            caller.setRCode(code);
            
            caller.runOnly();

            
            RCodeSession.INSTANCE.setRCode(code);
            
            return code.getPlot(plotFile);
            
        } catch (IOException ex) {
            Logger.getLogger(DataTableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public List<String> getColnames() {
        return columnNames;
    }
    
    public List<Double> getDataForColname(String colname) {
        return values.get(colname);
    }
}
