package utils;

import org.rosuda.JRI.Rengine;

public class MyRengine extends Rengine {
    
    private static MyRengine instance = null;
    
    private MyRengine() {
        super();
    }
    
    public static synchronized MyRengine getRengine(){
        if (instance == null) {
            //If not started with --vanilla, funny things may happen in this R shell.
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

}