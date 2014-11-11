package params;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VARParams extends Params {
    
    private List<String> endogenousVars = new ArrayList<>();
    private Integer lag;
    private String type;
    private Map<String, List<Double>> data = new HashMap<>();
    private String outputVarName;
    private List<Double> outputVarVals;

    public List<String> getEndogenousVars() {
        return endogenousVars;
    }

    public void setEndogenousVars(List<String> endogenousVars) {
        this.endogenousVars = endogenousVars;
    }

    public Integer getLag() {
        return lag;
    }

    public void setLag(Integer lag) {
        this.lag = lag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, List<Double>> getData() {
        return data;
    }

    public void setData(Map<String, List<Double>> data) {
        this.data = data;
    }

    public String getOutputVarName() {
        return outputVarName;
    }

    public void setOutputVarName(String outputVarName) {
        this.outputVarName = outputVarName;
    }

    public List<Double> getOutputVarVals() {
        return outputVarVals;
    }

    public void setOutputVarVals(List<Double> outputVarVals) {
        this.outputVarVals = outputVarVals;
    }

    @Override
    public VARParams getClone() {
        VARParams param = new VARParams();
        param.setDataRangeFrom(this.getDataRangeFrom());
        param.setDataRangeTo(this.getDataRangeTo());
        param.setNumForecasts(this.getNumForecasts());
        param.setPercentTrain(this.getPercentTrain());
        param.setEndogenousVars(endogenousVars);
        param.setLag(lag);
        param.setType(type);
        param.setData(data);
        param.setOutputVarName(outputVarName);
        param.setOutputVarVals(outputVarVals);
        
        return param;
    }

    @Override
    public String toString() {
        return "VARParams{" + "endogenous=" + endogenousVars + ", lag=" + lag + ", type=" + type + "}\n";
    }
}
