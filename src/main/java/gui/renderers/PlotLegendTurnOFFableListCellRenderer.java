package gui.renderers;

import javax.swing.*;
import java.awt.*;

public class PlotLegendTurnOFFableListCellRenderer extends DefaultListCellRenderer {
    
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, hasFocus);
        
        JCheckBox checkBox = new JCheckBox("", ((PlotLegendTurnOFFableListElement)value).getReport().isVisible());
        checkBox.setBorder(null);
        
        JLabel fillLabel = new JLabel("  ");
        JLabel fillLabel2 = new JLabel("  ");
        
        JLabel colourLabel = new JLabel("   ");
        colourLabel.setOpaque(true); //setBkg does not work without this
        colourLabel.setBackground(((PlotLegendTurnOFFableListElement)value).getColour());
        
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
        line.add(checkBox);
        line.add(fillLabel);
        line.add(colourLabel);
        line.add(fillLabel2);
        line.add(textLabel);
        line.setVisible(true);
        
        //or maybe the checkboxes for the texts could be created here
        
        return line;
    }
}