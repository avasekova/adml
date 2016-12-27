package utils;

import gui.tablemodels.DataTableModel;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyRengine extends Rengine {
    //TODO at the end of using Rengine (in models etc): "rm" all objects not needed anymore

    private static final Logger logger = LoggerFactory.getLogger(MyRengine.class);
    
    private static MyRengine instance = null;
    private final String TEMP = "temp" + Utils.getCounter();

    private MyRengine() {
        super();
    }
    
    public static synchronized MyRengine getRengine() {
        if (instance == null) {
            //If not started with --vanilla, funny things may happen in this R shell.
            //The command-line option --vanilla implies --no-site-file, --no-init-file, --no-environ and (except for R CMD) --no-restore
            String[] Rargs = {"--vanilla"};
            Rengine re = new Rengine(Rargs, false, null);
            // the engine creates R in a new thread, so we should wait until it's ready
            if (!re.waitForR()) {
                System.out.println("Cannot load R");
                return null;
            }
            
            //change the default destination for drawing with JavaGD
            re.eval("Sys.setenv('JAVAGD_CLASS_NAME'='utils/MyJavaGD')"); //!! it's setenv, not putenv
            
            //adding my own functions:
            re.eval("MLPtoR.scale <- function(x) { (x - min(x))/(max(x) - min(x)) }");
            re.eval("MLPtoR.unscale <- function(x,y) { x * (max(y) - min(y)) + min(y) }");
            re.eval("Modus <- function(x) { ux <- unique(x);    ux[which.max(tabulate(match(x, ux)))] }");
            //add more functions here
            
            addScriptsFromFile(re, "scripts.R");
            addScriptsFromFile(re, "scripts_iholt.R");
            
            instance = new MyRengine();
        }
        
        return instance;
    }
    
    private static void addScriptsFromFile(Rengine re, String file) {
        //adding scripts (hack - can only add functions one by one, not a whole file full of them):
        StringBuilder scripts = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if ((! line.isEmpty()) && (! line.startsWith("#"))) { //do not evaluate comments and empty lines
                    //further analyze the line and strip any remaining comments:
                    line = line.split("#")[0]; //take whatever there is until the comment
                    scripts.append(line).append("\n"); //bloody hell, it took ages to find out that it doesn't accept System.lineSeparator(), but accepts "\n"...
                }
            }
        } catch (IOException e) {
            logger.debug("error loading R scripts from file", e);
        }

        //for some reason I cannot make it load more functions in a single call of re.eval
        String scriptsAll = scripts.toString();
        String[] functions = scriptsAll.split("---"); //the file needs to separate the functions with ---
                                                      //(it will not compile, but we don't need it to...)
        for (String fun : functions) {
            re.eval(fun);
        }
    }
    
    public static void stopRengine() {
        if (instance != null) {
            instance.end();
            instance = null;
        }
    }
    
    public void assignMatrix(String name, Map<String, List<Double>> matrixColumns) {
        for (String s : matrixColumns.keySet()) {
            instance.assign(name + "." + s, Utils.listToArray(matrixColumns.get(s)));
        }
        
        StringBuilder sb = new StringBuilder("cbind(");
        boolean next = false;
        for (String s : matrixColumns.keySet()) {
            if (next) {
                sb.append(", ");
            } else {
                next = true;
            }
            
            sb.append(name).append(".").append(s);
        }
        sb.append(")");
        
        instance.eval(name + " <- " + sb.toString());
    }
    
    public void assignMatrix(String name, List<List<Double>> matrixColumns) {
        Map<String, List<Double>> matrixColumnsWithNames = new HashMap<>();
        for (List<Double> l : matrixColumns) {
            matrixColumnsWithNames.put("" + Utils.getCounter(), l);
        }
        
        assignMatrix(name, matrixColumnsWithNames);
    }
    
    public void rm(String name) {
        instance.eval("rm(" + name + ")");
    }
    
    public void rm(String... names) {
        for (String n : names) {
            rm(n);
        }
    }
    
    //TODO sth (method, annotation, anything) to prevent copying "if (!rengine.require) throw sth...." into every Forecastable
    //     i.e. make sure this require takes care of the errors when the pkg is not found and the inability to produce a model
    public boolean require(String packageName) {
        final String SUCCESS = "successihope";
        instance.eval(SUCCESS + " <- require(" + packageName + ")");
        
        if (! instance.eval(SUCCESS).asBool().isTRUE()) {
            JOptionPane.showMessageDialog(null, "The required package '" + packageName + "' was not successfully loaded, so "
                    + "the results of this analysis may be corrupted. "
                    + "Please make sure it is installed in your R environment and try again.");
            return false;
        }
        
        return true;
    }
    
    public boolean require(String... packageNames) {
        boolean success = true;
        for (String p : packageNames) {
            if (! require(p)) {
                success = false;
            }
        }
        
        return success;
    }

    @Override
    public REXP eval(String expression) {
        String[] commands = expression.split(";");
        REXP returnVal = null;
        for (String command : commands) {
//            System.out.println(command); //TODO log all R commands (debug or trace)
            returnVal = super.eval(command);
        }
        
        return returnVal;
    }

    public List<Double> evalAndReturnList(String expression) {
        double[] result = evalAndReturnArray(expression);
        return result == null ? new ArrayList<>() : Utils.arrayToList(result);
    }

    public double[] evalAndReturnArray(String expression) {
        eval(TEMP + " <- " + expression);  //to be sure; so we don't need 'eval(STH <- blabla), eval(STH)', but just 'eval(blabla)'
        REXP getResult = eval(TEMP);
        return getResult.asDoubleArray();
    }
    
    public String createDataFrame(List<String> selectedColumns) {
        List<Integer> counters = new ArrayList<>();
        
        //'assign' the selected columns   TODO later 'assign' them by default on loading the file, so this won't be necessary
        List<List<Double>> data = new ArrayList<>();
        boolean hasNaNs = false;
        int maxLength = 0;
        for (String col : selectedColumns) {
            List<Double> d = (DataTableModel.getInstance().getDataForColname(col));
            if (d.contains(Double.NaN)) {
                hasNaNs = true;
            }
            
            if (d.size() > maxLength) {
                maxLength = d.size();
            }
            data.add(d);
        }
        
        for (List<Double> d : data) {
            int counter = Utils.getCounter();
            counters.add(counter);
            String DATA = Const.INPUT + counter;
            instance.assign(DATA, Utils.listToArray(d));
            instance.eval(DATA + " <- c(rep(NA," + (maxLength - d.size()) + "), " + DATA + ")");
        }
        
        //create data frame
        StringBuilder dataFrame = new StringBuilder("data.frame(");
        for (Integer c : counters) {
            dataFrame.append(Const.INPUT).append(c).append(",");
        }
        dataFrame.deleteCharAt(dataFrame.length() - 1);
        dataFrame.append(")");
        
        final String INPUT = Const.INPUT + Utils.getCounter();
        instance.eval(INPUT + " <- " + dataFrame.toString());
        
        if (hasNaNs) {
            //compute missing vals, otherwise it complains
            final String WITH_MISSING = Const.OUTPUT + Utils.getCounter();
            instance.require("missMDA");
            instance.eval(WITH_MISSING + " <- imputePCA(" + INPUT + ", ncp=1)");
            instance.eval(INPUT + " <- " + WITH_MISSING + "$completeObs");
        }
        
        return INPUT;
    }
}
