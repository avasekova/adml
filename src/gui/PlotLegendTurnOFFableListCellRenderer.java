package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class PlotLegendTurnOFFableListCellRenderer extends DefaultListCellRenderer {
    
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, hasFocus);
        
        JCheckBox checkBox = new JCheckBox("", true); //no text, and selected
        checkBox.setBorder(null);
        
        JLabel fillLabel = new JLabel("  ");
        JLabel fillLabel2 = new JLabel("  ");
        
        Color colour = Color.decode(((Plottable) value).getColourInPlot());
        JLabel colourLabel = new JLabel("   ");
        colourLabel.setOpaque(true); //bez toho nefunguje setBkg
        colourLabel.setBackground(colour);
        
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
        
        //alebo by sa tu mohli vytvarat a returnovat aj checkboxy k tym textom!
        
        return line;
    }
}