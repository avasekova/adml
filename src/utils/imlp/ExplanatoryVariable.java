package utils.imlp;

public class ExplanatoryVariable { //TODO refactor this! pouzit Interval miesto boolCenterRad a first a second
                                   // potom netreba vzdy ifovat, ale staci si vypytat od toho intervalu upper a lower
                                   // alebo center a rad podla potreby. a tiez maju toString.
    
    private String name;
    private boolean centerRadius;
    private String first;
    private String second;
    private int lag;

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
        
        final ExplanatoryVariable other = (ExplanatoryVariable) obj;
        
        return (this.name.equals(other.name)) && (this.lag == other.lag) && (this.centerRadius == other.centerRadius)
            && (this.first.equals(other.first)) && this.second.equals(other.second);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + this.name.hashCode();
        hash = 17 * hash + ((this.centerRadius)?1:0);
        hash = 17 * hash + this.first.hashCode();
        hash = 17 * hash + this.second.hashCode();
        hash = 17 * hash + this.lag;
        return hash;
    }
}
