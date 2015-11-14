package gui.renderers;

import gui.Plottable;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JColorChooser;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import utils.Const;

//TODO refactor v suvislosti s PlotLegendTurnOFFableListElement
public class PlotLegendSimpleListElement extends JPanel implements RightClickable {
    
    private final Plottable report;
    private final JPopupMenu menu;
    
    public PlotLegendSimpleListElement(Plottable report, final JList listPlotLegend) {
        super();
        this.report = report;
        
        ActionListener menuListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (e.getActionCommand()) {
                    case Const.CHANGE_COLOUR:
                        Color chosenColor = JColorChooser.showDialog(null, "Select Colour", Color.BLACK);
                        if (chosenColor != null) {
                            ((PlotLegendSimpleListElement) listPlotLegend.getSelectedValue()).report
                                    .setColourInPlot(String.format("#%02X%02X%02X", chosenColor.getRed(), 
                                            chosenColor.getGreen(), chosenColor.getBlue())); //found this at SO
                            RightClickable.redrawPlots(listPlotLegend, new ArrayList<>());
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("unknown selection in menu");
                }
            }
        };
        
        menu = new JPopupMenu();
        
        JMenuItem item;        
        menu.add(item = new JMenuItem(Const.CHANGE_COLOUR));
        item.addActionListener(menuListener);
        
        
        MouseListener mListener = new RightClickLegendPopupMouseListener(listPlotLegend, new ArrayList<>(), this);
        this.addMouseListener(mListener);
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
