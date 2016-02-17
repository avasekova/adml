package models.params;

import gui.MainFrame;
import gui.settingspanels.HoltSettingsPanel;
import gui.settingspanels.PercentTrainSettingsPanel;
import utils.R_Bool;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class HoltParams extends SESParams {
    
    private String beta;
    private R_Bool damped;
    
    private Integer predIntPercent;

    public String getBeta() {
        return beta;
    }

    public void setBeta(String beta) {
        this.beta = beta;
    }

    public R_Bool getDamped() {
        return damped;
    }

    public void setDamped(R_Bool damped) {
        this.damped = damped;
    }

    public Integer getPredIntPercent() {
        return predIntPercent;
    }

    public void setPredIntPercent(Integer predIntPercent) {
        this.predIntPercent = predIntPercent;
    }
    
    @Override
    public HoltParams getClone() {
        HoltParams param = new HoltParams();
        param.setDataRangeFrom(this.getDataRangeFrom());
        param.setDataRangeTo(this.getDataRangeTo());
        param.setNumForecasts(this.getNumForecasts());
        param.setPercentTrain(this.getPercentTrain());
        param.setSeasonality(this.getSeasonality());
        param.setColName(this.getColName());
        param.setAlpha(this.getAlpha());
        param.setBeta(beta);
        param.setDamped(damped);
        param.setPredIntPercent(predIntPercent);
        
        return param;
    }

    @Override
    public String toString() {
        return "alpha = " + getAlpha() + "\n" +
               "beta = " + beta;
    }
    
    
    public static List<HoltParams> getParamsHolt(JPanel percentTrainSettingsPanel, JPanel panelSettingsHolt,
            JComboBox comboBoxColName) throws IllegalArgumentException {
        HoltParams par = new HoltParams();
        //get all params for the model:
        par.setPercentTrain(Integer.parseInt(((PercentTrainSettingsPanel)percentTrainSettingsPanel).getPercentTrain()));
        par.setColName(comboBoxColName.getSelectedItem().toString()); //data
        
        List<HoltParams> resultList = new ArrayList<>();
        resultList.add(par);
        
        MainFrame.getInstance().setParamsGeneral(HoltParams.class, resultList);
        ((HoltSettingsPanel)panelSettingsHolt).setSpecificParams(HoltParams.class, resultList);
        
        return resultList;
    }
    
}
