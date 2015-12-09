package models;

import java.util.HashMap;
import java.util.HashSet;

public enum Model {

    ARIMA ("ARIMA"),
    BINOM_PROP ("Binomial Proportion"),
    BNN ("Bayesian NN"),
    BNN_INT ("Bayesian NN(i)"),
    HOLT ("Holt"),
    HOLT_INT ("Holt(i)"),
    HOLT_WINTERS ("Holt-Winters"),
    HOLT_WINTERS_INT ("Holt-Winters(i)"),
    HYBRID ("Hybrid(i)"),
    INTERVAL_HOLT ("iHolt"),
    INTERVAL_MLP_C_CODE ("iMLP(C code)"),
    KNN_CUSTOM ("KNN(custom)"),
    KNN_FNN ("KNN(FNN)"),
    KNN_KKNN ("KNN(kknn)"),
    KNN_MYOWN ("KNN(custom impl)"),
    MAvg ("Moving average"),
    MLP_INT_NNET ("MLP(i)(nnet)"),
    MLP_INT_NNETAR ("MLP(i)(nnetar)"),
    NEURALNET ("MLP(neuralnet)"),
    NNET ("MLP(nnet)"),
    NNETAR ("MLP(nnetar)"),
    RBF ("RBF"),
    RBF_INT ("RBF(i)"),
    RANDOM_WALK ("random walk"),
    RANDOM_WALK_INT ("random walk(i)"),
    SES ("SES"),
    SES_INT ("SES(i)"),
    VAR ("VAR"),
    VAR_INT ("VAR(i)"),

    ALL ("all"),
    NONE ("none");

    private String name;

    //models that do not care about percentTrain and as such can be flexible when it comes to computing avgs
    public static final HashSet<Model> FLEXIBLE_PERCENTTRAIN_MODELS = new HashSet<Model>(){{
        add(RANDOM_WALK);
        add(RANDOM_WALK_INT);
        add(MAvg);
    }};

    private static HashMap<String, Model> ENUM_NAMES = new HashMap<String, Model>(){{
        for (Model val : Model.values()) {
            put(val.name, val);
        }
    }};

    Model(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static Model myValueOf(String name) {
        Model modelName = null;
        try {
            modelName = valueOf(name);
        } catch (IllegalArgumentException e) {
            modelName = ENUM_NAMES.get(name);
        }

        return modelName;
    }
}
