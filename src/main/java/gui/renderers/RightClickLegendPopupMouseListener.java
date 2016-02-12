package gui.renderers;

import models.TrainAndTestReport;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 *
 * @author xvasekov
 */
public class RightClickLegendPopupMouseListener extends MouseAdapter {
    private final JList listPlotLegend;
    private final RightClickable outer;

    public RightClickLegendPopupMouseListener(final JList listPlotLegend, final RightClickable outer) {
        this.outer = outer;
        this.listPlotLegend = listPlotLegend;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //tu nerob nic, az v Released
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!displayPopup(e)) {
            if (outer instanceof PlotLegendTurnOFFableListElement) {
                //invert the selection state of the checkbox
                outer.getReport().setVisible(!outer.getReport().isVisible());
                RightClickable.redrawPlots(listPlotLegend);
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        displayPopup(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        displayPopup(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        displayPopup(e);
    }

    private boolean displayPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            listPlotLegend.setSelectedIndex(listPlotLegend.locationToIndex(e.getPoint())); //select the item
            outer.getMenu().show(listPlotLegend/*.getParent()*/, e.getX(), e.getY());
        }
        return e.isPopupTrigger();
    }
    
}
