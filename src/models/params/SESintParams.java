package models.params;

import gui.settingspanels.DistanceSettingsPanel;
import gui.settingspanels.SESSettingsPanel;
import gui.settingspanels.SettingsPanel;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class SESintParams extends PseudoIntervalParams {
    
    private SESParams paramsCenter;
    private SESParams paramsRadius;

    @Override
    public SESParams getParamsCenter() {
        return paramsCenter;
    }

    public void setParamsCenter(SESParams paramsCenter) {
        this.paramsCenter = paramsCenter;
    }

    @Override
    public SESParams getParamsRadius() {
        return paramsRadius;
    }

    public void setParamsRadius(SESParams paramsRadius) {
        this.paramsRadius = paramsRadius;
    }

    @Override
    public Params getClone() {
        SESintParams param = new SESintParams();
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
    
    
    public static List<SESintParams> getParamsSESint(JPanel percentTrainSettingsPanel_center, JPanel panelSettingsSES_center,
                JComboBox comboBoxColName_center, JPanel percentTrainSettingsPanel_radius, JPanel panelSettingsSES_radius,
                JComboBox comboBoxColName_radius, JPanel panelSettingsDistance) {
        List<SESParams> resultListCenter = SESParams.getParamsSES(percentTrainSettingsPanel_center, comboBoxColName_center,
                panelSettingsSES_center);
        List<SESParams> resultListRadius = SESParams.getParamsSES(percentTrainSettingsPanel_radius, comboBoxColName_radius,
                panelSettingsSES_radius);
        
        SESintParams par = new SESintParams();
        
        List<SESintParams> resultList = new ArrayList<>();
        resultList.add(par);
        SettingsPanel.setSomethingList(SESintParams.class, resultList, "setParamsCenter",
                SESParams.class, resultListCenter);
        SettingsPanel.setSomethingList(SESintParams.class, resultList, "setParamsRadius",
                SESParams.class, resultListRadius);
        ((DistanceSettingsPanel)panelSettingsDistance).setSpecificParams(SESintParams.class, resultList);
        
        return resultList;
    }
}
