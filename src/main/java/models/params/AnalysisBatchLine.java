package models.params;

import java.util.List;

public class AnalysisBatchLine {
    
    private final String model;
    private final List<? extends Params> modelParams;
    private final int numModels;
    
    public AnalysisBatchLine(String model, List<? extends Params> modelParams, int numModels) {
        this.model = model;
        this.modelParams = modelParams;
        this.numModels = numModels;
    }

    public String getModel() {
        return model;
    }

    public List<? extends Params> getModelParams() {
        return modelParams;
    }

    public int getNumModels() {
        return numModels;
    }
}
