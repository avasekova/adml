package utils;

import rcaller.RCaller;

public class MyRCaller extends RCaller {
    
    private static MyRCaller instance = null;
    
    private MyRCaller() {
        super();
        this.RscriptExecutable = Const.RSCRIPT_EXE;
        this.RExecutable = Const.REXECUTABLE;
    }
    
    public static synchronized MyRCaller getInstance(){
        if (instance == null) {
            instance = new MyRCaller();
        }
        
        return instance;
    }
}