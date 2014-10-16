package params;

import utils.imlp.dist.Distance;

public class MLPintParams extends Params { //extends Params ciste kvoli hlavicke metody - nepouzivam atributy z Params!
    
    private NnetarParams paramsCenter;
    private NnetarParams paramsRadius;
    private Distance distanceFunction;

    public NnetarParams getParamsCenter() {
        return paramsCenter;
    }

    public void setParamsCenter(NnetarParams paramsCenter) {
        this.paramsCenter = paramsCenter;
    }

    public NnetarParams getParamsRadius() {
        return paramsRadius;
    }

    public void setParamsRadius(NnetarParams paramsRadius) {
        this.paramsRadius = paramsRadius;
    }

    public Distance getDistanceFunction() {
        return distanceFunction;
    }

    public void setDistanceFunction(Distance distanceFunction) {
        this.distanceFunction = distanceFunction;
    }
}
