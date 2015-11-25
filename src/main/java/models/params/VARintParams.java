package models.params;

import gui.MainFrame;
import gui.settingspanels.DistanceSettingsPanel;
import gui.settingspanels.PercentTrainSettingsPanel;
import gui.settingspanels.VARintSettingsPanel;
import utils.imlp.dist.Distance;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

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
        param.setSeasonality(this.getSeasonality());
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
        return "Center: " + center + "\n" + 
               "Radius: " + radius + "\n" + 
               "lag = " + lag + "\n" + 
               "type = " + type;
    }
    
    
    public static List<VARintParams> getParamsVARint(JPanel percentTrainSettingsPanel, JPanel distanceSettingsPanel, 
            JPanel panelSettingsVARint) throws IllegalArgumentException {
        VARintParams par = new VARintParams();
        //zohnat vsetky parametre pre dany model:
        par.setPercentTrain(Integer.parseInt(((PercentTrainSettingsPanel)percentTrainSettingsPanel).getPercentTrain()));
        
        List<VARintParams> resultList = new ArrayList<>();
        resultList.add(par);
        
        MainFrame.getInstance().setParamsGeneral(VARintParams.class, resultList);
        ((VARintSettingsPanel)panelSettingsVARint).setSpecificParams(VARintParams.class, resultList);
        ((DistanceSettingsPanel)distanceSettingsPanel).setSpecificParams(VARintParams.class, resultList);
        
        return resultList;
    }
}
