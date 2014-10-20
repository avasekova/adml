package params;

import utils.R_Bool;

public class NnetParams extends Params {
    
    private int lag;
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

    public int getLag() {
        return lag;
    }

    public void setLag(int lag) {
        this.lag = lag;
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
        param.setAbstol(abstol);
        param.setCensoredOnElseOff(censoredOnElseOff);
        param.setHessian(hessian);
        param.setInitWeightsRange(initWeightsRange);
        param.setLag(lag);
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
        
        return param;
    }
}
