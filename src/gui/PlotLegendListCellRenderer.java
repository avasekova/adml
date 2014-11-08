package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.UIManager;
import models.TrainAndTestReport;

public class PlotLegendListCellRenderer extends DefaultListCellRenderer {
    
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, hasFocus);
        
        Color colour = Color.decode(((Plottable) value).getColourInPlot());
        JLabel colourLabel = new JLabel("   ");
        colourLabel.setOpaque(true); //bez toho nefunguje setBkg
        colourLabel.setBackground(colour);
        
        JLabel textLabel = new JLabel("   " + value.toString());
        textLabel.setOpaque(true);
        if (isSelected) {
            textLabel.setForeground(Color.WHITE);
            textLabel.setBackground(UIManager.getDefaults().getColor("List.selectionBackground"));
        } else {
            textLabel.setBackground(Color.WHITE);
        }
        
        JPanel line = new JPanel();
        line.setLayout(new BorderLayout());
        line.add(colourLabel, BorderLayout.WEST);
        line.add(textLabel);
        line.setVisible(true);
        
        //alebo by sa tu mohli vytvarat a returnovat aj checkboxy k tym textom!
        
        return line;
    }
}