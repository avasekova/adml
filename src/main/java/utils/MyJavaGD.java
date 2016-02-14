package utils;

import gui.PlotDrawer;
import org.rosuda.javaGD.GDInterface;

public class MyJavaGD extends GDInterface {
    
    @Override
    public void gdOpen(double w, double h) {
        super.gdOpen(w, h);
        c = PlotDrawer.getDrawNowToThisGDBufferedPanel();
    }
}
