package de.polarwolf.alveran.integration.luckperms;

import org.bukkit.plugin.Plugin;

import de.polarwolf.alveran.notifications.NotificationManager;

public class LuckPermsNodeRemoveScheduler extends LuckPermsNodeScheduler {

	LuckPermsNodeRemoveScheduler(Plugin plugin, NotificationManager notificationManager, String nodeKey,
			String playerName) {
		super(plugin, notificationManager, nodeKey, playerName);
	}

	@Override
	protected void handleRun() {
		notificationManager.handleNodeRemove(nodeKey, playerName);
	}

}
