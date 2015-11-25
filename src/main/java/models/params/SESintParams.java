package models.params;

import gui.settingspanels.DistanceSettingsPanel;
import gui.settingspanels.SettingsPanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

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
        
        SESintParams par = new SESintParams();
        
        List<SESintParams> resultList = new ArrayList<>();
        resultList.add(par);
        
        List<SESParams> resultListCenter = SESParams.getParamsSES(percentTrainSettingsPanel_center, comboBoxColName_center,
                panelSettingsSES_center);
        SettingsPanel.setSomethingList(SESintParams.class, resultList, "setParamsCenter",
                SESParams.class, resultListCenter);
        
        List<SESParams> resultListRadius = SESParams.getParamsSES(percentTrainSettingsPanel_radius, comboBoxColName_radius,
                panelSettingsSES_radius);
        if (((SettingsPanel) panelSettingsSES_radius).isTakenIntoAccount()) {
            SettingsPanel.setSomethingList(SESintParams.class, resultList, "setParamsRadius",
                SESParams.class, resultListRadius);
        } else {
            for (SESintParams params : resultList) {
                params.setParamsRadius(params.getParamsCenter().getClone());
                //ale este preplacnut naspat niektore veci
                params.getParamsRadius().setColName(resultListRadius.get(0).getColName());
            }
        }
        ((DistanceSettingsPanel)panelSettingsDistance).setSpecificParams(SESintParams.class, resultList);
        
        return resultList;
    }
}
