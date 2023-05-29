package de.polarwolf.alveran.main;

import java.io.File;
import java.util.UUID;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import de.polarwolf.alveran.api.AlveranProvider;
import de.polarwolf.alveran.commands.AlveranCommand;
import de.polarwolf.alveran.config.ConfigManager;
import de.polarwolf.alveran.orchestrator.AlveranAPI;
import de.polarwolf.alveran.orchestrator.StartOptions;

public final class Main extends JavaPlugin {

	public static final int PLUGINID_AVLERAN = 9788;
	public static final String COMMAND_NAME = "alveran";

	protected UUID apiToken = null;
	protected AlveranAPI alveranAPI = null;

	@Override
	public void onEnable() {

		// Copy config from .jar if it dosn't exist
		saveDefaultConfig();

		// Copy message file frim .jar if it doesn't exist
		if (!new File(getDataFolder(), ConfigManager.MESSAGE_FILENAME).exists()) {
			saveResource(ConfigManager.MESSAGE_FILENAME, false);
		}

		// Generate our API Token
		apiToken = UUID.randomUUID();

		// Enable bStats Metrics
		new Metrics(this, PLUGINID_AVLERAN);

		// Register Command and TabCompleter
		new AlveranCommand(this, COMMAND_NAME);

		// Check for passive mode
		if (ConfigManager.isPassiveMode(this)) {
			getLogger().info("Alveran is in passive mode.");
			return;
		}

		// Start API
		boolean bEnableSanctuary = ConfigManager.isEnableSanctuary(this);
		StartOptions startOptions = new StartOptions(bEnableSanctuary);
		try {
			alveranAPI = new AlveranAPI(this, apiToken, startOptions);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Register API to Provider
		AlveranProvider.setAPI(alveranAPI);

		// Now initialization is done and we can print the finish message
		getLogger().info("Alveran is ready to bless player");

	}

	@Override
	public void onDisable() {
		if (alveranAPI != null) {
			alveranAPI.disable(apiToken);
			alveranAPI = null;
		}
		AlveranProvider.setAPI(null);
		getLogger().info("Alveran has closed its gate. Blessings are used up.");
	}

}
