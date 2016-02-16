package utils;

import java.io.Serializable;

public class Variable implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private int lag;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

        if (! (obj instanceof Variable)) {
            return false;
        }

        final Variable other = (Variable) obj;

        return (this.name.equals(other.name)) && (this.lag == other.lag);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + this.name.hashCode();
        hash = 17 * hash + this.lag;
        return hash;
    }

    @Override
    public String toString() {
        return "name=" + name + ", lag=" + lag;
    }
}
