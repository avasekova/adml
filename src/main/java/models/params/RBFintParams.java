package models.params;

import gui.settingspanels.BestModelCriterionIntervalSettingsPanel;
import gui.settingspanels.DistanceSettingsPanel;
import gui.settingspanels.SettingsPanel;
import utils.FieldsParser;
import utils.Improvable;

import java.util.ArrayList;
import java.util.List;

public class RBFintParams extends PseudoIntervalParams {
    
    private RBFParams paramsCenter;
    private RBFParams paramsRadius;
    private Integer numNetsToTrain;

    @Override
    public RBFParams getParamsCenter() {
        return paramsCenter;
    }

    public void setParamsCenter(RBFParams paramsCenter) {
        this.paramsCenter = paramsCenter;
    }

    @Override
    public RBFParams getParamsRadius() {
        return paramsRadius;
    }

    public void setParamsRadius(RBFParams paramsRadius) {
        this.paramsRadius = paramsRadius;
    }

    public Integer getNumNetsToTrain() {
        return numNetsToTrain;
    }

    public void setNumNetsToTrain(Integer numNetsToTrain) {
        this.numNetsToTrain = numNetsToTrain;
    }

    @Override
    public Params getClone() {
        RBFintParams param = new RBFintParams();
        param.setDataRangeFrom(this.getDataRangeFrom());
        param.setDataRangeTo(this.getDataRangeTo());
        param.setNumForecasts(this.getNumForecasts());
        param.setSeasonality(this.getSeasonality());
        param.setParamsCenter(paramsCenter);
        param.setParamsRadius(paramsRadius);
        param.setDistance(this.getDistance());
        param.setNumNetsToTrain(numNetsToTrain);
        param.setPercentTrain(this.getPercentTrain());
        
        return param;
    }

    @Override
    public String toString() {
        return super.toString() + "\nnumNetsToTrain=" + numNetsToTrain;
    }
    
    
    public static List<RBFintParams> getParamsRBFint(javax.swing.JPanel percentTrainSettingsPanel_center,
            javax.swing.JComboBox comboBoxColName_center, javax.swing.JPanel panelSettingsRBF_center,
            javax.swing.JPanel percentTrainSettingsPanel_radius, javax.swing.JComboBox comboBoxColName_radius, 
            javax.swing.JPanel panelSettingsRBF_radius, javax.swing.JPanel panelSettingsDistance,
            javax.swing.JTextField numNetsToTrainField,
            javax.swing.JPanel panelBestModelCriterion) throws IllegalArgumentException {
        
        RBFintParams par = new RBFintParams();
        
        List<RBFintParams> resultList = new ArrayList<>();
        resultList.add(par);
        
        List<RBFParams> resultListCenter = RBFParams.getParamsRBF(percentTrainSettingsPanel_center, comboBoxColName_center,
                panelSettingsRBF_center);
        SettingsPanel.setSomethingList(RBFintParams.class, resultList, "setParamsCenter",
                RBFParams.class, resultListCenter);
        
        List<RBFParams> resultListRadius = RBFParams.getParamsRBF(percentTrainSettingsPanel_radius, comboBoxColName_radius,
                panelSettingsRBF_radius);
        if (((SettingsPanel) panelSettingsRBF_radius).isTakenIntoAccount()) {
            SettingsPanel.setSomethingList(RBFintParams.class, resultList, "setParamsRadius",
                RBFParams.class, resultListRadius);
        } else {
            for (RBFintParams params : resultList) {
                params.setParamsRadius(params.getParamsCenter().getClone());
                //overwrite
                params.getParamsRadius().setExplVars(resultListRadius.get(0).getExplVars());
                params.getParamsRadius().setOutVars(resultListRadius.get(0).getOutVars());
            }
        }
        
        ((DistanceSettingsPanel)panelSettingsDistance).setSpecificParams(RBFintParams.class, resultList);
        SettingsPanel.setSomethingList(RBFintParams.class, resultList, "setNumNetsToTrain",
                Integer.class, FieldsParser.parseIntegers(numNetsToTrainField));
        SettingsPanel.setSomethingOneValue(RBFintParams.class, resultList, "setCriterion",
                Improvable.class, ((BestModelCriterionIntervalSettingsPanel)panelBestModelCriterion).getBestModelCriterion());
        
        return resultList;
    }
}
