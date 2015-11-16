package utils.imlp;

import utils.Utils;

import java.io.Serializable;

public abstract class Interval implements Serializable {
    
    public abstract double getUpperBound();
    public abstract double getLowerBound();
    public abstract double getCentre();
    public abstract double getRadius();
    
    @Override
    public String toString() {
        return "[" + Utils.valToDecPoints(getLowerBound()) + ", " + Utils.valToDecPoints(getUpperBound()) + "]";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        final Interval other = (Interval) obj;
        return Utils.equalsDoubles(this.getLowerBound(), other.getLowerBound()) && 
               Utils.equalsDoubles(this.getUpperBound(), other.getUpperBound());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash += 17 * hash + new Double(this.getLowerBound()).hashCode();
        hash += 17 * hash + new Double(this.getUpperBound()).hashCode();
        return hash;
    }
}
