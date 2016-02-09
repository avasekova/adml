package utils;

import gui.PlotDrawer;
import org.rosuda.javaGD.GDInterface;

public class MyJavaGD extends GDInterface {
    
    @Override
    public void gdOpen(double w, double h) {
        super.gdOpen(w, h); //TODO este odsledovat, co sa zmeni, ked teraz "this.open = true;"
        c = PlotDrawer.getDrawNowToThisGDBufferedPanel();
    }
}
