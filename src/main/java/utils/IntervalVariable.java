package utils;

import utils.imlp.IntervalNames;

public class IntervalVariable extends Variable {

    private IntervalNames intervalNames;

    public IntervalNames getIntervalNames() {
        return intervalNames;
    }

    public void setIntervalNames(IntervalNames intervalNames) {
        this.intervalNames = intervalNames;
    }

    @Override
    public boolean equals(Object obj) {
        if ((! super.equals(obj)) || (! (obj instanceof IntervalVariable))) {
            return false;
        }

        return this.intervalNames.equals(((IntervalVariable) obj).intervalNames);
    }

    @Override
    public int hashCode() {
        return super.hashCode() + this.intervalNames.hashCode();
    }
    
    @Override
    public String toString() {
        return super.toString() + ", intvl=" + intervalNames;
    }
}
