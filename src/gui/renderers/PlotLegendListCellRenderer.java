package gui.renderers;

import gui.Plottable;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class PlotLegendListCellRenderer extends DefaultListCellRenderer {
    
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, hasFocus);
        
        Color colour = Color.decode(((Plottable) value).getColourInPlot());
        JLabel colourLabel = new JLabel("   ");
        colourLabel.setOpaque(true); //bez toho nefunguje setBkg
        colourLabel.setBackground(colour);
        
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