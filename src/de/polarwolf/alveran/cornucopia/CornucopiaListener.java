package de.polarwolf.alveran.cornucopia;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import de.polarwolf.alveran.config.ConfigManager;

public class CornucopiaListener implements Listener {

	protected final Plugin plugin;
	protected final ConfigManager configManager;
	protected final CornucopiaManager cornucopiaManager;

	public CornucopiaListener(Plugin plugin, ConfigManager configManager, CornucopiaManager cornucopiaManager) {
		this.plugin = plugin;
		this.configManager = configManager;
		this.cornucopiaManager = cornucopiaManager;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	public void disableListener() {
		HandlerList.unregisterAll(this);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent evt) {
		try {
			if (configManager.isUnblessOnLeave()) {
				Player player = evt.getPlayer();
				cornucopiaManager.unblessPlayer(player);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent evt) {
		try {
			if (configManager.isUnblessOnLeave()) {
				Player player = evt.getPlayer();
				cornucopiaManager.unblessPlayer(player);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}