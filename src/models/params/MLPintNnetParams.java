package models.params;

import gui.settingspanels.BestModelCriterionIntervalSettingsPanel;
import gui.settingspanels.DistanceSettingsPanel;
import gui.settingspanels.MLPNnetSettingsPanel;
import gui.settingspanels.SettingsPanel;
import java.util.ArrayList;
import java.util.List;
import utils.FieldsParser;
import utils.Improvable;

public class MLPintNnetParams extends PseudoIntervalParams {
    
    private NnetParams paramsCenter;
    private NnetParams paramsRadius;
    private Integer numNetsToTrain;
    
    @Override
    public NnetParams getParamsCenter() {
        return paramsCenter;
    }

    public void setParamsCenter(NnetParams paramsCenter) {
        this.paramsCenter = paramsCenter;
    }

    @Override
    public NnetParams getParamsRadius() {
        return paramsRadius;
    }
    
    public void setParamsRadius(NnetParams paramsRadius) {
        this.paramsRadius = paramsRadius;
    }
    
    public Integer getNumNetsToTrain() {
        return numNetsToTrain;
    }

    public void setNumNetsToTrain(Integer numNetsToTrain) {
        this.numNetsToTrain = numNetsToTrain;
    }
    
    @Override
    public MLPintNnetParams getClone() {
        MLPintNnetParams param = new MLPintNnetParams();
        param.setDataRangeFrom(this.getDataRangeFrom());
        param.setDataRangeTo(this.getDataRangeTo());
        param.setDistance(this.getDistance());
        param.setNumForecasts(this.getNumForecasts());
        param.setSeasonality(this.getSeasonality());
        param.setParamsCenter(paramsCenter);
        param.setParamsRadius(paramsRadius);
        param.setCriterion(this.getCriterion());
        param.setNumNetsToTrain(this.getNumNetsToTrain());
        param.setPercentTrain(this.getPercentTrain());
        
        return param;
    }

    @Override
    public String toString() {
        return super.toString() + "\nnumNetsToTrain=" + numNetsToTrain;
    }
    
    
    public static List<MLPintNnetParams> getParamsMLPintNnet(javax.swing.JPanel percentTrainSettingsPanel_center,
            javax.swing.JComboBox comboBoxColName_center, javax.swing.JPanel panelSettingsNnet_center,
            javax.swing.JPanel percentTrainSettingsPanel_radius, javax.swing.JComboBox comboBoxColName_radius, 
            javax.swing.JPanel panelSettingsNnet_radius, javax.swing.JPanel panelSettingsDistance,
            javax.swing.JTextField numNetsToTrainField, 
            javax.swing.JPanel panelBestModelCriterion) throws IllegalArgumentException {
        List<NnetParams> resultListCenter = NnetParams.getParamsNnet(percentTrainSettingsPanel_center, comboBoxColName_center,
                panelSettingsNnet_center);
        List<NnetParams> resultListRadius = NnetParams.getParamsNnet(percentTrainSettingsPanel_radius, comboBoxColName_radius,
                panelSettingsNnet_radius);
        
        
        MLPintNnetParams par = new MLPintNnetParams();
        
        List<MLPintNnetParams> resultList = new ArrayList<>();
        resultList.add(par);
        
        ((DistanceSettingsPanel)panelSettingsDistance).setSpecificParams(MLPintNnetParams.class, resultList);
        SettingsPanel.setSomethingList(MLPintNnetParams.class, resultList, "setParamsCenter",
                NnetParams.class, resultListCenter);
        SettingsPanel.setSomethingList(MLPintNnetParams.class, resultList, "setParamsRadius",
                NnetParams.class, resultListRadius);
        SettingsPanel.setSomethingList(MLPintNnetParams.class, resultList, "setNumNetsToTrain",
                Integer.class, FieldsParser.parseIntegers(numNetsToTrainField).subList(0, 1));
        SettingsPanel.setSomethingOneValue(MLPintNnetParams.class, resultList, "setCriterion",
                Improvable.class, ((BestModelCriterionIntervalSettingsPanel)panelBestModelCriterion).getBestModelCriterion());
        
        return resultList;
    }
}
