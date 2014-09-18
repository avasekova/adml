package utils;

import gui.MainFrame;
import javax.swing.JFrame;
import org.rosuda.javaGD.GDInterface;

public class MyJavaGD extends GDInterface {
    public JFrame f;
    
    @Override
    public void gdOpen(double w, double h) {
        c = MainFrame.gdCanvas;
    }
}
