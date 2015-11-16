package utils;

import java.io.Serializable;

public class CrispExplanatoryVariable implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String name;
    private String fieldName;
    private int lag;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
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
        
        final CrispExplanatoryVariable other = (CrispExplanatoryVariable) obj;
        
        return (this.name.equals(other.name)) && (this.lag == other.lag) && (this.fieldName.equals(other.fieldName));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + this.name.hashCode();
        hash = 17 * hash + this.fieldName.hashCode();
        hash = 17 * hash + this.lag;
        return hash;
    }

    @Override
    public String toString() {
        return "{name=" + name + ", field=" + fieldName + ", lag=" + lag + "}";
    }
}
