package utils.imlp;

public class OutputVariable {
    
    private String name;
    private boolean centerRadius;
    private String first;
    private String second;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCenterRadius() {
        return centerRadius;
    }

    public void setCenterRadius(boolean centerRadius) {
        this.centerRadius = centerRadius;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        final OutputVariable other = (OutputVariable) obj;
        
        return (this.name.equals(other.name)) && (this.centerRadius == other.centerRadius)
            && (this.first.equals(other.first)) && this.second.equals(other.second);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + this.name.hashCode();
        hash = 17 * hash + ((this.centerRadius)?1:0);
        hash = 17 * hash + this.first.hashCode();
        hash = 17 * hash + this.second.hashCode();
        return hash;
    }
}
