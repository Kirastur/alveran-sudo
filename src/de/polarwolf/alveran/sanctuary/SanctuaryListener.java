package de.polarwolf.alveran.sanctuary;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

public class SanctuaryListener implements Listener {

	protected final Plugin plugin;
	protected final SanctuaryManager sanctuaryManager;

	public SanctuaryListener(Plugin plugin, SanctuaryManager sanctuaryManager) {
		this.plugin = plugin;
		this.sanctuaryManager = sanctuaryManager;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	public void disableListener() {
		HandlerList.unregisterAll(this);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		if (event.getAction() != Action.PHYSICAL) {
			return;
		}
		Block clickedBlock = event.getClickedBlock();
		try {
			sanctuaryManager.requestBlessing(clickedBlock);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
