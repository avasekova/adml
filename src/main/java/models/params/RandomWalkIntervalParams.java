package models.params;

import gui.MainFrame;
import gui.settingspanels.DistanceSettingsPanel;
import models.RandomWalkInterval;
import utils.imlp.dist.Distance;

import java.util.ArrayList;
import java.util.List;

public class RandomWalkIntervalParams extends Params {

    private Distance distance;
    private String colnameCenter;
    private String colnameRadius;

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public String getColnameCenter() {
        return colnameCenter;
    }

    public void setColnameCenter(String colnameCenter) {
        this.colnameCenter = colnameCenter;
    }

    public String getColnameRadius() {
        return colnameRadius;
    }

    public void setColnameRadius(String colnameRadius) {
        this.colnameRadius = colnameRadius;
    }

    @Override
    public RandomWalkIntervalParams getClone() {
        RandomWalkIntervalParams param = new RandomWalkIntervalParams();
        param.setDataRangeFrom(this.getDataRangeFrom());
        param.setDataRangeTo(this.getDataRangeTo());
        param.setSeasonality(this.getSeasonality());
        param.setNumForecasts(this.getNumForecasts());
        param.setColnameCenter(colnameCenter);
        param.setColnameRadius(colnameRadius);
        param.setDistance(distance);

        return param;
    }

    public static List<RandomWalkIntervalParams> getParamsRandomWalkInterval(javax.swing.JPanel panelSettingsDistance,
            javax.swing.JComboBox comboBoxColNameCenter, javax.swing.JComboBox comboBoxColNameRadius) {
        RandomWalkIntervalParams par = new RandomWalkIntervalParams();
        par.setColnameCenter(comboBoxColNameCenter.getSelectedItem().toString()); //data
        par.setColnameRadius(comboBoxColNameRadius.getSelectedItem().toString());

        List<RandomWalkIntervalParams> resultList = new ArrayList<>();
        resultList.add(par);

        MainFrame.getInstance().setParamsGeneral(RandomWalkIntervalParams.class, resultList);
        ((DistanceSettingsPanel)panelSettingsDistance).setSpecificParams(RandomWalkIntervalParams.class, resultList);

        return resultList;
    }
}
