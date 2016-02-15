package gui.renderers;

import gui.Plottable;
import utils.Const;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

public class PlotLegendSimpleListElement extends JPanel implements RightClickable {
    
    private final Plottable report;
    private final JPopupMenu menu;
    
    public PlotLegendSimpleListElement(Plottable report, final JList<RightClickable> listPlotLegend) {
        super();
        this.report = report;

        MouseListener mListener = new RightClickLegendPopupMouseListener(listPlotLegend, this);
        this.addMouseListener(mListener);

        menu = new JPopupMenu();

        JMenuItem item = new JMenuItem(Const.CHANGE_COLOUR);
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color chosenColor = JColorChooser.showDialog(null, "Select Colour", Color.BLACK);
                if (chosenColor != null) {
                    listPlotLegend.getSelectedValue().getReport()
                            .setColourInPlot(String.format("#%02X%02X%02X", chosenColor.getRed(),
                                    chosenColor.getGreen(), chosenColor.getBlue())); //found this at SO
                    RightClickable.redrawPlots(listPlotLegend);
                }
            }
        });
        menu.add(item);
    }

    @Override
    public Plottable getReport() {
        return report;
    }
    
    public Color getColour() {
        return Color.decode(report.getColourInPlot());
    }
    
    @Override
    public String toString() {
        return report.toString();
    }
    
    @Override
    public JPopupMenu getMenu() {
        return menu;
    }
}
