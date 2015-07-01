package utils;

import gui.MainFrame;
import org.rosuda.javaGD.GDInterface;

public class MyJavaGD extends GDInterface {
    
    @Override
    public void gdOpen(double w, double h) {
        c = MainFrame.drawNowToThisGDBufferedPanel;
    }
}
