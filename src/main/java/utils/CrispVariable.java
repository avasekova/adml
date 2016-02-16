package utils;

public class CrispVariable extends Variable {

    private String fieldName;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public boolean equals(Object obj) {
        if ((! super.equals(obj)) || (! (obj instanceof CrispVariable))) {
            return false;
        }

        return this.fieldName.equals(((CrispVariable) obj).fieldName);
    }

    @Override
    public int hashCode() {
        return super.hashCode() + this.fieldName.hashCode();
    }

    @Override
    public String toString() {
        return super.toString() + ", field=" + fieldName;
    }
}
