package models.params;

import gui.settingspanels.BestModelCriterionIntervalSettingsPanel;
import gui.settingspanels.DistanceSettingsPanel;
import gui.settingspanels.MLPNnetarSettingsPanel;
import gui.settingspanels.SettingsPanel;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JTextField;
import utils.FieldsParser;
import utils.Improvable;

public class MLPintNnetarParams extends PseudoIntervalParams {
    
    private NnetarParams paramsCenter;
    private NnetarParams paramsRadius;
    private Integer numNetsToTrain;
    
    @Override
    public NnetarParams getParamsCenter() {
        return paramsCenter;
    }

    public void setParamsCenter(NnetarParams paramsCenter) {
        this.paramsCenter = paramsCenter;
    }

    @Override
    public NnetarParams getParamsRadius() {
        return paramsRadius;
    }

    public void setParamsRadius(NnetarParams paramsRadius) {
        this.paramsRadius = paramsRadius;
    }

    public Integer getNumNetsToTrain() {
        return numNetsToTrain;
    }

    public void setNumNetsToTrain(Integer numNetsToTrain) {
        this.numNetsToTrain = numNetsToTrain;
    }
    
    @Override
    public MLPintNnetarParams getClone() {
        MLPintNnetarParams param = new MLPintNnetarParams();
        param.setDataRangeFrom(this.getDataRangeFrom());
        param.setDataRangeTo(this.getDataRangeTo());
        param.setDistance(this.getDistance());
        param.setSeasonality(this.getSeasonality());
        param.setNumForecasts(this.getNumForecasts());
        param.setParamsCenter(paramsCenter);
        param.setParamsRadius(paramsRadius);
        param.setPercentTrain(this.getPercentTrain());
        param.setNumNetsToTrain(this.getNumNetsToTrain());
        param.setCriterion(this.getCriterion());
        
        return param;
    }

    @Override
    public String toString() {
        return super.toString() + "\nnumNetsToTrain=" + numNetsToTrain;
    }
    
    
    public static List<MLPintNnetarParams> getParamsMLPintNnetar(javax.swing.JPanel percentTrainSettingsPanel_center,
            javax.swing.JComboBox comboBoxColName_center, javax.swing.JPanel panelSettingsNnetar_center,
            javax.swing.JPanel percentTrainSettingsPanel_radius, javax.swing.JComboBox comboBoxColName_radius, 
            javax.swing.JPanel panelSettingsNnetar_radius, javax.swing.JPanel panelSettingsDistance,
            JTextField numNetsToTrainField, JPanel panelBestModelCriterion) {
        List<NnetarParams> resultListCenter = NnetarParams.getParamsNnetar(percentTrainSettingsPanel_center, comboBoxColName_center, 
                panelSettingsNnetar_center);
        List<NnetarParams> resultListRadius = NnetarParams.getParamsNnetar(percentTrainSettingsPanel_radius, comboBoxColName_radius, 
                panelSettingsNnetar_radius);
        
        MLPintNnetarParams par = new MLPintNnetarParams();
        
        List<MLPintNnetarParams> resultList = new ArrayList<>();
        resultList.add(par);
        
        ((DistanceSettingsPanel)panelSettingsDistance).setSpecificParams(MLPintNnetarParams.class, resultList);
        SettingsPanel.setSomethingList(MLPintNnetarParams.class, resultList, "setParamsCenter",
                NnetarParams.class, resultListCenter);
        SettingsPanel.setSomethingList(MLPintNnetarParams.class, resultList, "setParamsRadius",
                NnetarParams.class, resultListRadius);
        SettingsPanel.setSomethingList(MLPintNnetarParams.class, resultList, "setNumNetsToTrain",
                Integer.class, FieldsParser.parseIntegers(numNetsToTrainField).subList(0, 1));
        SettingsPanel.setSomethingOneValue(MLPintNnetarParams.class, resultList, "setCriterion",
                Improvable.class, ((BestModelCriterionIntervalSettingsPanel)panelBestModelCriterion).getBestModelCriterion());
        
        return resultList;
    }
}
