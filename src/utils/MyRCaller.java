package utils;

import rcaller.RCaller;

public class MyRCaller extends RCaller {
    
    public MyRCaller() {
        super();
        this.RscriptExecutable = Const.RSCRIPT_EXE;
        this.RExecutable = Const.REXECUTABLE;
    }
    
}
