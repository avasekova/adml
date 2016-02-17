package models.params;

import gui.MainFrame;
import gui.settingspanels.HoltWintersSettingsPanel;
import gui.settingspanels.PercentTrainSettingsPanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class HoltWintersParams extends HoltParams {
    
    private String gamma;
    private String seasonalityAddMult;
    private Integer frequency;

    public String getGamma() {
        return gamma;
    }

    public void setGamma(String gamma) {
        this.gamma = gamma;
    }

    public String getSeasonalityAddMult() {
        return seasonalityAddMult;
    }

    public void setSeasonalityAddMult(String seasonalityAddMult) {
        this.seasonalityAddMult = seasonalityAddMult;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }
    
    @Override
    public HoltWintersParams getClone() {
        HoltWintersParams param = new HoltWintersParams();
        param.setDataRangeFrom(this.getDataRangeFrom());
        param.setDataRangeTo(this.getDataRangeTo());
        param.setNumForecasts(this.getNumForecasts());
        param.setPercentTrain(this.getPercentTrain());
        param.setColName(this.getColName());
        param.setAlpha(this.getAlpha());
        param.setBeta(this.getBeta());
        param.setDamped(this.getDamped());
        param.setGamma(gamma);
        param.setSeasonalityAddMult(seasonalityAddMult);
        param.setFrequency(frequency);
        
        return param;
    }

    @Override
    public String toString() {
        return "alpha = " + getAlpha() + "\n" +
               "beta = " + getBeta() + "\n" + 
               "gamma = " + getGamma();
    }
    
    
    public static List<HoltWintersParams> getParamsHoltWinters(JPanel percentTrainSettingsPanel, JPanel panelSettingsHoltWint,
            JComboBox comboBoxColName) throws IllegalArgumentException {
        HoltWintersParams par = new HoltWintersParams();
        //get all params for the model:
        par.setPercentTrain(Integer.parseInt(((PercentTrainSettingsPanel)percentTrainSettingsPanel).getPercentTrain()));
        par.setColName(comboBoxColName.getSelectedItem().toString()); //data
        
        List<HoltWintersParams> resultList = new ArrayList<>();
        resultList.add(par);
        
        MainFrame.getInstance().setParamsGeneral(HoltWintersParams.class, resultList);
        ((HoltWintersSettingsPanel)panelSettingsHoltWint).setSpecificParams(HoltWintersParams.class, resultList);
        
        if (resultList.get(resultList.size() - 1).getFrequency() > 24) {
            int result = JOptionPane.showConfirmDialog(null, "Seasonal frequency larger than 24, so a TBATS model will be used instead of Holt-Winters. "
                    + "This may take a few minutes. Continue?", "Seasonality too large for HW", JOptionPane.YES_NO_OPTION);
            if ((result == JOptionPane.NO_OPTION) || (result == JOptionPane.CLOSED_OPTION)) {
                return new ArrayList<>(); //do nothing
            }
        }
        
        return resultList;
    }
}
