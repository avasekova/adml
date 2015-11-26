package models;

import utils.Const;

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
    public static Forecastable getCrispModel(String modelName){
        Forecastable forecastable = null;
        switch (modelName) { //crisp models
            case Const.ARIMA:
                forecastable = new Arima();
                break;
            case Const.HOLT:
                forecastable = new Holt();
                break;
            case Const.HOLT_WINTERS:
                forecastable = new HoltWinters();
                break;
            case Const.KNN_CUSTOM:
                break;
            case Const.KNN_FNN:
                forecastable = new KNNfnn();
                break;
            case Const.KNN_KKNN:
                forecastable = new KNNkknn();
                break;
            case Const.KNN_MYOWN:
                forecastable = new KNNmyown();
                break;
            case Const.NEURALNET:
                forecastable = new Neuralnet();
                break;
            case Const.NNET:
                forecastable = new Nnet();
                break;
            case Const.NNETAR:
                forecastable = new Nnetar();
                break;
            case Const.RBF:
                forecastable = new RBF();
                break;
            case Const.SES:
                forecastable = new SES();
                break;
            case Const.MAvg:
                forecastable = new MAvg();
            case Const.VAR:
                break;
            case Const.BNN:
                forecastable = new BNN();
                break;
            case Const.RANDOM_WALK:
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
    public static Forecastable getIntervalModel(String modelName){
        Forecastable forecastable = null;
        switch (modelName) {
            case Const.HOLT_INT:
                forecastable = new HoltInt();
                break;
            case Const.HOLT_WINTERS_INT:
                forecastable = new HoltWintersInt();
                break;
            case Const.HYBRID:
                forecastable = new Hybrid();
                break;
            case Const.INTERVAL_HOLT:
                forecastable = new IntervalHolt();
                break;
            case Const.INTERVAL_MLP_C_CODE:
                forecastable = new IntervalMLPCcode();
                break;
            case Const.MLP_INT_NNET:
                forecastable = new MLPintNnet();
                break;
            case Const.MLP_INT_NNETAR:
                forecastable = new MLPintNnetar();
                break;
            case Const.RBF_INT:
                forecastable = new RBFint();
                break;
            case Const.SES_INT:
                forecastable = new SESint();
                break;
            case Const.VAR_INT:
                forecastable = new VARint();
                break;
            case Const.BNN_INT:
                forecastable = new BNNint();
                break;
            case Const.RANDOM_WALK_INT:
                forecastable = new RandomWalkInterval();
                break;
        }

        return forecastable;
    }
}
