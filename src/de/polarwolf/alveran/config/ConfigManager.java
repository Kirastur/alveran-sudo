package de.polarwolf.alveran.config;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import de.polarwolf.alveran.exception.AlveranException;
import de.polarwolf.alveran.orchestrator.AlveranOrchestrator;
import de.polarwolf.alveran.text.Message;
import de.polarwolf.alveran.text.TextManager;

public class ConfigManager {

	public static final String MESSAGE_FILENAME = "messages.yml";
	public static final String SECTION_STARTUP = "startup";
	public static final String PARAM_STARTUP_PASSIVEMODE = "passiveMode";
	public static final String PARAM_STARTUP_SANCTUARY = "enableSanctuary";

	public static final boolean DEFAULT_STARTUP_PASSIVEMODE = false;
	public static final boolean DEFAULT_STARTUP_SANCTUARY = false;

	protected final Plugin plugin;
	protected final TextManager textManager;
	protected AlveranConfig alveranConfig = new AlveranConfig();

	public ConfigManager(AlveranOrchestrator orchestrator) {
		this.plugin = orchestrator.getPlugin();
		this.textManager = orchestrator.getTextManager();
	}

	//
	// Startup section
	//

	public static boolean isPassiveMode(Plugin startupPlugin) {
		return startupPlugin.getConfig().getConfigurationSection(SECTION_STARTUP).getBoolean(PARAM_STARTUP_PASSIVEMODE,
				DEFAULT_STARTUP_PASSIVEMODE);
	}

	public static boolean isEnableSanctuary(Plugin startupPlugin) {
		return startupPlugin.getConfig().getConfigurationSection(SECTION_STARTUP).getBoolean(PARAM_STARTUP_SANCTUARY,
				DEFAULT_STARTUP_SANCTUARY);
	}

	//
	// Alveran Config Section
	//

	public boolean canPlayerExecuteCommand() {
		return alveranConfig.canPlayerExecuteCommand();
	}

	public boolean isEnableDebug() {
		return alveranConfig.isEnableDebug();
	}

	public String getRegion() {
		return alveranConfig.getRegion();
	}

	public String getActorPermission() {
		return alveranConfig.getActorPermission();
	}

	public String getReceivePermission() {
		return alveranConfig.getReceivePermission();
	}

	public String getDestinationGroup() {
		return alveranConfig.getDestinationGroup();
	}

	public int getBlessingDuration() {
		return alveranConfig.getBlessingDuration();
	}

	public boolean isUnblessOnLeave() {
		return alveranConfig.isUnblessOnLeave();
	}

	public boolean isNotifyOnUnbless() {
		return alveranConfig.isNotifyOnUnbless();
	}

	public String getNotifySound() {
		return alveranConfig.getNotifySound();
	}

	//
	// Reload
	//

	public void reloadMessages() throws AlveranException {
		File messageFile = new File(plugin.getDataFolder(), MESSAGE_FILENAME);
		if (!messageFile.exists()) {
			throw new AlveranException(null, Message.CONFIG_MESSAGE_FILE_MISSING, MESSAGE_FILENAME);
		}

		FileConfiguration fileConfiguration;
		try {
			fileConfiguration = YamlConfiguration.loadConfiguration(messageFile);
		} catch (Exception e) {
			throw new AlveranException(null, Message.YAML_PARSE_ERROR, MESSAGE_FILENAME, e);
		}

		textManager.clear();
		for (String myKey : fileConfiguration.getKeys(false)) {
			String myText = fileConfiguration.getString(myKey);
			textManager.addText(myKey, myText);
		}
	}

	public void reloadConfig() {
		AlveranConfig newAlveranConfig = new AlveranConfig(plugin.getConfig().getRoot());
		alveranConfig = newAlveranConfig;
	}

}
