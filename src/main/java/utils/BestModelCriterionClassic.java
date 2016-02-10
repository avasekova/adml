package utils;

import models.TrainAndTestReportCrisp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BestModelCriterionClassic {
    
    public enum MINIMIZE implements Improvable {
        ME, RMSE, MAE, MSE, THEILSU, RMSSE, MPE, MAPE, MASE;

        @Override
        public boolean isBetterThanBest(double currentValue, double bestValue) {
            return currentValue < bestValue;
        }
    }

    public enum MAXIMIZE implements Improvable {

        ; //nothing here so far

        @Override
        public boolean isBetterThanBest(double currentValue, double bestValue) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
    
    public static Improvable[] getValues() {
        List<Improvable> values = new ArrayList<>();
        
        values.addAll(Arrays.asList(MAXIMIZE.class.getEnumConstants()));
        values.addAll(Arrays.asList(MINIMIZE.class.getEnumConstants()));
        
        return values.toArray(new Improvable[values.size()]);
    }
    
    public static double computeCriterion(TrainAndTestReportCrisp report, Improvable criterion) {
        ErrorMeasuresCrisp m = (ErrorMeasuresCrisp)(report.getErrorMeasures());
        
        double result = 0;
        
        if (criterion instanceof MINIMIZE) {
            switch ((MINIMIZE)criterion) {
                case MAE:
                    result = m.getMAEtrain() + m.getMAEtest();
                    break;
                case ME:
                    result = m.getMEtrain() + m.getMEtest();
                    break;
                case MSE:
                    result = m.getMSEtrain() + m.getMSEtest();
                    break;
                case RMSE:
                    result = m.getRMSEtrain() + m.getRMSEtest();
                    break;
                case THEILSU:
                    result = m.getTheilUtrain() + m.getTheilUtest();
                    break;
                case RMSSE:
                    result = m.getRMSSEtrain() + m.getRMSSEtest();
                    break;
                case MPE:
                    result = m.getMPEtrain() + m.getMPEtest();
                    break;
                case MAPE:
                    result = m.getMAPEtrain() + m.getMAPEtest();
                    break;
                case MASE:
                    result = m.getMASEtrain() + m.getMASEtest();
                    break;
            }
        } else if (criterion instanceof MAXIMIZE) {
            //
        }
        
        return result;
    }
    
    public static boolean isCurrentBetterThanBest(Improvable criterion, double currentMeasures, double bestMeasures) {
        return criterion.isBetterThanBest(currentMeasures, bestMeasures);
    }
}
