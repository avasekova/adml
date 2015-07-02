package analysis;

import gui.tablemodels.DataTableModel;
import java.util.List;
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
    
}
