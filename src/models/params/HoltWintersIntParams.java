package models.params;

import gui.settingspanels.DistanceSettingsPanel;
import gui.settingspanels.SettingsPanel;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class HoltWintersIntParams extends PseudoIntervalParams {
    
    private HoltWintersParams paramsCenter;
    private HoltWintersParams paramsRadius;
    
    @Override
    public HoltWintersParams getParamsCenter() {
        return paramsCenter;
    }

    public void setParamsCenter(HoltWintersParams paramsCenter) {
        this.paramsCenter = paramsCenter;
    }

    @Override
    public HoltWintersParams getParamsRadius() {
        return paramsRadius;
    }

    public void setParamsRadius(HoltWintersParams paramsRadius) {
        this.paramsRadius = paramsRadius;
    }

    @Override
    public Params getClone() {
        HoltWintersIntParams param = new HoltWintersIntParams();
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
    
    
    public static List<HoltWintersIntParams> getParamsHoltWintersInt(JPanel percentTrainSettingsPanel_center, 
                JPanel panelSettingsHolt_center, JComboBox comboBoxColName_center, JPanel percentTrainSettingsPanel_radius, 
                JPanel panelSettingsHolt_radius, JComboBox comboBoxColName_radius, JPanel panelSettingsDistance) {
        
        HoltWintersIntParams par = new HoltWintersIntParams();
        
        List<HoltWintersIntParams> resultList = new ArrayList<>();
        resultList.add(par);
        
        List<HoltWintersParams> resultListCenter = HoltWintersParams.getParamsHoltWinters(percentTrainSettingsPanel_center, 
                panelSettingsHolt_center, comboBoxColName_center);
        SettingsPanel.setSomethingList(HoltWintersIntParams.class, resultList, "setParamsCenter",
                HoltWintersParams.class, resultListCenter);
        
        List<HoltWintersParams> resultListRadius = HoltWintersParams.getParamsHoltWinters(percentTrainSettingsPanel_radius, 
                panelSettingsHolt_radius, comboBoxColName_radius);
        if (((SettingsPanel) panelSettingsHolt_radius).isTakenIntoAccount()) {
            SettingsPanel.setSomethingList(HoltWintersIntParams.class, resultList, "setParamsRadius",
                    HoltWintersParams.class, resultListRadius);
        } else {
            for (HoltWintersIntParams params : resultList) {
                params.setParamsRadius(params.getParamsCenter().getClone());
                //ale este preplacnut naspat niektore veci
                params.getParamsRadius().setColName(resultListRadius.get(0).getColName());
            }
        }
        ((DistanceSettingsPanel)panelSettingsDistance).setSpecificParams(HoltWintersIntParams.class, resultList);
        
        return resultList;
    }
}
