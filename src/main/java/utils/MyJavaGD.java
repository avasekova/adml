package utils;

import gui.PlotDrawer;
import org.rosuda.javaGD.GDInterface;

public class MyJavaGD extends GDInterface {
    
    @Override
    public void gdOpen(double w, double h) {
        c = PlotDrawer.getDrawNowToThisGDBufferedPanel();
    }
}
