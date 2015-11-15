package utils;

import java.io.Serializable;

public class CrispOutputVariable implements Serializable {
    
    private String name;
    private String fieldName;

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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        final CrispOutputVariable other = (CrispOutputVariable) obj;
        
        return (this.name.equals(other.name)) && (this.fieldName.equals(other.fieldName));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + this.name.hashCode();
        hash = 17 * hash + this.fieldName.hashCode();
        return hash;
    }
    
    @Override
    public String toString() {
        return "{name=" + name + ", field=" + fieldName + "}";
    }
}
