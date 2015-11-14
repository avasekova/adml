package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import models.TrainAndTestReportInterval;

public abstract class BestModelCriterionInterval {
    
    public enum MINIMIZE implements Improvable {
        MDE, RMSE, MSE, THEILSU, ARV, RMSSE_CENTER, RMSSE_RADIUS;
        
        @Override
        public boolean isBetterThanBest(double currentValue, double bestValue) {
            return currentValue < bestValue;
        }
    }

    public enum MAXIMIZE implements Improvable {
        CVG, EFF, SUM_CVG_EFF;
        
        @Override
        public boolean isBetterThanBest(double currentValue, double bestValue) {
            return currentValue > bestValue;
        }
    }
    
    public static Improvable[] getValues() {
        List<Improvable> values = new ArrayList<>();
        
        values.addAll(Arrays.asList(MAXIMIZE.class.getEnumConstants()));
        values.addAll(Arrays.asList(MINIMIZE.class.getEnumConstants()));
        
        return values.toArray(new Improvable[]{});
    }
    
    public static double computeCriterion(TrainAndTestReportInterval report, Improvable criterion) {
        ErrorMeasuresInterval m = (ErrorMeasuresInterval)(report.getErrorMeasures());
        
        double result = 0;
        
        if (criterion instanceof MINIMIZE) {
            switch ((MINIMIZE)criterion) {
                case ARV:
                    result = m.getArvIntervalTrain() + m.getArvIntervalTest();
                    break;
                case MDE:
                    result = m.getMDEtrain() + m.getMDEtest();
                    break;
                case MSE:
                    result = m.getMSEtrain() + m.getMSEtest();
                    break;
                case RMSE:
                    result = m.getRMSEtrain() + m.getRMSEtest();
                    break;
                case RMSSE_CENTER:
                    result = m.getRMSSEtrain_1() + m.getRMSSEtest_1();
                    break;
                case RMSSE_RADIUS:
                    result = m.getRMSSEtrain_2() + m.getRMSSEtest_2();
                    break;
                case THEILSU:
                    result = m.getTheilsUintervalTrain() + m.getTheilsUintervalTest();
                    break;
            }
        } else if (criterion instanceof MAXIMIZE) {
            switch ((MAXIMIZE)criterion) {
                case CVG:
                    result = m.getMeanCoverageTrain() + m.getMeanCoverageTest();
                    break;
                case EFF:
                    result = m.getMeanEfficiencyTrain() + m.getMeanEfficiencyTest();
                    break;
                case SUM_CVG_EFF:
                    result = m.getMeanCoverageTest() + m.getMeanCoverageTrain() + m.getMeanEfficiencyTest() + m.getMeanEfficiencyTrain();
                    break;
            }
        }
        
        return result;
    }
    
    public static boolean isCurrentBetterThanBest(Improvable criterion, double currentMeasures, double bestMeasures) {
        return criterion.isBetterThanBest(currentMeasures, bestMeasures);
    }
}
