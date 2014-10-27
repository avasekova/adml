package utils.imlp;

public class IntervalNamesCentreRadius {

    private final String centre;
    private final String radius;
    
    public IntervalNamesCentreRadius(String centre, String radius) {
        this.centre = centre;
        this.radius = radius;
    }

    public String getCentre() {
        return centre;
    }

    public String getRadius() {
        return radius;
    }
    
    @Override
    public String toString() {
        return "<" + centre + ", " + radius + ">";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        final IntervalNamesCentreRadius other = (IntervalNamesCentreRadius) obj;
        return this.centre.equals(other.centre) && 
               this.radius.equals(other.radius);
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash += 17 * hash + (this.centre).hashCode();
        hash += 17 * hash + (this.radius).hashCode();
        return hash;
    }
}

