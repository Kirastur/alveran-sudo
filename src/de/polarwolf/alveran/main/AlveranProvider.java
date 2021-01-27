package de.polarwolf.alveran.main;

import de.polarwolf.alveran.api.AlveranAPI;

public class AlveranProvider{
	
	private static AlveranAPI alveranAPI;
	
	private AlveranProvider () {
	}

    protected static void setAPI (AlveranAPI newAPI) {
    	alveranAPI=newAPI;
    }
    
    public static AlveranAPI getAPI() {
    	return alveranAPI;
    }
    
}
