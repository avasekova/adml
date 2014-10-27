package utils.ugliez;

public class PlotStateKeeper {
    
    private static double lastDrawnCrispXmin;//last call, tj. new alebo zoom
    private static double lastDrawnCrispXmax;
    private static double lastDrawnCrispYmin;
    private static double lastDrawnCrispYmax;
    private static double crispXmax; //total, tj new
    private static double crispYmax;
    
    private static double lastDrawnIntXmin;
    private static double lastDrawnIntXmax;
    private static double lastDrawnIntYmin;
    private static double lastDrawnIntYmax;
    private static double intXmax;
    private static double intYmax;
    
    private static CallParams lastCallParams;

    public static double getLastDrawnCrispXmin() {
        return lastDrawnCrispXmin;
    }

    public static void setLastDrawnCrispXmin(double lastDrawnCrispXmin) {
        PlotStateKeeper.lastDrawnCrispXmin = lastDrawnCrispXmin;
    }

    public static double getLastDrawnCrispXmax() {
        return lastDrawnCrispXmax;
    }

    public static void setLastDrawnCrispXmax(double lastDrawnCrispXmax) {
        PlotStateKeeper.lastDrawnCrispXmax = lastDrawnCrispXmax;
    }

    public static double getLastDrawnCrispYmin() {
        return lastDrawnCrispYmin;
    }

    public static void setLastDrawnCrispYmin(double lastDrawnCrispYmin) {
        PlotStateKeeper.lastDrawnCrispYmin = lastDrawnCrispYmin;
    }

    public static double getLastDrawnCrispYmax() {
        return lastDrawnCrispYmax;
    }

    public static void setLastDrawnCrispYmax(double lastDrawnCrispYmax) {
        PlotStateKeeper.lastDrawnCrispYmax = lastDrawnCrispYmax;
    }

    public static double getCrispXmax() {
        return crispXmax;
    }

    public static void setCrispXmax(double crispXmax) {
        PlotStateKeeper.crispXmax = crispXmax;
    }

    public static double getCrispYmax() {
        return crispYmax;
    }

    public static void setCrispYmax(double crispYmax) {
        PlotStateKeeper.crispYmax = crispYmax;
    }

    public static double getLastDrawnIntXmin() {
        return lastDrawnIntXmin;
    }

    public static void setLastDrawnIntXmin(double lastDrawnIntXmin) {
        PlotStateKeeper.lastDrawnIntXmin = lastDrawnIntXmin;
    }

    public static double getLastDrawnIntXmax() {
        return lastDrawnIntXmax;
    }

    public static void setLastDrawnIntXmax(double lastDrawnIntXmax) {
        PlotStateKeeper.lastDrawnIntXmax = lastDrawnIntXmax;
    }

    public static double getLastDrawnIntYmin() {
        return lastDrawnIntYmin;
    }

    public static void setLastDrawnIntYmin(double lastDrawnIntYmin) {
        PlotStateKeeper.lastDrawnIntYmin = lastDrawnIntYmin;
    }

    public static double getLastDrawnIntYmax() {
        return lastDrawnIntYmax;
    }

    public static void setLastDrawnIntYmax(double lastDrawnIntYmax) {
        PlotStateKeeper.lastDrawnIntYmax = lastDrawnIntYmax;
    }

    public static double getIntXmax() {
        return intXmax;
    }

    public static void setIntXmax(double intXmax) {
        PlotStateKeeper.intXmax = intXmax;
    }

    public static double getIntYmax() {
        return intYmax;
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
}
