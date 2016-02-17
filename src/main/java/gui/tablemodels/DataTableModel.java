package gui.tablemodels;

import gui.LoadDataCustomizerPanel;
import org.rosuda.JRI.REXP;
import utils.Const;
import utils.MyRengine;
import utils.Utils;

import javax.swing.table.AbstractTableModel;
import java.io.File;
import java.util.*;

public class DataTableModel extends ReportsTableModel {

    private static DataTableModel INSTANCE = null; //TODO may change once we allow loading multiple files
    
    private final Map<String, List<Double>> values = new LinkedHashMap<>();
    private List<String> columnNames = new ArrayList<>();      //for convenience
    public static final String LABELS_AXIS_X = Const.LABELS + Utils.getCounter();
    
    private int maxRows = 0;
    
    private DataTableModel() {
        super();
    }
    
    public static synchronized DataTableModel getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataTableModel();
        }
        
        return INSTANCE;
    }
    
    @Override
    public int getRowCount() {
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
    
    //CONVENTIONS:
    //interprets the first line as headers, regardless of what it contains (TODO customizable: 1. use the 1st line, 2. input
    //   custom headers for each column now, 3. use placeholder names X1 ... Xn)
    //interprets the first column as labels for axis X, i.e. typically dates etc.. Just dumb Strings so as not to cause trouble.
    public void openFile(File file, LoadDataCustomizerPanel customizer) {
        final String WORKBOOK = Const.WORKBOOK + Utils.getCounter();
        final String DATA = Const.BRENT + Utils.getCounter();
        
        //first clean anything you may have:
        this.columnNames.clear();
        this.values.clear();
        //do not forget to clean all the comboboxes, lists, etc. in the GUI!
        
        //then load new
        MyRengine rengine = MyRengine.getRengine();
        
        String filePathEscaped = file.getPath().replace("\\", "/"); //alright, "File.separator" instead of "/" crashes the whole thing
        String header = "";
        //read data
        switch (customizer.getColnamesType()) {
            case FIRST_ROW:
                header = "TRUE";
                break;
            case DUMMY:
            case CUSTOM:
                header = "FALSE";
                break;
        }
        
        if (".csv".equals(file.getName().substring(file.getName().lastIndexOf('.'), file.getName().length()))) {
            //csv loads differently from xls(x)
            rengine.eval(DATA + " <- read.csv(file=\"" + filePathEscaped + "\", header=" + header + ", sep=\",\")");
        } else {
            rengine.require("XLConnect");
            rengine.eval(WORKBOOK + " <- loadWorkbook(\"" + filePathEscaped + "\")");
        
            rengine.eval(DATA + " <- readWorksheet(" + WORKBOOK + ", sheet = 1, header = " + header + ")"); //header=TRUE
        }
        
        //read X labels
        if (customizer.isFirstColumnLabelsAxisX()) {
            //take the first column as the axis X labels
            //asStringArray needs them quoted!
            REXP getLabelsAxisX = rengine.eval(DATA + "[,1]"); //1st column
            String[] labelsAxisX = getLabelsAxisX.asStringArray();
            if (labelsAxisX == null) { //could not be read as strings; read them as numbers and convert
                double[] labelsAxisXasNumbers = getLabelsAxisX.asDoubleArray();
                labelsAxisX = new String[labelsAxisXasNumbers.length];
                for (int i = 0; i < labelsAxisXasNumbers.length; i++) {
                    if ((labelsAxisXasNumbers[i] % 1) == 0) { //integer
                        //cut off the decimal point
                        labelsAxisX[i] = "" + (int)Math.floor(labelsAxisXasNumbers[i]);
                    } else {
                        labelsAxisX[i] = "" + labelsAxisXasNumbers[i];
                    }
                }
            }
            rengine.assign(LABELS_AXIS_X, labelsAxisX);

            rengine.eval(DATA + " <- " + DATA + "[2:length(" + DATA + ")]"); //and cut off the first column
        } else {
            rengine.eval(LABELS_AXIS_X + " <- seq(1,length(" + DATA + "[,1]))"); //TODO check if correct: maybe there's one too many
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
        
        //numbers
        int i = 1;
        for (String colName : columnNames) {
            double[] doubleArray = rengine.evalAndReturnArray(DATA + "[," + i + "]");
            values.put(colName, Utils.arrayToList(doubleArray));
            
            if (doubleArray.length > maxRows) {
                maxRows = doubleArray.length;
            }
            
            i++;
        }
    }
    
    public List<String> getColnames() {
        return columnNames;
    }
    
    public List<Double> getDataForColname(String colname) {
        return values.get(colname);
    }

    public Map<String, List<Double>> getAllValues() {
        return Collections.unmodifiableMap(values);
    }
    
    public void addDataForColname(String colname, List<Double> data) {
        values.put(colname, data);
        if (! columnNames.contains(colname)) {
            columnNames.add(colname);
            
            if (data.size() > maxRows) {
                maxRows = data.size();
            }
        }
    }
}
