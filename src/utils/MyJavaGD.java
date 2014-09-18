package utils;

import gui.MainFrame;
import javax.swing.JFrame;
import org.rosuda.javaGD.GDInterface;

public class MyJavaGD extends GDInterface {  //TODO potom naparametrizovat, aby sa tam dal menit nazov grafu a podobne
    public JFrame f;
    
    @Override
    public void gdOpen(double w, double h) {
        System.out.println("bol som tu");
        c = MainFrame.gdCanvas;
    }
}
