package de.polarwolf.alveran.main;

import org.bukkit.plugin.java.JavaPlugin;

import de.polarwolf.alveran.api.AlveranAPI;
import de.polarwolf.alveran.bstats.MetricsLite;
import de.polarwolf.alveran.commands.AlveranCommand;
import de.polarwolf.alveran.config.AlveranConfig;
import de.polarwolf.alveran.events.LuckPermsListener;
import de.polarwolf.alveran.events.PlayerListener;
import de.polarwolf.alveran.providers.AlveranProvider;


public class Main extends JavaPlugin{
	
	private AlveranConfig alveranConfig;
	private AlveranAPI alveranAPI;
	private AlveranProvider alveranProvider;
	private LuckPermsListener luckPermsListener; 
	
	public void onEnable() {
		
		// Load Config
		alveranConfig = new AlveranConfig(this);
		
		// Initialize AlveranAPI
		alveranAPI = new AlveranAPI(this);
		
		// Initialize AlveranProvider
		alveranProvider = new AlveranProvider(this);

		// Register Alveran command to perform blessing
		getCommand("alveran").setExecutor(new AlveranCommand(this));
		
		// Register for Player events to remove blessing on logout
		getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
		
		// Register for LuckPerms events for blessing removal
		luckPermsListener = new LuckPermsListener(this);

		// Enable bStats Metrics
		int pluginId = 9788; // Metrics of "Alveran" from "Kirastur"
        @SuppressWarnings("unused")
		MetricsLite metrics = new MetricsLite(this, pluginId);

		getLogger().info("Alveran is ready to bless players");
	}
	
	public AlveranConfig getAlveranConfig() {
		return alveranConfig;
	}
	
	public AlveranAPI getAlveranAPI() {
		return alveranAPI;
	}

	public AlveranProvider getAlveranProvider() {
		return alveranProvider;
	}
	
	public LuckPermsListener getLuckPermsListener() {
		return luckPermsListener;
	}
	public void onDisable() {
		alveranProvider.UnloadAPI();
		getLogger().info("Alveran has closed its gate. Blessings are used up.");
	}
		
}
