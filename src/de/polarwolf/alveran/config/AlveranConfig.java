package de.polarwolf.alveran.config;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class AlveranConfig {
	
	protected final Plugin plugin;
	
	public AlveranConfig(Plugin plugin) {
		this.plugin = plugin;
		plugin.saveDefaultConfig();
	}
	
	protected Boolean getConfigEntryBoolean(String lineIdentifier) {
		return plugin.getConfig().getBoolean(lineIdentifier);
	}
	
	protected Integer getConfigEntryInt(String lineIdentifier) {
		return plugin.getConfig().getInt(lineIdentifier);
	}
	
	protected String getConfigEntryStr(String lineIdentifier) {
		return plugin.getConfig().getString(lineIdentifier);
	}
	
	protected String getConfigEntryMessageLocale (String messageIdentifier, String locale) {
		return getConfigEntryStr("messages."+locale+"."+messageIdentifier);
	}
	
	protected @Nonnull String getConfigEntryMessage(Player player, String messageIdentifier) {
		if (player!=null) {
			String locale = player.getLocale();		
			String message = getConfigEntryMessageLocale(messageIdentifier, locale);
			if ((message!=null) && (!message.isEmpty())) {
					return message;
			}
			String locale2 = player.getLocale().substring(0,2);
			String message2 = getConfigEntryMessageLocale(messageIdentifier, locale2);
			if ((message2!=null) && (!message2.isEmpty())) {
				return message2;
			}
		}
		String defaultMessage = getConfigEntryMessageLocale(messageIdentifier, "default");
		if ((defaultMessage!=null) && (!defaultMessage.isEmpty())) {
			return defaultMessage;
		}
		return "ALVERAN ERROR";
	}

	
	// General Settings
	
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

	public @Nonnull String getMessgeBlessed(Player player) {
		return getConfigEntryMessage(player, "blessed");
	}
		
	public @Nonnull String getMessgeFaded(Player player) {
		return getConfigEntryMessage(player, "faded");
	}
	public @Nonnull String getMessageExecutedAsPlayer(Player player) {
		return getConfigEntryMessage(player, "executed-as-player");
	}
	
	public @Nonnull String getMessageWorldNotFound(Player player) {
		return getConfigEntryMessage(player, "world-not-found");
	}
}
