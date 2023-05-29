package de.polarwolf.alveran.integration.luckperms;

import org.bukkit.plugin.Plugin;

import de.polarwolf.alveran.notifications.NotificationManager;

public class LuckPermsNodeAddScheduler extends LuckPermsNodeScheduler {

	LuckPermsNodeAddScheduler(Plugin plugin, NotificationManager notificationManager, String nodeKey,
			String playerName) {
		super(plugin, notificationManager, nodeKey, playerName);
	}

	@Override
	protected void handleRun() {
		notificationManager.handleNodeAdd(nodeKey, playerName);
	}

}
