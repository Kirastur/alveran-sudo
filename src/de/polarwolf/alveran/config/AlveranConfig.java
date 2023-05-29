package de.polarwolf.alveran.config;

import org.bukkit.configuration.ConfigurationSection;

public class AlveranConfig {

	public static final String SECTION_GENERAL = "general";
	public static final String SECTION_PROTECTION = "protection";
	public static final String SECTION_ACTION = "action";

	public static final String PARAM_GENERAL_PLAYER_CAN_EXECUTE_COMMAND = "player-can-execute-command";
	public static final String PARAM_GENERAL_ENABLE_DEBUG = "enableDebug";
	public static final String PARAM_PROTECTION_REGION = "region";
	public static final String PARAM_PROTECTION_ACTOR_PERMISSION = "actorPermission";
	public static final String PARAM_PROTECTION_RECEIVE_PERMISSION = "receivePermission";
	public static final String PARAM_ACTION_DESTINATION_GROUP = "destination-group";
	public static final String PARAM_ACTION_BLESSING_DURATION = "blessing-duration-hours";
	public static final String PARAM_ACTION_UNBLESS_ON_LEAVE = "unbless-on-leave";
	public static final String PARAM_ACTION_UNBLESS_NOTIFY = "notify-on-unbless";
	public static final String PARAM_ACTION_NOTIFY_SOUND = "notifySound";

	public static final boolean DEFAULT_GENERAL_PLAYER_CAN_EXECUTE_COMMAND = true;
	public static final boolean DEFAULT_GENERAL_ENABLE_DEBUG = false;
	public static final String DEFAULT_PROTECTION_REGION = "alveran";
	public static final String DEFAULT_PROTECTION_ACTOR_PERMISSION = "alveran.priest";
	public static final String DEFAULT_PROTECTION_RECEIVE_PERMISSION = "alveran.acolyte";
	public static final String DEFAULT_ACTION_DESTINATION_GROUP = "alveran";
	public static final int DEFAULT_ACTION_BLESSING_DURATION = 24;
	public static final boolean DEFAULT_ACTION_UNBLESS_ON_LEAVE = true;
	public static final boolean DEFAULT_ACTION_UNBLESS_NOTIFY = true;
	public static final String DEFAULT_ACTION_NOTIFY_SOUND = "";

	protected boolean canPlayerExecuteCommand = DEFAULT_GENERAL_PLAYER_CAN_EXECUTE_COMMAND;
	protected boolean enableDebug = DEFAULT_GENERAL_ENABLE_DEBUG;
	protected String region = DEFAULT_PROTECTION_REGION;
	protected String actorPermission = DEFAULT_PROTECTION_ACTOR_PERMISSION;
	protected String receivePermission = DEFAULT_PROTECTION_RECEIVE_PERMISSION;
	protected String destinationGroup = DEFAULT_ACTION_DESTINATION_GROUP;
	protected int blessingDuration = DEFAULT_ACTION_BLESSING_DURATION;
	protected boolean unblessOnLeave = DEFAULT_ACTION_UNBLESS_ON_LEAVE;
	protected boolean notifyOnUnbless = DEFAULT_ACTION_UNBLESS_NOTIFY;
	protected String notifySound = DEFAULT_ACTION_NOTIFY_SOUND;

	public AlveranConfig() {
	}

	public AlveranConfig(ConfigurationSection configurationSection) {
		ConfigurationSection sectionGeneral = configurationSection.getConfigurationSection(SECTION_GENERAL);
		ConfigurationSection sectionProtection = configurationSection.getConfigurationSection(SECTION_PROTECTION);
		ConfigurationSection sectionAction = configurationSection.getConfigurationSection(SECTION_ACTION);

		if (sectionGeneral != null) {
			canPlayerExecuteCommand = sectionGeneral.getBoolean(PARAM_GENERAL_PLAYER_CAN_EXECUTE_COMMAND,
					DEFAULT_GENERAL_PLAYER_CAN_EXECUTE_COMMAND);
			enableDebug = sectionGeneral.getBoolean(PARAM_GENERAL_ENABLE_DEBUG, DEFAULT_GENERAL_ENABLE_DEBUG);
		}

		if (sectionProtection != null) {
			region = sectionProtection.getString(PARAM_PROTECTION_REGION, DEFAULT_PROTECTION_REGION);
			actorPermission = sectionProtection.getString(PARAM_PROTECTION_ACTOR_PERMISSION,
					DEFAULT_PROTECTION_ACTOR_PERMISSION);
			receivePermission = configurationSection.getString(PARAM_PROTECTION_RECEIVE_PERMISSION,
					DEFAULT_PROTECTION_RECEIVE_PERMISSION);
		}

		if (sectionAction != null) {
			destinationGroup = sectionAction.getString(PARAM_ACTION_DESTINATION_GROUP,
					DEFAULT_ACTION_DESTINATION_GROUP);
			blessingDuration = sectionAction.getInt(PARAM_ACTION_BLESSING_DURATION, DEFAULT_ACTION_BLESSING_DURATION);
			unblessOnLeave = sectionAction.getBoolean(PARAM_ACTION_UNBLESS_ON_LEAVE, DEFAULT_ACTION_UNBLESS_ON_LEAVE);
			notifyOnUnbless = sectionAction.getBoolean(PARAM_ACTION_UNBLESS_NOTIFY, DEFAULT_ACTION_UNBLESS_NOTIFY);
			notifySound = sectionAction.getString(PARAM_ACTION_NOTIFY_SOUND, DEFAULT_ACTION_NOTIFY_SOUND);
		}
	}

	public boolean canPlayerExecuteCommand() {
		return canPlayerExecuteCommand;
	}

	public boolean isEnableDebug() {
		return enableDebug;
	}

	public String getRegion() {
		return region;
	}

	public String getActorPermission() {
		return actorPermission;
	}

	public String getReceivePermission() {
		return receivePermission;
	}

	public String getDestinationGroup() {
		return destinationGroup;
	}

	public int getBlessingDuration() {
		return blessingDuration;
	}

	public boolean isUnblessOnLeave() {
		return unblessOnLeave;
	}

	public boolean isNotifyOnUnbless() {
		return notifyOnUnbless;
	}

	public String getNotifySound() {
		return notifySound;
	}

}
