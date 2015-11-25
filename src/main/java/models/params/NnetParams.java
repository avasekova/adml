package models.params;

import gui.MainFrame;
import gui.settingspanels.MLPNnetSettingsPanel;
import gui.settingspanels.PercentTrainSettingsPanel;
import utils.CrispExplanatoryVariable;
import utils.R_Bool;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class NnetParams extends Params {
    
    private List<CrispExplanatoryVariable> explVars = new ArrayList<>();
    private String colName;
    private Double abstol;
    private Double reltol;
    private R_Bool skipLayerConnections = null;
    private Double initWeightsRange;
    private Integer maxIterations;
    private Integer numNodesHiddenLayer;
    private R_Bool linearElseLogistic = null;
    private R_Bool leastSqrsElseMaxCondLikelihood = null;
    private R_Bool loglinSoftmaxElseMaxCondLikelihood = null;
    private R_Bool censoredOnElseOff = null;
    private Double weightDecay;
    private R_Bool hessian = null;
    private R_Bool traceOptimization = null;
    private Integer maxNumOfWeights;

    public List<CrispExplanatoryVariable> getExplVars() {
        return explVars;
    }

    public void setExplVars(List<CrispExplanatoryVariable> explVars) {
        this.explVars = explVars;
    }
    
    public String getColName() {
        return colName;
    }
    
    public void setColName(String colName) {
        this.colName = colName;
    }
    
    public Double getAbstol() {
        return abstol;
    }

    public void setAbstol(Double abstol) {
        this.abstol = abstol;
    }

    public Double getReltol() {
        return reltol;
    }

    public void setReltol(Double reltol) {
        this.reltol = reltol;
    }

    public R_Bool getSkipLayerConnections() {
        return skipLayerConnections;
    }

    public void setSkipLayerConnections(R_Bool skipLayerConnections) {
        this.skipLayerConnections = skipLayerConnections;
    }

    public Double getInitWeightsRange() {
        return initWeightsRange;
    }

    public void setInitWeightsRange(Double initWeightsRange) {
        this.initWeightsRange = initWeightsRange;
    }

    public Integer getMaxIterations() {
        return maxIterations;
    }

    public void setMaxIterations(Integer maxIterations) {
        this.maxIterations = maxIterations;
    }

    public Integer getNumNodesHiddenLayer() {
        return numNodesHiddenLayer;
    }

    public void setNumNodesHiddenLayer(Integer numNodesHiddenLayer) {
        this.numNodesHiddenLayer = numNodesHiddenLayer;
    }

    public R_Bool getLinearElseLogistic() {
        return linearElseLogistic;
    }

    public void setLinearElseLogistic(R_Bool linearElseLogistic) {
        this.linearElseLogistic = linearElseLogistic;
    }

    public R_Bool getLeastSqrsElseMaxCondLikelihood() {
        return leastSqrsElseMaxCondLikelihood;
    }

    public void setLeastSqrsElseMaxCondLikelihood(R_Bool leastSqrsElseMaxCondLikelihood) {
        this.leastSqrsElseMaxCondLikelihood = leastSqrsElseMaxCondLikelihood;
    }

    public R_Bool getLoglinSoftmaxElseMaxCondLikelihood() {
        return loglinSoftmaxElseMaxCondLikelihood;
    }

    public void setLoglinSoftmaxElseMaxCondLikelihood(R_Bool loglinSoftmaxElseMaxCondLikelihood) {
        this.loglinSoftmaxElseMaxCondLikelihood = loglinSoftmaxElseMaxCondLikelihood;
    }

    public R_Bool getCensoredOnElseOff() {
        return censoredOnElseOff;
    }

    public void setCensoredOnElseOff(R_Bool censoredOnElseOff) {
        this.censoredOnElseOff = censoredOnElseOff;
    }

    public Double getWeightDecay() {
        return weightDecay;
    }

    public void setWeightDecay(Double weightDecay) {
        this.weightDecay = weightDecay;
    }

    public R_Bool getHessian() {
        return hessian;
    }

    public void setHessian(R_Bool hessian) {
        this.hessian = hessian;
    }

    public R_Bool getTraceOptimization() {
        return traceOptimization;
    }

    public void setTraceOptimization(R_Bool traceOptimization) {
        this.traceOptimization = traceOptimization;
    }

    public Integer getMaxNumOfWeights() {
        return maxNumOfWeights;
    }

    public void setMaxNumOfWeights(Integer maxNumOfWeights) {
        this.maxNumOfWeights = maxNumOfWeights;
    }
    
    @Override
    public NnetParams getClone() {
        NnetParams param = new NnetParams();
        param.setDataRangeFrom(this.getDataRangeFrom());
        param.setDataRangeTo(this.getDataRangeTo());
        param.setSeasonality(this.getSeasonality());
        param.setAbstol(abstol);
        param.setCensoredOnElseOff(censoredOnElseOff);
        param.setHessian(hessian);
        param.setInitWeightsRange(initWeightsRange);
        param.setLeastSqrsElseMaxCondLikelihood(leastSqrsElseMaxCondLikelihood);
        param.setLinearElseLogistic(linearElseLogistic);
        param.setLoglinSoftmaxElseMaxCondLikelihood(loglinSoftmaxElseMaxCondLikelihood);
        param.setMaxIterations(maxIterations);
        param.setMaxNumOfWeights(maxNumOfWeights);
        param.setNumForecasts(this.getNumForecasts());
        param.setNumNodesHiddenLayer(numNodesHiddenLayer);
        param.setPercentTrain(this.getPercentTrain());
        param.setReltol(reltol);
        param.setSkipLayerConnections(skipLayerConnections);
        param.setTraceOptimization(traceOptimization);
        param.setWeightDecay(weightDecay);
        param.setColName(colName);
        param.setExplVars(explVars);
        
        return param;
    }

    @Override
    public String toString() {
        return "absolute threshold = " + abstol + "\n" +
               "relative threshold = " + reltol + "\n" +
               "skip-layer connections allowed = " + Utils.booleanToHumanString(skipLayerConnections) + "\n" +
               "init weights with values in range = <-" + initWeightsRange + ", " + initWeightsRange + ">\n" + 
               "max number of iterations = " + maxIterations + "\n" +
               "number of nodes in the hidden layer = " + numNodesHiddenLayer + "\n" +
               "linear? (if not, logistic) = " + Utils.booleanToHumanString(linearElseLogistic) + "\n" + 
               "least squares? (if not, maximum conditional likelihood) = " + Utils.booleanToHumanString(leastSqrsElseMaxCondLikelihood) + "\n" + 
               "loglinear softmax? (if not, maximum conditional likelihood) = " + Utils.booleanToHumanString(loglinSoftmaxElseMaxCondLikelihood) + "\n" +
               "censored = " + Utils.booleanToHumanString(censoredOnElseOff) + "\n" +
               "weight decay = " + weightDecay + "\n" + 
               "hessian = " + Utils.booleanToHumanString(hessian) + "\n" + 
               "trace optimization = " + Utils.booleanToHumanString(traceOptimization) + "\n" + 
               "max number of weights = " + maxNumOfWeights +
               "expl.vars = " + explVars;
    }

    
    public static List<NnetParams> getParamsNnet(javax.swing.JPanel percentTrainSettingsPanel,
            javax.swing.JComboBox comboBoxColName, javax.swing.JPanel panelSettingsNnet) throws IllegalArgumentException {
        NnetParams par = new NnetParams();
        //zohnat vsetky parametre pre dany model:
        par.setPercentTrain(Integer.parseInt(((PercentTrainSettingsPanel)percentTrainSettingsPanel).getPercentTrain()));
        par.setColName(comboBoxColName.getSelectedItem().toString()); //data
        
        List<NnetParams> resultList = new ArrayList<>();
        resultList.add(par);
        
        MainFrame.getInstance().setParamsGeneral(NnetParams.class, resultList);
        ((MLPNnetSettingsPanel)panelSettingsNnet).setSpecificParams(NnetParams.class, resultList);
        
        return resultList;
    }
}
