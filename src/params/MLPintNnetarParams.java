package params;

import utils.imlp.dist.Distance;

public class MLPintNnetarParams extends Params { //extends Params ciste kvoli hlavicke metody - nepouzivam atributy z Params!
    
    //TODO rep(NA, " + params.getLag() + "), do plotu! len vymysliet, ako skombinovat lag z center a lag z radius!
    //     asi teda to zarezat na rovnaku dlzku, tj tam, kde zacinaju uz obe (max(startC, startR)) a to povazovat za lag
    
    private NnetarParams paramsCenter;
    private NnetarParams paramsRadius;
    private Distance distanceFunction;
    private boolean centerRadius; //TODO vymysliet krajsie?

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
    
    public boolean isCenterRadius() {
        return centerRadius;
    }

    public void setCenterRadius(Boolean centerRadius) {
        this.centerRadius = centerRadius;
    }
    
    @Override
    public MLPintNnetarParams getClone() {
        MLPintNnetarParams param = new MLPintNnetarParams();
        param.setDataRangeFrom(this.getDataRangeFrom());
        param.setDataRangeTo(this.getDataRangeTo());
        param.setDistanceFunction(distanceFunction);
        param.setNumForecasts(this.getNumForecasts());
        param.setParamsCenter(paramsCenter);
        param.setParamsRadius(paramsRadius);
        param.setPercentTrain(this.getPercentTrain());
        
        return param;
    }
    
    @Override
    public String toString() {
        return "MLPintNnetarParams{" + "paramsCenter=" + paramsCenter + ", paramsRadius=" + paramsRadius + ", distanceFunction=" + distanceFunction + "}\n";
    }
}
