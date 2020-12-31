package de.polarwolf.alveran.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.polarwolf.alveran.main.Main;

public class PlayerListener implements Listener {
	
	private final Main main;
	
	public PlayerListener(Main main) {
		this.main = main;
	}
		
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerJoin(PlayerJoinEvent evt) {
		if (main.getAlveranConfig().getUnblessOnLeave()) {
			Player player = evt.getPlayer();
			main.getAlveranAPI().UnblessPlayer(player);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerQuit (PlayerQuitEvent evt) {
		if (main.getAlveranConfig().getUnblessOnLeave()) {
			Player player = evt.getPlayer();
			main.getAlveranAPI().UnblessPlayer(player);
		}
	}

}