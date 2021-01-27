package de.polarwolf.alveran.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import de.polarwolf.alveran.api.AlveranAPI;
import de.polarwolf.alveran.config.AlveranConfig;

public class PlayerListener implements Listener {
	
	protected final AlveranConfig alveranConfig;
	protected final AlveranAPI alveranAPI;
	
	public PlayerListener(AlveranConfig alveranConfig, AlveranAPI alveranAPI) {
		this.alveranConfig=alveranConfig;
		this.alveranAPI=alveranAPI;
	}
		
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerJoin(PlayerJoinEvent evt) {
		if (alveranConfig.getUnblessOnLeave()) {
			Player player = evt.getPlayer();
			alveranAPI.unblessPlayer(player);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerQuit (PlayerQuitEvent evt) {
		if (alveranConfig.getUnblessOnLeave()) {
			Player player = evt.getPlayer();
			alveranAPI.unblessPlayer(player);
		}
	}

}