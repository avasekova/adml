package models.avg;

import gui.MainFrame;
import models.TrainAndTestReportCrisp;
import models.TrainAndTestReportInterval;
import utils.Const;
import utils.MyRengine;
import utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AverageEigenvalsPCA extends Average {
    
    private Map<String, Double> eigenValsCenters = new HashMap<>();
    private Map<String, Double> eigenValsRadii = new HashMap<>();
    private double denominatorCenters = 0;
    private double denominatorRadii = 0;

    public AverageEigenvalsPCA(boolean avgCTSperM, boolean avgCTS, boolean avgIntTSperM, 
            boolean avgIntTS, List<TrainAndTestReportInterval> models) {
        super(avgCTSperM, avgCTS, avgIntTSperM, avgIntTS);
        
        if (avgIntTS) {
            //add all models to data
            List<String> namesOfModelsForPCA_center = new ArrayList<>();
            List<String> namesOfModelsForPCA_radius = new ArrayList<>();
            for (TrainAndTestReportInterval r : models) {
                MainFrame.getInstance().addReportToData(r);
                namesOfModelsForPCA_center.add(r.toString() + "(C)");
                namesOfModelsForPCA_radius.add(r.toString() + "(R)");
            }

            //compute pca for all: once for centers, once for radii
            double[] eigenvals_center = computeEigenvals(namesOfModelsForPCA_center);
            double[] eigenvals_radius = computeEigenvals(namesOfModelsForPCA_radius);

            //compute weights from the eigenvals.
            denominatorCenters = 0;
            for (double d : eigenvals_center) {
                denominatorCenters += d;
            }
            denominatorRadii = 0;
            for (double d : eigenvals_radius) {
                denominatorRadii += d;
            }

            eigenValsCenters = getEigenvals(eigenvals_center, namesOfModelsForPCA_center);
            eigenValsRadii = getEigenvals(eigenvals_radius, namesOfModelsForPCA_radius);
        }
    }
    
    private Map<String, Double> getEigenvals(double[] eigenvals, List<String> models) {
        Map<String, Double> ev = new HashMap<>();
        for (int i = 0; i < eigenvals.length; i++) {
            ev.put(models.get(i), eigenvals[i]);
        }
        
        return ev;
    }
    
    
    private double[] computeEigenvals(List<String> selected) {
        MyRengine rengine = MyRengine.getRengine();
        rengine.require("FactoMineR");
        
        final String INPUT = rengine.createDataFrame(selected);
        
        final String RESULT = Const.OUTPUT + Utils.getCounter();
        rengine.eval(RESULT + " <- PCA(" + INPUT + ", graph = FALSE)"); //TODO output the graph, maybe?

        return rengine.eval(RESULT + "$eig$eigenvalue").asDoubleArray();
    }

    @Override
    public double getWeightForModelTrain(TrainAndTestReportCrisp report) {
        return 0;
    }

    @Override
    public double getWeightForModelTest(TrainAndTestReportCrisp report) {
        return 0;
    }

    @Override
    public double getWeightForModelFuture(TrainAndTestReportCrisp report) {
        return getWeightForModelTest(report);
    }

    @Override
    public double getWeightForModelTrain(TrainAndTestReportInterval report) {
        return eigenValsCenters.get(report.toString() + "(C)")/denominatorCenters;
    }

    @Override
    public double getWeightForModelTest(TrainAndTestReportInterval report) {
        return eigenValsCenters.get(report.toString() + "(C)")/denominatorCenters;
    }
    
    
    @Override
    public double getWeightForModelFuture(TrainAndTestReportInterval report) {
        return getWeightForModelTest(report);
    }

    @Override
    public String getName() {
        return "avg[eigenValsPCA]";
    }
    
}
