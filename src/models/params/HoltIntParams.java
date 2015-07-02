package models.params;

import gui.settingspanels.DistanceSettingsPanel;
import gui.settingspanels.HoltSettingsPanel;
import gui.settingspanels.SettingsPanel;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class HoltIntParams extends PseudoIntervalParams {
    
    private HoltParams paramsCenter;
    private HoltParams paramsRadius;

    @Override
    public HoltParams getParamsCenter() {
        return paramsCenter;
    }

    public void setParamsCenter(HoltParams paramsCenter) {
        this.paramsCenter = paramsCenter;
    }

    @Override
    public HoltParams getParamsRadius() {
        return paramsRadius;
    }

    public void setParamsRadius(HoltParams paramsRadius) {
        this.paramsRadius = paramsRadius;
    }
    
    @Override
    public Params getClone() {
        HoltIntParams param = new HoltIntParams();
        param.setDataRangeFrom(this.getDataRangeFrom());
        param.setDataRangeTo(this.getDataRangeTo());
        param.setNumForecasts(this.getNumForecasts());
        param.setPercentTrain(this.getPercentTrain());
        param.setSeasonality(this.getSeasonality());
        param.setParamsCenter(paramsCenter);
        param.setParamsRadius(paramsRadius);
        param.setDistance(this.getDistance());
        
        return param;
    }
    
    
    public static List<HoltIntParams> getParamsHoltInt(JPanel percentTrainSettingsPanel_center, JPanel panelSettingsHolt_center,
                JComboBox comboBoxColName_center, JPanel percentTrainSettingsPanel_radius, JPanel panelSettingsHolt_radius,
                JComboBox comboBoxColName_radius, JPanel panelSettingsDistance) {
        List<HoltParams> resultListCenter = HoltParams.getParamsHolt(percentTrainSettingsPanel_center, panelSettingsHolt_center,
                comboBoxColName_center);
        List<HoltParams> resultListRadius = HoltParams.getParamsHolt(percentTrainSettingsPanel_radius, panelSettingsHolt_radius,
                comboBoxColName_radius);
        
        HoltIntParams par = new HoltIntParams();
        
        List<HoltIntParams> resultList = new ArrayList<>();
        resultList.add(par);
        SettingsPanel.setSomethingList(HoltIntParams.class, resultList, "setParamsCenter",
                HoltParams.class, resultListCenter);
        SettingsPanel.setSomethingList(HoltIntParams.class, resultList, "setParamsRadius",
                HoltParams.class, resultListRadius);
        ((DistanceSettingsPanel)panelSettingsDistance).setSpecificParams(HoltIntParams.class, resultList);
        
        return resultList;
    }
}
