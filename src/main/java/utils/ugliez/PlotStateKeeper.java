package utils.ugliez;

import utils.Utils;

public class PlotStateKeeper {
    
    private static double lastDrawnCrispXmin;//last call, tj. new alebo zoom
    private static double lastDrawnCrispXmax;
    private static double lastDrawnCrispYmin;
    private static double lastDrawnCrispYmax;
    private static double crispXmin; //total, tj new
    private static double crispXmax;
    private static double crispYmin;
    private static double crispYmax;
    
    private static double lastDrawnIntXmin;
    private static double lastDrawnIntXmax;
    private static double lastDrawnIntYmin;
    private static double lastDrawnIntYmax;
    private static double intXmin;
    private static double intXmax;
    private static double intYmin;
    private static double intYmax;
    
    private static CallParams lastCallParams;

    public static double getLastDrawnCrispXmin() {
        return Utils.valToDecPoints(lastDrawnCrispXmin);
    }

    public static void setLastDrawnCrispXmin(double lastDrawnCrispXmin) {
        PlotStateKeeper.lastDrawnCrispXmin = lastDrawnCrispXmin;
    }

    public static double getLastDrawnCrispXmax() {
        return Utils.valToDecPoints(lastDrawnCrispXmax);
    }

    public static void setLastDrawnCrispXmax(double lastDrawnCrispXmax) {
        PlotStateKeeper.lastDrawnCrispXmax = lastDrawnCrispXmax;
    }

    public static double getLastDrawnCrispYmin() {
        return Utils.valToDecPoints(lastDrawnCrispYmin);
    }

    public static void setLastDrawnCrispYmin(double lastDrawnCrispYmin) {
        PlotStateKeeper.lastDrawnCrispYmin = lastDrawnCrispYmin;
    }

    public static double getLastDrawnCrispYmax() {
        return Utils.valToDecPoints(lastDrawnCrispYmax);
    }

    public static void setLastDrawnCrispYmax(double lastDrawnCrispYmax) {
        PlotStateKeeper.lastDrawnCrispYmax = lastDrawnCrispYmax;
    }

    public static double getCrispXmax() {
        return Utils.valToDecPoints(crispXmax);
    }

    public static void setCrispXmax(double crispXmax) {
        PlotStateKeeper.crispXmax = crispXmax;
    }

    public static double getCrispYmax() {
        return Utils.valToDecPoints(crispYmax);
    }

    public static void setCrispYmax(double crispYmax) {
        PlotStateKeeper.crispYmax = crispYmax;
    }

    public static double getLastDrawnIntXmin() {
        return Utils.valToDecPoints(lastDrawnIntXmin);
    }

    public static void setLastDrawnIntXmin(double lastDrawnIntXmin) {
        PlotStateKeeper.lastDrawnIntXmin = lastDrawnIntXmin;
    }

    public static double getLastDrawnIntXmax() {
        return Utils.valToDecPoints(lastDrawnIntXmax);
    }

    public static void setLastDrawnIntXmax(double lastDrawnIntXmax) {
        PlotStateKeeper.lastDrawnIntXmax = lastDrawnIntXmax;
    }

    public static double getLastDrawnIntYmin() {
        return Utils.valToDecPoints(lastDrawnIntYmin);
    }

    public static void setLastDrawnIntYmin(double lastDrawnIntYmin) {
        PlotStateKeeper.lastDrawnIntYmin = lastDrawnIntYmin;
    }

    public static double getLastDrawnIntYmax() {
        return Utils.valToDecPoints(lastDrawnIntYmax);
    }

    public static void setLastDrawnIntYmax(double lastDrawnIntYmax) {
        PlotStateKeeper.lastDrawnIntYmax = lastDrawnIntYmax;
    }

    public static double getIntXmax() {
        return Utils.valToDecPoints(intXmax);
    }

    public static void setIntXmax(double intXmax) {
        PlotStateKeeper.intXmax = intXmax;
    }

    public static double getIntYmax() {
        return Utils.valToDecPoints(intYmax);
    }

    public static void setIntYmax(double intYmax) {
        PlotStateKeeper.intYmax = intYmax;
    }

    public static CallParams getLastCallParams() {
        return lastCallParams;
    }

    public static void setLastCallParams(CallParams lastCallParams) {
        PlotStateKeeper.lastCallParams = lastCallParams;
    }

    public static double getCrispXmin() {
        return crispXmin;
    }

    public static void setCrispXmin(double crispXmin) {
        PlotStateKeeper.crispXmin = crispXmin;
    }

    public static double getCrispYmin() {
        return crispYmin;
    }

    public static void setCrispYmin(double crispYmin) {
        PlotStateKeeper.crispYmin = crispYmin;
    }

    public static double getIntXmin() {
        return intXmin;
    }

    public static void setIntXmin(double intXmin) {
        PlotStateKeeper.intXmin = intXmin;
    }

    public static double getIntYmin() {
        return intYmin;
    }

    public static void setIntYmin(double intYmin) {
        PlotStateKeeper.intYmin = intYmin;
    }
}
