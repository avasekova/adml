package models;

import gui.DataTableModel;
import java.util.ArrayList;
import java.util.List;
import params.Params;
import params.RBFParams;
import utils.IntervalExplanatoryVariable;
import utils.IntervalOutputVariable;

public class RBF {

    public TrainAndTestReport forecast(DataTableModel dataTableModel, Params parameters) {
        
        
        //ako v iMLP C code:
        // - trimnut vstupy podla lagov do obdlznika
        // - bachnut to funkcii na vstup
        // - zalagovat vystupy a podobne
        // - ale ako Crisp modely: vyrobit si Plot.
        
        RBFParams params = (RBFParams) parameters;
        TrainAndTestReport report = new TrainAndTestReportCrisp("RBF");
        report.setModelDescription("");
        
        List<List<Double>> data = prepareData(dataTableModel, params.getExplVars(), 
                params.getOutVars(), params.getDataRangeFrom()-1, params.getDataRangeTo());
        
        return report;
    }
    
    //podla vzoru iMLP C code
    private List<List<Double>> prepareData(DataTableModel dataTableModel, List<IntervalExplanatoryVariable> explVars, 
                                                                          List<IntervalOutputVariable> outVars,
                                                                          int from, int to) {
        List<List<Double>> data = new ArrayList<>();
        
        int maxLag = 0;
        for (IntervalExplanatoryVariable var : explVars) {
//            List<Double> vals;
//            
//            if (var.getIntervalNames() instanceof IntervalNamesCentreRadius) {
//                centers = dataTableModel.getDataForColname(((IntervalNamesCentreRadius)var.getIntervalNames()).getCentre()).subList(from, to);
//                radii = dataTableModel.getDataForColname(((IntervalNamesCentreRadius)var.getIntervalNames()).getRadius()).subList(from, to);
//            } else {
//                //we have LB and UB
//                List<Double> lowers = dataTableModel.getDataForColname(((IntervalNamesLowerUpper)var.getIntervalNames()).getLowerBound()).subList(from, to);
//                List<Double> uppers = dataTableModel.getDataForColname(((IntervalNamesLowerUpper)var.getIntervalNames()).getUpperBound()).subList(from, to);
//                centers = new ArrayList<>();
//                radii = new ArrayList<>();
//                for (int i = 0; i < lowers.size(); i++) {
//                    centers.add((uppers.get(i) + lowers.get(i))/2);
//                    radii.add((uppers.get(i) - lowers.get(i))/2);
//                }
//                
//            }
//            
//            data.add(lagBy(var.getLag(), centers));
//            data.add(lagBy(var.getLag(), radii));
//            
//            maxLag = Math.max(maxLag, var.getLag());
        }
        
        for (IntervalOutputVariable var : outVars) {
//            List<Double> centers;
//            List<Double> radii;
//            
//            if (var.getIntervalNames() instanceof IntervalNamesCentreRadius) {
//                centers = dataTableModel.getDataForColname(((IntervalNamesCentreRadius)var.getIntervalNames()).getCentre()).subList(from, to);
//                radii = dataTableModel.getDataForColname(((IntervalNamesCentreRadius)var.getIntervalNames()).getRadius()).subList(from, to);
//            } else {
//                //we have LB and UB
//                List<Double> lowers = dataTableModel.getDataForColname(((IntervalNamesLowerUpper)var.getIntervalNames()).getLowerBound()).subList(from, to);
//                List<Double> uppers = dataTableModel.getDataForColname(((IntervalNamesLowerUpper)var.getIntervalNames()).getUpperBound()).subList(from, to);
//                centers = new ArrayList<>();
//                radii = new ArrayList<>();
//                for (int i = 0; i < lowers.size(); i++) {
//                    centers.add((uppers.get(i) + lowers.get(i))/2);
//                    radii.add((uppers.get(i) - lowers.get(i))/2);
//                }
//                
//            }
//            
//            data.add(centers);
//            data.add(radii);
        }
        
        return IntervalMLPCcode.trimDataToRectangle(data, maxLag);
    }
}
