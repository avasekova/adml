package params;

import utils.imlp.dist.Distance;

public class MLPintParams extends Params { //extends Params ciste kvoli hlavicke metody - nepouzivam atributy z Params!
    
    //TODO rep(NA, " + params.getLag() + "), do plotu! len vymysliet, ako skombinovat lag z center a lag z radius!
    //     asi teda to zarezat na rovnaku dlzku, tj tam, kde zacinaju uz obe (max(startC, startR)) a to povazovat za lag
    
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
    
    @Override
    public MLPintParams getClone() {
        MLPintParams param = new MLPintParams();
        param.setDataRangeFrom(this.getDataRangeFrom());
        param.setDataRangeTo(this.getDataRangeTo());
        param.setDistanceFunction(distanceFunction);
        param.setNumForecasts(this.getNumForecasts());
        param.setParamsCenter(paramsCenter);
        param.setParamsRadius(paramsRadius);
        param.setPercentTrain(this.getPercentTrain());
        
        return param;
    }
}
