package de.polarwolf.alveran.integration.luckperms;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import de.polarwolf.alveran.notifications.NotificationManager;

public abstract class LuckPermsNodeScheduler extends BukkitRunnable {

	protected final NotificationManager notificationManager;
	protected final String nodeKey;
	protected final String playerName;

	LuckPermsNodeScheduler(Plugin plugin, NotificationManager notificationManager, String nodeKey, String playerName) {
		this.notificationManager = notificationManager;
		this.nodeKey = nodeKey;
		this.playerName = playerName;
		this.runTask(plugin);
	}

	protected abstract void handleRun();

	@Override
	public void run() {
		try {
			handleRun();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
