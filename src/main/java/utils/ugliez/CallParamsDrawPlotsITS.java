package utils.ugliez;

import gui.tablemodels.DataTableModel;
import org.rosuda.javaGD.JGDBufferedPanel;
import utils.imlp.IntervalNamesCentreRadius;
import utils.imlp.IntervalNamesLowerUpper;

import javax.swing.*;
import java.util.List;

public class CallParamsDrawPlotsITS extends CallParams {
    
    private DataTableModel dataTableModel;
    private List<IntervalNamesCentreRadius> listCentreRadius;
    private List<IntervalNamesLowerUpper> listLowerUpper;
    private boolean scatterplot;

    public CallParamsDrawPlotsITS(JList listPlotLegend, JGDBufferedPanel canvasToUse, int width, int height, 
            DataTableModel dataTableModel, List<IntervalNamesCentreRadius> listCentreRadius, 
            List<IntervalNamesLowerUpper> listLowerUpper, boolean scatterplot) {
        super(listPlotLegend, canvasToUse, width, height);
        this.dataTableModel = dataTableModel;
        this.listCentreRadius = listCentreRadius;
        this.listLowerUpper = listLowerUpper;
        this.scatterplot = scatterplot;
    }
    
    public DataTableModel getDataTableModel() {
        return dataTableModel;
    }

    public void setDataTableModel(DataTableModel dataTableModel) {
        this.dataTableModel = dataTableModel;
    }

    public List<IntervalNamesCentreRadius> getListCentreRadius() {
        return listCentreRadius;
    }

    public void setListCentreRadius(List<IntervalNamesCentreRadius> listCentreRadius) {
        this.listCentreRadius = listCentreRadius;
    }

    public List<IntervalNamesLowerUpper> getListLowerUpper() {
        return listLowerUpper;
    }

    public void setListLowerUpper(List<IntervalNamesLowerUpper> listLowerUpper) {
        this.listLowerUpper = listLowerUpper;
    }

    public boolean isScatterplot() {
        return scatterplot;
    }

    public void setScatterplot(boolean scatterplot) {
        this.scatterplot = scatterplot;
    }
}
