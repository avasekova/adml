package params;

import utils.imlp.dist.Distance;

public class VARintParams extends Params {
    
    private String center;
    private String radius;
    private Integer lag;
    private String type;
    private Boolean optimizeLag;
    private String criterionOptimizeLag;
    private Distance distance;

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public Integer getLag() {
        return lag;
    }

    public void setLag(Integer lag) {
        this.lag = lag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean isOptimizeLag() {
        return optimizeLag;
    }

    public void setOptimizeLag(Boolean optimizeLag) {
        this.optimizeLag = optimizeLag;
    }

    public String getCriterionOptimizeLag() {
        return criterionOptimizeLag;
    }

    public void setCriterionOptimizeLag(String criterionOptimizeLag) {
        this.criterionOptimizeLag = criterionOptimizeLag;
    }

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }
    
    @Override
    public VARintParams getClone() {
        VARintParams param = new VARintParams();
        param.setDataRangeFrom(this.getDataRangeFrom());
        param.setDataRangeTo(this.getDataRangeTo());
        param.setNumForecasts(this.getNumForecasts());
        param.setPercentTrain(this.getPercentTrain());
        param.setLag(lag);
        param.setType(type);
        param.setCenter(center);
        param.setRadius(radius);
        param.setOptimizeLag(optimizeLag);
        param.setCriterionOptimizeLag(criterionOptimizeLag);
        param.setDistance(distance);
        
        return param;
    }

    @Override
    public String toString() {
        return "VARintParams{" + "center=" + center + ", radius=" + radius + ", lag=" + lag + ", type=" + type + "}\n";
    }
}
