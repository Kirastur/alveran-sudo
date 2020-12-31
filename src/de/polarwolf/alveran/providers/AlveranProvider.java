package de.polarwolf.alveran.providers;

import de.polarwolf.alveran.api.AlveranAPI;
import de.polarwolf.alveran.main.Main;

public class AlveranProvider{
	
	private static Main main;

    @SuppressWarnings("static-access")
	public AlveranProvider(Main main) {
    	this.main = main;
    }
    
    public static AlveranAPI getAPI() {
    	if (!(main == null)) {
    		if(main.getAlveranConfig().getEnableAPI()) { 
    			return  main.getAlveranAPI();
    		}
    	}
   		return null;
    	    }
    
    public void UnloadAPI() {
    	main = null;
    }
    
}
