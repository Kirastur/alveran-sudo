package de.polarwolf.alveran.text;

public enum Message {

	OK, // OK
	ERROR, // ERROR
	JAVA_EXCEPTION, // Java Exception Error

	LUCKPERM_LINK_ERROR, // Cannot establish link to Luckperm
	WORLDGUARD_LINK_ERROR, // Cannot establish link to Worldguard

	LUCKPERM_BLESS_ERROR, // Something went wrong while blessing
	LUCKPERM_UNBLESS_ERROR, // Something went wrong while removing the blessing
	LUCKPERMS_GROUP_NOT_FOUND, // LuckPerms group not found
	WORLDGUARD_REGION_NOT_FOUND, // Worldguard region not found
	CONFIG_MESSAGE_FILE_MISSING, // Message file missing
	YAML_PARSE_ERROR, // Error parsing message file
	UNKNOWN_SOUND, // Cannot parse sound from config

	PLAYER_BLESSED, // You have received the blessing of Alveran
	PLAYER_UNBLESSED, // The blessing of Alveran is fading away
	NOTIFY_BLESSED, // %s has been blessed by Alveran
	NOTIFY_UNBLESSED, // "%s has lost the blessing of Alveran"
	BLESS_REPORT_0, // You have not blesed any players
	BLESS_REPORT_1, // You have blessed 1 player
	BLESS_REPORT_X, // You have blessed %d players

	NOT_AUTHORIZED, // No priest has approved the blessing request
	NOT_AS_PLAYER, // Command cannot be executed as player
	WORLD_NOT_FOUND, // Worldname could not be resolved to an existing world
	NO_AUTOWORLD, // The world could not be identified. Enter the world name as command parameter
	UNKNOWN_SOURCE, // Alveran command called from unknown source
	NO_API // Alveran orchestrator missing or not startet

}
