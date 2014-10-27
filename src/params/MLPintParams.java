package params;

import utils.imlp.dist.Distance;

public abstract class MLPintParams extends Params {
    
    private Distance distanceFunction;
    private boolean centerRadius; //TODO vymysliet krajsie?

    public Distance getDistanceFunction() {
        return distanceFunction;
    }

    public void setDistanceFunction(Distance distanceFunction) {
        this.distanceFunction = distanceFunction;
    }
    
    public boolean isCenterRadius() {
        return centerRadius;
    }

    public void setCenterRadius(Boolean centerRadius) {
        this.centerRadius = centerRadius;
    }
}
