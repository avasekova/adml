package utils;

import rcaller.RCode;

public enum RCodeSession { //singleton (hopefully)
    
    //nepouzivat code.clean()! a vzdy, ked si vyziadam INSTANCE, tak po pouziti prepisem code (setRCode)
    
    INSTANCE;
    
    private RCode code = new RCode();
    private int counter = 0;
    
    public RCode getRCode() {
        return code;
    }
    
    public void setRCode(RCode code) {
        this.code = code;
    }
    
    public int getCounter() { //to make sure that no two variables share the same name in one session
        counter++;
        return counter;
    }
    
}
