package gui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;
import rcaller.RCaller;
import rcaller.RCode;

public class DataTableModel extends AbstractTableModel {
    
    private static final String BRENT = "brent";
    private static final String ACC = "acc";
    private static final String RSCRIPT_EXE = "C:\\Program Files\\R\\R-3.1.0\\bin\\x64\\Rscript.exe";
    
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
        caller.setRscriptExecutable(RSCRIPT_EXE);
        
        RCode code = new RCode();
        
        String filePathEscaped = file.getPath().replace("\\","\\\\");
        
        code.R_require("gdata");
        code.addRCode(BRENT + " <- read.xls(\"" + filePathEscaped + "\", sheet = 1, header = TRUE, stringsAsFactors = FALSE)");
        
        caller.setRCode(code);
        
        caller.runAndReturnResult(BRENT); //pozor, prvy riadok sa berie ako nazov stlpca, a ak tam nie je slovo, vyrobi sa dummy nazov (takze to zahodi hodnoty!)
        
        System.out.println(caller.getParser().getNames());
        
        columnNames = caller.getParser().getNames();

//getNames() z nejakeho dovodu vracia nazvy stlpcov, t.j. [Month_Year, Upper_bound, Lower_bound, Center, Radius] a nie "brent" premennu ako taku        
        for (String colName : columnNames) {
            double[] doubleArray = caller.getParser().getAsDoubleArray(colName);
            values.put(colName, arrayToList(doubleArray));
        }
        
        System.out.println(values);
        //int[] dimensions = caller.getParser().getDimensions("Center");
    }
    
    public ImageIcon producePlot(String colname) {
        try {
            RCaller caller = new RCaller();
            caller.setRscriptExecutable(RSCRIPT_EXE);

            RCode code = new RCode();
            code.clear();
            
            code.addDoubleArray("data", listToArray(values.get(colname)));
            
            File plotFile = code.startPlot();
            System.out.println("Plot will be saved to: " + plotFile);
//            code.addRCode("plot.ts(" + BRENT + "$" + colname + ")");
            code.addRCode("plot.ts(data)");
            code.endPlot();

            caller.setRCode(code);
            
            caller.runOnly();

            
            //code.showPlot(plotFile);
            return code.getPlot(plotFile);
            
        } catch (IOException ex) {
            Logger.getLogger(DataTableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public List<Double> trainAndTest(String colname, int percentTrain) {
        RCaller caller = new RCaller();
        caller.setRscriptExecutable(RSCRIPT_EXE);
        
        RCode code = new RCode();
        code.clear();
        
        code.R_require("forecast");
        List<Double> allData = values.get(colname);
        int numTrainingEntries = Math.round(((float) percentTrain/100)*allData.size());
        List<Double> trainingPortionOfData = allData.subList(0, numTrainingEntries);
        List<Double> testingPortionOfData = allData.subList(numTrainingEntries, allData.size());
        
        code.addDoubleArray("traindata", listToArray(trainingPortionOfData));
        code.addRCode("nnetwork <- nnetar(traindata)");
        
        code.addRCode("forecasted <- forecast(nnetwork, " + testingPortionOfData.size() + ")");
        
        code.addDoubleArray("testdata", listToArray(testingPortionOfData));
        code.addRCode(ACC + " <- accuracy(forecasted, testdata)");
        
        caller.setRCode(code);
        
        caller.runAndReturnResult(ACC);
        
        double[] acc = caller.getParser().getAsDoubleArray(ACC); //pozor na poradie vysledkov, ochenta setenta...
        //vrati vysledky po stlpcoch, tj. ME train, ME test, RMSE train, RMSE test, MAE, MPE, MAPE, MASE
        
        //dalo by sa aj maticu, ale momentalne mi staci ten list:
        //double[][] acc2 = caller.getParser().getAsDoubleMatrix(ACC, 6, 2);
        
        System.out.println(Arrays.toString(acc));
        return arrayToList(acc);
    }
    
    private List<Double> arrayToList(double[] array) {
        List<Double> listDouble = new ArrayList<>();
        for (double value : array) {
            listDouble.add(value);
        }
        return listDouble;
    }
    
    private double[] listToArray(List<Double> list) {
        double[] arrayDouble = new double[list.size()];
        int i = 0;
        for (Double value : list) {
            arrayDouble[i] = value;
            i++;
        }
        return arrayDouble;
    }
    
    public List<String> getColnames() {
        return columnNames;
    }
}
