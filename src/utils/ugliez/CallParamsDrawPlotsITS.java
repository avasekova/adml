package utils.ugliez;

import gui.DataTableModel;
import java.util.List;
import org.rosuda.javaGD.JGDBufferedPanel;
import utils.imlp.IntervalNamesCentreRadius;
import utils.imlp.IntervalNamesLowerUpper;

public class CallParamsDrawPlotsITS extends CallParams {
    
    private JGDBufferedPanel canvasToUse;
    private int width;
    private int height;
    private DataTableModel dataTableModel;
    private List<IntervalNamesCentreRadius> listCentreRadius;
    private List<IntervalNamesLowerUpper> listLowerUpper;

    public CallParamsDrawPlotsITS(JGDBufferedPanel canvasToUse, int width, int height, DataTableModel dataTableModel, List<IntervalNamesCentreRadius> listCentreRadius, List<IntervalNamesLowerUpper> listLowerUpper) {
        this.canvasToUse = canvasToUse;
        this.width = width;
        this.height = height;
        this.dataTableModel = dataTableModel;
        this.listCentreRadius = listCentreRadius;
        this.listLowerUpper = listLowerUpper;
    }
    
    public JGDBufferedPanel getCanvasToUse() {
        return canvasToUse;
    }

    public void setCanvasToUse(JGDBufferedPanel canvasToUse) {
        this.canvasToUse = canvasToUse;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
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
