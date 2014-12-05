package gui;
public class ColourService {
    
    private static ColourService instance = null;
    private int counter = -1;
    private static final String[] COLOURS = new String[]{ //TODO vybrat sem nejake pekne! (rucne) - a hlavne viac
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
