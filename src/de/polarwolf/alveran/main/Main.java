package de.polarwolf.alveran.main;

import org.bukkit.plugin.java.JavaPlugin;

import de.polarwolf.alveran.api.AlveranAPI;
import de.polarwolf.alveran.bstats.MetricsLite;
import de.polarwolf.alveran.commands.AlveranCommand;
import de.polarwolf.alveran.commands.AlveranTabCompleter;
import de.polarwolf.alveran.config.AlveranConfig;
import de.polarwolf.alveran.events.LuckPermsListener;
import de.polarwolf.alveran.events.PlayerListener;


public final class Main extends JavaPlugin {
	
	
	@Override
	public void onEnable() {
		
		// Load Config
		AlveranConfig alveranConfig = new AlveranConfig(this);
		
		// Initialize AlveranAPI
		AlveranAPI alveranAPI = new AlveranAPI(this, alveranConfig);
		
		// Initialize AlveranProvider
		if (alveranConfig.getEnableAPI()) {
			AlveranProvider.setAPI(alveranAPI);
		}

		// Register Alveran command to perform blessing
		getCommand("alveran").setExecutor(new AlveranCommand(this, alveranConfig, alveranAPI));
		getCommand("alveran").setTabCompleter(new AlveranTabCompleter(this));
		
		// Register for Player events to remove blessing on logout
		getServer().getPluginManager().registerEvents(new PlayerListener(alveranConfig, alveranAPI), this);
		
		// Register for LuckPerms events for blessing removal
		new LuckPermsListener(this, alveranConfig);

		// Enable bStats Metrics
		int pluginId = 9788; // Metrics of "Alveran" from "Kirastur"
		new MetricsLite(this, pluginId);

		getLogger().info("Alveran is ready to bless players");
	}
	
	@Override
	public void onDisable() {
		AlveranProvider.setAPI(null);
		getLogger().info("Alveran has closed its gate. Blessings are used up.");
	}
		
}
