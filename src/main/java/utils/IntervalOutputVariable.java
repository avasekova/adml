package utils;

import utils.imlp.IntervalNames;

import java.io.Serializable;

public class IntervalOutputVariable implements Serializable {
    
    private String name;
    private IntervalNames intervalNames;

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
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        final IntervalOutputVariable other = (IntervalOutputVariable) obj;
        
        return (this.name.equals(other.name)) && (this.intervalNames.equals(other.intervalNames));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + this.name.hashCode();
        hash = 17 * hash + this.intervalNames.hashCode();
        return hash;
    }
    
    @Override
    public String toString() {
        return "{name=" + name + ", intvl=" + intervalNames + "}";
    }
}
