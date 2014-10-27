package utils.imlp;

public class IntervalNamesLowerUpper {
    
    private final String lowerBound;
    private final String upperBound;
    
    public IntervalNamesLowerUpper(String lowerBound, String upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public String getLowerBound() {
        return lowerBound;
    }
    
    public String getUpperBound() {
        return upperBound;
    }
    
    @Override
    public String toString() {
        return "[" + lowerBound + ", " + upperBound + "]";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        final IntervalNamesLowerUpper other = (IntervalNamesLowerUpper) obj;
        return this.lowerBound.equals(other.lowerBound) && 
               this.upperBound.equals(other.upperBound);
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash += 17 * hash + (this.lowerBound).hashCode();
        hash += 17 * hash + (this.upperBound).hashCode();
        return hash;
    }
}
