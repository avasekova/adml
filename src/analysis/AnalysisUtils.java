package analysis;

import gui.tablemodels.DataTableModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import models.TrainAndTestReport;
import models.TrainAndTestReportCrisp;
import models.TrainAndTestReportInterval;
import models.params.BasicStats;
import org.rosuda.JRI.REXP;
import utils.Const;
import utils.MyRengine;
import utils.Utils;

public class AnalysisUtils {
    
    public static String getBasicStats(List<String> selectedValuesList) {
        //TODO refactor: toto sa da volat z basicPlots (tam je ten isty kod, ale prepleteny s plotovanim)
        MyRengine rengine = MyRengine.getRengine();
        
        //mean, standard deviation, median
        StringBuilder basicStatsString = new StringBuilder();
        final String TRAINDATA = Const.TRAINDATA + Utils.getCounter();

        for (String col : selectedValuesList) {
            List<Double> data = DataTableModel.getInstance().getDataForColname(col);
            
            rengine.assign(TRAINDATA, Utils.listToArray(data));
            
            //and compute basic statistics of the data:
            //TODO na.rm - radsej nemazat v kazdej tej funkcii, ale iba raz pred tymi troma volaniami
            REXP getMean = rengine.eval("mean(" + TRAINDATA + ", na.rm=TRUE)");
            double mean = getMean.asDoubleArray()[0];
            REXP getStdDev = rengine.eval("sd(" + TRAINDATA + ", na.rm=TRUE)");
            double stDev = getStdDev.asDoubleArray()[0];
            REXP getMedian = rengine.eval("median(" + TRAINDATA + ", na.rm=TRUE)");
            double median = getMedian.asDoubleArray()[0];
            BasicStats stat = new BasicStats(col);
            stat.setMean(mean);
            stat.setStdDev(stDev);
            stat.setMedian(median);
            
            basicStatsString.append(stat.toString());
            basicStatsString.append(System.lineSeparator());
        }
        
        rengine.rm(TRAINDATA);
        
        return basicStatsString.toString();
    }
    
    public static String getModelDetails(List<TrainAndTestReport> allReports) {
        StringBuilder details = new StringBuilder();
        for (TrainAndTestReport r : allReports) {
            details.append(r.getModelName()).append(" (").append(r.getID()).append(")");
            details.append("\n");
            details.append("--------------------");
            details.append("\n");
            details.append(r.getModelDescription());
            details.append("\n\n");
        }
        
        return details.toString();
    }
    
    public static void addReportToData(TrainAndTestReportCrisp r) {
        final String TRAIN = Const.INPUT + Utils.getCounter();
        final String TEST = Const.INPUT + Utils.getCounter();
        final String FUT = Const.INPUT + Utils.getCounter();
        final String VAR = Const.INPUT + Utils.getCounter();

        MyRengine rengine = MyRengine.getRengine();
        
        rengine.assign(TRAIN, r.getFittedValues());
        rengine.assign(TEST, r.getForecastValuesTest());
        
        if (r.getForecastValuesFuture().length == 0) {
            rengine.eval(VAR + " <- c(" + TRAIN + ", " + TEST + ")");
        } else {
            rengine.assign(FUT, r.getForecastValuesFuture());
            rengine.eval(VAR + " <- c(" + TRAIN + ", " + TEST + ", " + FUT + ")");
        }
        
        //TODO unique identifier of the model wrt Run, or enable rename. now overwrites columns with the same name
        DataTableModel.getInstance().addDataForColname(r.toString(), Utils.arrayToList(rengine.eval(VAR).asDoubleArray()));
        
        rengine.rm(TRAIN, TEST, FUT, VAR);
    }
    
