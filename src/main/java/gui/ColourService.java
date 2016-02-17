package gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ColourService {
    
    //TODO steal ideas from sth like:
    ///https://github.com/mbostock/d3/blob/master/lib/colorbrewer/colorbrewer.js
    ///https://github.com/mbostock/d3/wiki/Ordinal-Scales
    
    private static ColourService instance = null;
    private int counter = -1;
    private static final String[] COLOURS = new String[]{ //TODO or maybe just choose some nice colours here
//        //D3js colours:
//        "#1F77B4",
//        "#FF7F0E",
//        "#2CA02C",
//        "#D62728",
//        "#9467BD",
//        "#8C564B",
//        "#E377C2",
//        "#7F7F7F",
//        "#BCBD22",
//        "#17BECF",
//        "#AEC7E8",
//        "#FFBB78",
//        "#98DF8A",
//        "#FF9896",
//        "#C5B0D5",
//        "#C49C94",
//        "#F7B6D2",
//        "#C7C7C7",
//        "#DBDB8D",
//        "#9EDAE5",
        
        //TODO add more colours!
        "#FF00FF",
        "#0000FF",
        "#00CD00",
        "#42E99D", //turquoise
        "#FF7A4B", //orange
        "#EA0D5B", //pink
        "#1B60C5", //blue
        "#FFEA48", //yellow
        "#44F04F", //green
        "#820000", //brown
        "#05787E"  //steel
            
            
        //desperate times call for desperate measures, repeat the colours:
        ,"#FF00FF",
        "#0000FF",
        "#00CD00",
        "#42E99D", //turquoise
        "#FF7A4B", //orange
        "#EA0D5B", //pink
        "#1B60C5", //blue
        "#FFEA48", //yellow
        "#44F04F", //green
        "#820000", //brown
        "#05787E"  //steel
    };
    
    private ColourService() { }
    
    public static synchronized ColourService getService() {
        if (instance == null) {
            instance = new ColourService();
        }
        
        return instance;
    }
    
    public void resetCounter() {
        counter = -1;
    }
    
    public String getLastColour() {
        return COLOURS[counter % COLOURS.length];
    }
    
    public String getNewColour() {
        counter++;
        return getLastColour();
    }
}
