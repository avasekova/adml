package models.params;

import gui.settingspanels.BNNSettingsPanel;
import gui.settingspanels.BestModelCriterionIntervalSettingsPanel;
import gui.settingspanels.DistanceSettingsPanel;
import gui.settingspanels.SettingsPanel;
import java.util.ArrayList;
import java.util.List;
import utils.FieldsParser;
import utils.Improvable;

public class BNNintParams extends PseudoIntervalParams {
    
    private BNNParams paramsCenter;
    private BNNParams paramsRadius;
    private Integer numNetsToTrain;

    @Override
    public BNNParams getParamsCenter() {
        return paramsCenter;
    }

    public void setParamsCenter(BNNParams paramsCenter) {
        this.paramsCenter = paramsCenter;
    }

    @Override
    public BNNParams getParamsRadius() {
        return paramsRadius;
    }

    public void setParamsRadius(BNNParams paramsRadius) {
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
        BNNintParams param = new BNNintParams();
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
    
    
    public static List<BNNintParams> getParamsBNNint(javax.swing.JPanel percentTrainSettingsPanel,
            javax.swing.JComboBox comboBoxColName_center, javax.swing.JPanel panelSettingsBNNint_center,
            javax.swing.JComboBox comboBoxColName_radius, javax.swing.JPanel panelSettingsBNNint_radius, 
            javax.swing.JPanel panelSettingsDistance, javax.swing.JTextField numNetsToTrainField,
            javax.swing.JPanel panelBestModelCriterion) throws IllegalArgumentException {
        
        List<BNNParams> resultListCenter = BNNParams.getParamsBNN(percentTrainSettingsPanel, comboBoxColName_center,
                panelSettingsBNNint_center);
        List<BNNParams> resultListRadius = BNNParams.getParamsBNN(percentTrainSettingsPanel, comboBoxColName_radius,
                panelSettingsBNNint_radius);
        
        BNNintParams par = new BNNintParams();
        
        List<BNNintParams> resultList = new ArrayList<>();
        resultList.add(par);
        SettingsPanel.setSomethingList(BNNintParams.class, resultList, "setParamsCenter",
                BNNParams.class, resultListCenter);
        SettingsPanel.setSomethingList(BNNintParams.class, resultList, "setParamsRadius",
                BNNParams.class, resultListRadius);
        ((DistanceSettingsPanel)panelSettingsDistance).setSpecificParams(BNNintParams.class, resultList);
        SettingsPanel.setSomethingList(BNNintParams.class, resultList, "setNumNetsToTrain",
                Integer.class, FieldsParser.parseIntegers(numNetsToTrainField));
        SettingsPanel.setSomethingOneValue(BNNintParams.class, resultList, "setCriterion",
                Improvable.class, ((BestModelCriterionIntervalSettingsPanel)panelBestModelCriterion).getBestModelCriterion());
        
        return resultList;
    }
}