    public static void addReportToData(TrainAndTestReportInterval r) {
        final String TRAIN_MIN = Const.INPUT + Utils.getCounter();
        final String TRAIN_MAX = Const.INPUT + Utils.getCounter();
        final String TRAIN_CENTER = Const.INPUT + Utils.getCounter();
        final String TRAIN_RADIUS = Const.INPUT + Utils.getCounter();
        final String TEST_MIN = Const.INPUT + Utils.getCounter();
        final String TEST_MAX = Const.INPUT + Utils.getCounter();
        final String TEST_CENTER = Const.INPUT + Utils.getCounter();
        final String TEST_RADIUS = Const.INPUT + Utils.getCounter();
        final String FUT_MIN = Const.INPUT + Utils.getCounter();
        final String FUT_MAX = Const.INPUT + Utils.getCounter();
        final String FUT_CENTER = Const.INPUT + Utils.getCounter();
        final String FUT_RADIUS = Const.INPUT + Utils.getCounter();
        
        final String CENTERS = Const.INPUT + Utils.getCounter();
        final String RADII = Const.INPUT + Utils.getCounter();
        final String LOWERS = Const.INPUT + Utils.getCounter();
        final String UPPERS = Const.INPUT + Utils.getCounter();

        MyRengine rengine = MyRengine.getRengine();
        
        rengine.assign(TRAIN_MIN, r.getFittedValuesLowers());
        rengine.assign(TRAIN_MAX, r.getFittedValuesUppers());
        rengine.assign(TRAIN_CENTER, r.getFittedValuesCenters());
        rengine.assign(TRAIN_RADIUS, r.getFittedValuesRadii());
        
        rengine.assign(TEST_MIN, r.getForecastValuesTestLowers());
        rengine.assign(TEST_MAX, r.getForecastValuesTestUppers());
        rengine.assign(TEST_CENTER, r.getForecastValuesTestCenters());
        rengine.assign(TEST_RADIUS, r.getForecastValuesTestRadii());
        
        if (r.getForecastValuesFuture().isEmpty()) {
            rengine.eval(LOWERS + " <- c(" + TRAIN_MIN + ", " + TEST_MIN + ")");
            rengine.eval(UPPERS + " <- c(" + TRAIN_MAX + ", " + TEST_MAX + ")");
            rengine.eval(CENTERS + " <- c(" + TRAIN_CENTER + ", " + TEST_CENTER + ")");
            rengine.eval(RADII + " <- c(" + TRAIN_RADIUS + ", " + TEST_RADIUS + ")");
        } else {
            rengine.assign(FUT_MIN, r.getForecastValuesFutureLowers());
            rengine.assign(FUT_MAX, r.getForecastValuesFutureUppers());
            rengine.assign(FUT_CENTER, r.getForecastValuesFutureCenters());
            rengine.assign(FUT_RADIUS, r.getForecastValuesFutureRadii());
            
            rengine.eval(LOWERS + " <- c(" + TRAIN_MIN + ", " + TEST_MIN + ", " + FUT_MIN + ")");
            rengine.eval(UPPERS + " <- c(" + TRAIN_MAX + ", " + TEST_MAX + ", " + FUT_MAX + ")");
            rengine.eval(CENTERS + " <- c(" + TRAIN_CENTER + ", " + TEST_CENTER + ", " + FUT_CENTER + ")");
            rengine.eval(RADII + " <- c(" + TRAIN_RADIUS + ", " + TEST_RADIUS + ", " + FUT_RADIUS + ")");
        }
        
        //TODO unique identifier of the model wrt Run, or enable rename. now overwrites columns with the same name
        DataTableModel.getInstance().addDataForColname(r.toString() + "(LB)", Utils.arrayToList(rengine.eval(LOWERS).asDoubleArray()));
        DataTableModel.getInstance().addDataForColname(r.toString() + "(UB)", Utils.arrayToList(rengine.eval(UPPERS).asDoubleArray()));
        DataTableModel.getInstance().addDataForColname(r.toString() + "(C)", Utils.arrayToList(rengine.eval(CENTERS).asDoubleArray()));
        DataTableModel.getInstance().addDataForColname(r.toString() + "(R)", Utils.arrayToList(rengine.eval(RADII).asDoubleArray()));
        
        rengine.rm(TRAIN_MIN, TRAIN_MAX, TRAIN_CENTER, TRAIN_RADIUS,
                TEST_MIN, TEST_MAX, TEST_CENTER, TEST_RADIUS,
                FUT_MIN, FUT_MAX, FUT_CENTER, FUT_RADIUS,
                LOWERS, UPPERS, CENTERS, RADII);
    }

    public static String getPrincipalComponents(List<String> selectedValuesList) {
        MyRengine rengine = MyRengine.getRengine();
        rengine.require("FactoMineR");
        
        final String INPUT = rengine.createDataFrame(selectedValuesList);
        
        final String RESULT = Const.OUTPUT + Utils.getCounter();
        rengine.eval(RESULT + " <- PCA(" + INPUT + ", graph = FALSE)"); //TODO output the graph, maybe?
        
        double[] eigenValues = rengine.eval(RESULT + "$eig$eigenvalue").asDoubleArray();
        
        StringBuilder output = new StringBuilder("(Warning: the eigenvalues, rotated component matrix and scores were all calculated using different R packages.)");
        
        output.append("\n\n");
        
        output.append("eigenvalues: ");
        for (int i = 0; i < eigenValues.length; i++) {
            output.append(Utils.valToDecPoints(eigenValues[i])).append(" ");
            output.append("[").append(selectedValuesList.get(i)).append("], ");
        }
        output.deleteCharAt(output.length() - 1);
        output.deleteCharAt(output.length() - 1);
        
        ////
        //rotated components matrix:
        output.append("\n\n\n");
        
        output.append("Rotated components matrix:\n\n");
        
        rengine.eval(RESULT + " <- prcomp(" + INPUT + ", retx = TRUE)");
        double[][] scores = rengine.eval(RESULT + "$x").asDoubleMatrix();
        
        for (String val : selectedValuesList) {
            output.append(String.format("%0$-15s", val)).append("|"); //TODO format!
        }
        output.deleteCharAt(output.length() - 1);
        output.append("\n");
        for (String val : selectedValuesList) {
            output.append("---------------").append("-");
        }
        output.append("\n");
        
        //TODO if rownames in the input file, output rowname to each row
        for (int row = 0; row < scores.length; row++) {
            for (int col = 0; col < scores[row].length; col++) {
                output.append(String.format("%0$-15s", Utils.valToDecPoints(scores[row][col])));
                output.append("|");
            }
            output.deleteCharAt(output.length() - 1);
            output.append("\n");
        }
        
        /////
        //the scores for the whole dataset:
        output.append("\n\n\n");
        
        output.append("Scores of the supplied data on the principal components:\n\n");
        
        rengine.eval(RESULT + " <- princomp(" + INPUT + ", scores = TRUE)");
        scores = rengine.eval(RESULT + "$scores").asDoubleMatrix();
        
        for (String val : selectedValuesList) {
            output.append(String.format("%0$-15s", val)).append("|"); //TODO format!
        }
        output.deleteCharAt(output.length() - 1);
        output.append("\n");
        for (String val : selectedValuesList) {
            output.append("---------------").append("-");
        }
        output.append("\n");
        
        //TODO if rownames in the input file, output rowname to each row
        for (int row = 0; row < scores.length; row++) {
            for (int col = 0; col < scores[row].length; col++) {
                output.append(String.format("%0$-15s", Utils.valToDecPoints(scores[row][col])));
                output.append("|");
            }
            output.deleteCharAt(output.length() - 1);
            output.append("\n");
        }
        
        rengine.rm(INPUT);
        
        return output.toString();
    }
    
    
}
