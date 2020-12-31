package de.polarwolf.alveran.config;

import java.util.Map;

import org.bukkit.entity.Player;

import de.polarwolf.alveran.main.Main;

public class AlveranConfig {
	
	private final Main main;
	
	public AlveranConfig(Main main) {
		this.main = main;
		main.saveDefaultConfig();
	}
	
	private Boolean getConfigEntryBoolean(String lineIdentifier) {
		return main.getConfig().getBoolean(lineIdentifier);
	}
	
	private Integer getConfigEntryInt(String lineIdentifier) {
		return main.getConfig().getInt(lineIdentifier);
	}
	
	private String getConfigEntryStr(String lineIdentifier) {
		return main.getConfig().getString(lineIdentifier);
	}
	
	private String getConfigEntryMessage(Player player, String messageIdentifier) {
		String message = null;
		String locale = player.getLocale().replace("_","-");
		Map<String, Object> translations = main.getConfig().getConfigurationSection("messages").getValues(false);
		if (translations.keySet().stream().anyMatch(s ->s.equals(locale))) {
			message = getConfigEntryStr("messages."+locale+"."+messageIdentifier);
			if (!(message==null)) {
				if (message.length() > 0) {
					return message;
				}
			}
		}
		message = getConfigEntryStr("messages.default."+messageIdentifier);
		return message;
	}

	
	// General Settings
	
	public String getWorld() {
		return getConfigEntryStr("general.world");
	}
	
	public Boolean getEnableAPI() {
		return getConfigEntryBoolean("general.enable-api");
	}
		
	public Boolean getExecuteAsPlayer() {
		return getConfigEntryBoolean("general.player-can-execute-command");
	}

	public Boolean getDebug() {
		return getConfigEntryBoolean("general.debug");
	}
	
	// Authentication

	public String getRegion() {
		return getConfigEntryStr("protection.region");
	}
	
	public String getPermission() {
		return getConfigEntryStr("protection.permission");		
	}
	

	// Action

	public String getDestinationGroup() {
		return getConfigEntryStr("action.destination-group");				
	}
	
	public Integer getDuration() {
		return getConfigEntryInt("action.blessing-duration-hours");		
	}
	
	public Boolean getUnblessOnLeave() {
		return getConfigEntryBoolean("action.unbless-on-leave");		
	}

	public Boolean getNofityPlayerOnUnbless() {
		return getConfigEntryBoolean("action.notify-on-unbless");		
	}

	
	//Messages

	public String getMessgeBlessed(Player player) {
		return getConfigEntryMessage(player, "blessed");
	}
		
	public String getMessgeFaded(Player player) {
		return getConfigEntryMessage(player, "faded");
	}
	public String getMessageExecutedAsPlayer(Player player) {
		return getConfigEntryMessage(player, "executedAsPlayer");
	}
	
}
