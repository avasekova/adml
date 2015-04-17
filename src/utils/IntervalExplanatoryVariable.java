package utils;

import utils.imlp.IntervalNames;

public class IntervalExplanatoryVariable {
    
    private String name;
    private IntervalNames intervalNames;
    private int lag;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IntervalNames getIntervalNames() {
        return intervalNames;
    }

    public void setIntervalNames(IntervalNames intervalNames) {
        this.intervalNames = intervalNames;
    }

    public int getLag() {
        return lag;
    }

    public void setLag(int lag) {
        this.lag = lag;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        final IntervalExplanatoryVariable other = (IntervalExplanatoryVariable) obj;
        
        return (this.name.equals(other.name)) && (this.lag == other.lag) && (this.intervalNames.equals(other.intervalNames));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + this.name.hashCode();
        hash = 17 * hash + this.intervalNames.hashCode();
        hash = 17 * hash + this.lag;
        return hash;
    }
    
    @Override
    public String toString() {
        return "{name=" + name + ", intvl=" + intervalNames + ", lag=" + lag + "}";
    }
}
