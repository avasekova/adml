package utils;

import rcaller.RCode;

public enum RCodeSession { //singleton (hopefully)
    
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
