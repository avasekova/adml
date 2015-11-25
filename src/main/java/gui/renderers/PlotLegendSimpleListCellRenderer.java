package gui.renderers;

import gui.Plottable;

import javax.swing.*;
import java.awt.*;

public class PlotLegendSimpleListCellRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, hasFocus);
        
        JLabel colourLabel = new JLabel("   ");
        colourLabel.setOpaque(true); //bez toho nefunguje setBkg
        if (value instanceof PlotLegendSimpleListElement) {
            colourLabel.setBackground(((PlotLegendSimpleListElement)value).getColour());
        } else {
            colourLabel.setBackground(Color.decode(((Plottable)value).getColourInPlot()));
        }
        
        
        JLabel fillLabel = new JLabel("  ");
        
        JLabel textLabel = new JLabel(value.toString());
        textLabel.setOpaque(true);
        if (isSelected) {
            textLabel.setForeground(Color.WHITE);
            textLabel.setBackground(UIManager.getDefaults().getColor("List.selectionBackground"));
        } else {
            textLabel.setBackground(Color.WHITE);
        }
        
        JPanel line = new JPanel();
        line.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        line.setBackground(Color.WHITE);
        line.add(colourLabel);
        line.add(fillLabel);
        line.add(textLabel);
        line.setVisible(true);
        
        //alebo by sa tu mohli vytvarat a returnovat aj checkboxy k tym textom!
        
        return line;
    }
}