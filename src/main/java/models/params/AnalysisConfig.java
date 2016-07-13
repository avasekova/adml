package models.params;

import models.Model;

import java.util.List;

public class AnalysisConfig {
    
    private final Model model;
    private final List<? extends Params> modelParams;
    private final int numModels;
    
    public AnalysisConfig(Model model, List<? extends Params> modelParams, int numModels) {
        this.model = model;
        this.modelParams = modelParams;
        this.numModels = numModels;
    }

    public Model getModel() {
        return model;
    }

    public List<? extends Params> getModelParams() {
        return modelParams;
    }

    public int getNumModels() {
        return numModels;
    }
}
