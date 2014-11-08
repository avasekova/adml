package utils.ugliez;

import gui.DataTableModel;
import java.util.List;
import javax.swing.JList;
import org.rosuda.javaGD.JGDBufferedPanel;
import utils.imlp.IntervalNamesCentreRadius;
import utils.imlp.IntervalNamesLowerUpper;

public class CallParamsDrawPlotsITS extends CallParams {
    
    private DataTableModel dataTableModel;
    private List<IntervalNamesCentreRadius> listCentreRadius;
    private List<IntervalNamesLowerUpper> listLowerUpper;

    public CallParamsDrawPlotsITS(JList listPlotLegend, JGDBufferedPanel canvasToUse, int width, int height, 
            DataTableModel dataTableModel, List<IntervalNamesCentreRadius> listCentreRadius, 
            List<IntervalNamesLowerUpper> listLowerUpper) {
        super(listPlotLegend, canvasToUse, width, height);
        this.dataTableModel = dataTableModel;
        this.listCentreRadius = listCentreRadius;
        this.listLowerUpper = listLowerUpper;
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
}
