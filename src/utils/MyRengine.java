package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.rosuda.JRI.Rengine;

public class MyRengine extends Rengine {
    //TODO na konci kazdeho pouzivania Rengine (v modeloch atd): "rm" vsetky objekty, co uz nebudem potrebovat
    
    private static MyRengine instance = null;
    
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
            
            //change the default destination for drawing with JavaGD //TODO always update when refactoring...
            re.eval("Sys.setenv('JAVAGD_CLASS_NAME'='utils/MyJavaGD')"); //pozor! je to setenv, nie putenv
            
            //adding my own functions:
            re.eval("MLPtoR.scale <- function(x) { (x - min(x))/(max(x) - min(x)) }");
            re.eval("MLPtoR.unscale <- function(x,y) { x * (max(y) - min(y)) + min(y) }");
            //add more functions here
            
            //adding scripts (hack - can only add functions one by one, not a whole file full of them):
            StringBuilder scripts = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader("scripts.R"))) {
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
                //TODO log
            }
            
            //for some reason I cannot make it load more functions in a single call of re.eval
            String scriptsAll = scripts.toString();
            String[] functions = scriptsAll.split("---"); //the file needs to separate the functions with ---
                                                          //(it will not compile, but we don't need it to...)
            for (String fun : functions) {
                re.eval(fun);
            }
            
            instance = new MyRengine();
        }
        
        return instance;
    }
    
    public static void stopRengine() {
        if (instance != null) {
            instance.end();
            instance = null;
        }
    }
    
    public void assignMatrix(String name, Map<String, List<Double>> matrixColumns) {
        for (String s : matrixColumns.keySet()) {
            this.assign(name + "." + s, Utils.listToArray(matrixColumns.get(s)));
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
        
        this.eval(name + " <- " + sb.toString());
    }

}