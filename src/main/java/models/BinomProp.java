package models;

import models.params.BinomPropParams;
import models.params.Params;
import utils.Const;
import utils.MyRengine;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BinomProp implements Forecastable {
    private static final long serialVersionUID = 1L;

    @Override
    public TrainAndTestReport forecast(Map<String, List<Double>> data, Params parameters) {
        
        BinomPropParams params = (BinomPropParams) parameters;
        
        MyRengine rengine = MyRengine.getRengine();
        rengine.require("forecast");
        
        TrainAndTestReportCrisp report = new TrainAndTestReportCrisp(Model.BINOM_PROP);
        
        
        return report;
    }
    
    public static List<String> binomPropComputePosterior(List<BinomPropParams> params) {
        MyRengine rengine = MyRengine.getRengine();
        rengine.require("LearnBayes");
        
        final String BETA_PARAMS = Const.INPUT + Utils.getCounter();
        final String P = Const.INPUT + Utils.getCounter();
        final String MEAN = Const.INPUT + Utils.getCounter();
        final String MODE = Const.INPUT + Utils.getCounter();
        final String POSTERIOR = Const.OUTPUT + Utils.getCounter();
        
        List<String> plots = new ArrayList<>();
        StringBuilder info = new StringBuilder();
        
        for (int i = 0; i < params.size(); i++) {
            String BETA_PARAMS_NOW = BETA_PARAMS + "." + i;
            BinomPropParams par = params.get(i);
            
            //prepare the plots
            String beta = BETA_PARAMS_NOW + " <- beta.select("
                    + "list(p = " + par.getQuantileOne() + "/100, "
                         + "x = " + par.getQuantileOneValue() + "), "
                    + "list(p = " + par.getQuantileTwo() + "/100, "
                         + "x = " + par.getQuantileTwoValue() + "))";
            rengine.eval(beta);
            
            String distrAndTriplot = beta + ";"
                    + "triplot(" + BETA_PARAMS_NOW + ", c(" + par.getNumSuccesses() + ", "
                                       + (par.getNumObservations() - par.getNumSuccesses())
                            + "))";
            plots.add(distrAndTriplot);
            
            
            //and write summary into the info panel:
            rengine.eval(P + " <- seq(0.005, 0.995, length=500)");
            rengine.eval(POSTERIOR + " <- dbeta(" + P + ", " + BETA_PARAMS_NOW + "[1] + " + par.getNumSuccesses()
                                                      + ", " + BETA_PARAMS_NOW + "[2] + " 
                                                  + (par.getNumObservations() - par.getNumSuccesses())
                                            + ")");
            rengine.eval(MEAN + " <- mean(" + POSTERIOR + ")");
            rengine.eval(MODE + " <- Modus(" + POSTERIOR + ")");
            
            double priorOne = rengine.eval(BETA_PARAMS_NOW + "[1]").asDoubleArray()[0];
            double priorTwo = rengine.eval(BETA_PARAMS_NOW + "[2]").asDoubleArray()[0];
            info.append("Prior distribution: beta(")
                    .append(priorOne).append(", ").append(priorTwo).append(")\n")
                    .append("Posterior distribution: beta(").append(priorOne + par.getNumSuccesses()).append(", ")
                    .append(priorTwo + (par.getNumObservations() - par.getNumSuccesses())).append(")\n");
            info.append("Mean: ").append(rengine.eval(MEAN).asDoubleArray()[0]).append("\n");
            info.append("Mode: ").append(rengine.eval(MODE).asDoubleArray()[0]).append("\n\n");
        }
        
        //TODO osefovat nejak krajsie!!
        plots.add(info.toString());
        
        return plots;
    }
    
    public static String binomPropSimulate(List<BinomPropParams> params, List<Double> alphasssRaw) {
        MyRengine rengine = MyRengine.getRengine();
        rengine.require("LearnBayes");
        
        final String BETA_PARAMS = Const.INPUT + Utils.getCounter();
        final String RESULT = Const.OUTPUT + Utils.getCounter();
        final String POSTERIOR_SAMPLE = Const.OUTPUT + Utils.getCounter();
        final String BETA_POSTERIOR_PARAMS = Const.OUTPUT + Utils.getCounter();
        
        StringBuilder info = new StringBuilder();
        
        for (int i = 0; i < params.size(); i++) {
            String BETA_PARAMS_NOW = BETA_PARAMS + "." + i;
            BinomPropParams par = params.get(i);
            
            rengine.eval(BETA_PARAMS_NOW + " <- beta.select("
                    + "list(p = " + par.getQuantileOne() + "/100, "
                         + "x = " + par.getQuantileOneValue() + "), "
                    + "list(p = " + par.getQuantileTwo() + "/100, "
                         + "x = " + par.getQuantileTwoValue() + "))");
            
            rengine.eval(BETA_POSTERIOR_PARAMS + " <- " + BETA_PARAMS_NOW + " + c(" + par.getNumSuccesses() + ", "
                    + (par.getNumObservations() - par.getNumSuccesses()) +  ")");
            
            rengine.eval(POSTERIOR_SAMPLE + " <- rbeta(1000, " + BETA_POSTERIOR_PARAMS + "[1], "
                    + BETA_POSTERIOR_PARAMS + "[2])");
            
            
            for (Double alphaRaw : alphasssRaw) {
                double alpha = (100 - alphaRaw)/2;
                rengine.eval(RESULT + " <- quantile(" + POSTERIOR_SAMPLE + ", c(" + alpha/100 + ", " + (100-alpha)/100 + "))");
                
                info.append(alphasssRaw).append("% probability interval for the posterior:\n")
                        .append("(").append(Utils.valToDecPoints(rengine.eval(RESULT + "[1]").asDoubleArray()[0])).append(",")
                        .append(Utils.valToDecPoints(rengine.eval(RESULT + "[2]").asDoubleArray()[0])).append(")\n\n");
            }
            
            rengine.rm(BETA_PARAMS_NOW);
        }
        
        rengine.rm(BETA_PARAMS, RESULT, POSTERIOR_SAMPLE, BETA_POSTERIOR_PARAMS);
        
        return info.toString();
    }

    public static List<String> binomPropPredict(List<BinomPropParams> params, List<Integer> numFutureObsss) {
        MyRengine rengine = MyRengine.getRengine();
        rengine.require("LearnBayes");
        
        final String BETA_PARAMS = Const.INPUT + Utils.getCounter();
        final String PREDICTED_DISTR = Const.OUTPUT + Utils.getCounter();
        
        List<String> plots = new ArrayList<>();
        
        for (int i = 0; i < params.size(); i++) {
            String BETA_PARAMS_NOW = BETA_PARAMS + "." + i;
            BinomPropParams par = params.get(i);
            
            rengine.eval(BETA_PARAMS_NOW + " <- beta.select("
                    + "list(p = " + par.getQuantileOne() + "/100, "
                         + "x = " + par.getQuantileOneValue() + "), "
                    + "list(p = " + par.getQuantileTwo() + "/100, "
                         + "x = " + par.getQuantileTwoValue() + "))");
            
            for (int j = 0; j < numFutureObsss.size(); j++) {
                String PREDICTED_DISTR_NOW = PREDICTED_DISTR + "." + i + "." + j;
                rengine.eval(PREDICTED_DISTR_NOW + " <- pbetap(" + BETA_PARAMS_NOW + ", " + numFutureObsss.get(j)
                                         + ", 0:" + numFutureObsss.get(j) + ")");
                plots.add("plot(0:" + numFutureObsss.get(j) + ", " + PREDICTED_DISTR_NOW + ", type=\"h\", "
                        + "xlab = \"Number of successes in the future " + numFutureObsss.get(j) + " observations\", "
                        + "ylab = \"Probability of each number of successes\")");
                
                rengine.rm(PREDICTED_DISTR_NOW);
            }
            
            rengine.rm(BETA_PARAMS_NOW);
        }
        
        rengine.rm(BETA_PARAMS, PREDICTED_DISTR);
        
        return plots;
    }
}
