package models;

/**
 * Model factory
 * Helps with converting model name to model instance.
 *
 * Created by dusanklinec on 14.11.15.
 */
public class ModelFactory {

    /**
     * Returns an instance of a Crisp model with given model name.
     * If such model is not supported, null is returned.
     *
     * @param modelName name of a model to instantiate
     * @return instance of a model with given model name
     */
    public static Forecastable getCrispModel(Model modelName){
        Forecastable forecastable = null;
        switch (modelName) { //crisp models
            case ARIMA:
                forecastable = new Arima();
                break;
            case HOLT:
                forecastable = new Holt();
                break;
            case HOLT_WINTERS:
                forecastable = new HoltWinters();
                break;
            case KNN_CUSTOM:
                break;
            case KNN_FNN:
                forecastable = new KNNfnn();
                break;
            case KNN_KKNN:
                forecastable = new KNNkknn();
                break;
            case KNN_MYOWN:
                forecastable = new KNNmyown();
                break;
            case NEURALNET:
                forecastable = new Neuralnet();
                break;
            case NNET:
                forecastable = new Nnet();
                break;
            case NNETAR:
                forecastable = new Nnetar();
                break;
            case RBF:
                forecastable = new RBF();
                break;
            case SES:
                forecastable = new SES();
                break;
            case MAvg:
                forecastable = new MAvg();
            case VAR:
                break;
            case BNN:
                forecastable = new BNN();
                break;
            case RANDOM_WALK:
                forecastable = new RandomWalk();
                break;
        }

        return forecastable;
    }

    /**
     * Returns an instance of an Interval model with given model name.
     * If such model is not supported, null is returned.
     *
     * @param modelName name of a model to instantiate
     * @return instance of a model with given model name
     */
    public static Forecastable getIntervalModel(Model modelName){
        Forecastable forecastable = null;
        switch (modelName) {
            case HOLT_INT:
                forecastable = new HoltInt();
                break;
            case HOLT_WINTERS_INT:
                forecastable = new HoltWintersInt();
                break;
            case HYBRID:
                forecastable = new Hybrid();
                break;
            case INTERVAL_HOLT:
                forecastable = new IntervalHolt();
                break;
            case INTERVAL_MLP_C_CODE:
                forecastable = new IntervalMLPCcode();
                break;
            case MLP_INT_NNET:
                forecastable = new MLPintNnet();
                break;
            case MLP_INT_NNETAR:
                forecastable = new MLPintNnetar();
                break;
            case RBF_INT:
                forecastable = new RBFint();
                break;
            case SES_INT:
                forecastable = new SESint();
                break;
            case VAR_INT:
                forecastable = new VARint();
                break;
            case BNN_INT:
                forecastable = new BNNint();
                break;
            case RANDOM_WALK_INT:
                forecastable = new RandomWalkInterval();
                break;
        }

        return forecastable;
    }
}
