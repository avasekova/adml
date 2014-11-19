package params;

import java.util.List;

public class AnalysisBatchLine {
    
    private String model;
    private List<? extends Params> modelParams;
    
    public AnalysisBatchLine() {}
    
    public AnalysisBatchLine(String model, List<? extends Params> modelParams) {
        this.model = model;
        this.modelParams = modelParams;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<? extends Params> getModelParams() {
        return modelParams;
    }

    public void setModelParams(List<Params> modelParams) {
        this.modelParams = modelParams;
    }
    
}
