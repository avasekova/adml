package gui;
public class ColourService {
    
    //TODO vykradnut nieco ako:
    ///https://github.com/mbostock/d3/blob/master/lib/colorbrewer/colorbrewer.js
    ///https://github.com/mbostock/d3/wiki/Ordinal-Scales
    
    private static ColourService instance = null;
    private int counter = -1;
    private static final String[] COLOURS = new String[]{ //TODO vybrat sem nejake pekne! (rucne) - a hlavne viac
//        //farby z D3js:
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
        
        //TODO urgentne pridat viac farieb, aby to nebolo treba modulit!
        "#FF00FF",
        "#0000FF",
        "#00CD00",
        "#42E99D", //tyrkysova
        "#FF7A4B", //oranzova
        "#EA0D5B", //ruzova
        "#1B60C5", //modra
        "#FFEA48", //zlta
        "#44F04F", //zelena
        "#820000", //hneda
        "#05787E" //ocelova
            
            
        //desperate times call for desperate measures, zopakovane tie farby:
        ,"#FF00FF",
        "#0000FF",
        "#00CD00",
        "#42E99D", //tyrkysova
        "#FF7A4B", //oranzova
        "#EA0D5B", //ruzova
        "#1B60C5", //modra
        "#FFEA48", //zlta
        "#44F04F", //zelena
        "#820000", //hneda
        "#05787E" //ocelova
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
