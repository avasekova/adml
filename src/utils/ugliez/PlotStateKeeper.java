package utils.ugliez;

public class PlotStateKeeper {
    
    private static int lastDrawnCrispXmax;
    private static double lastDrawnCrispYmax;
    private static int lastDrawnIntXmax;
    private static double lastDrawnIntYmax;
    private static CallParams lastCallParams;

    public static int getLastDrawnCrispXmax() {
        return lastDrawnCrispXmax;
    }

    public static void setLastDrawnCrispXmax(int lastDrawnCrispXmax) {
        PlotStateKeeper.lastDrawnCrispXmax = lastDrawnCrispXmax;
    }

    public static double getLastDrawnCrispYmax() {
        return lastDrawnCrispYmax;
    }

    public static void setLastDrawnCrispYmax(double lastDrawnCrispYmax) {
        PlotStateKeeper.lastDrawnCrispYmax = lastDrawnCrispYmax;
    }

    public static int getLastDrawnIntXmax() {
        return lastDrawnIntXmax;
    }

    public static void setLastDrawnIntXmax(int lastDrawnIntXmax) {
        PlotStateKeeper.lastDrawnIntXmax = lastDrawnIntXmax;
    }

    public static double getLastDrawnIntYmax() {
        return lastDrawnIntYmax;
    }

    public static void setLastDrawnIntYmax(double lastDrawnIntYmax) {
        PlotStateKeeper.lastDrawnIntYmax = lastDrawnIntYmax;
    }
    
    public static CallParams getLastCallParams() {
        return lastCallParams;
    }

    public static void setLastCallParams(CallParams lastCallParams) {
        PlotStateKeeper.lastCallParams = lastCallParams;
    }
}
