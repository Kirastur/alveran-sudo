package de.polarwolf.alveran.events;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import de.polarwolf.alveran.cornucopia.Blessing;
import de.polarwolf.alveran.exception.AlveranException;
import de.polarwolf.alveran.orchestrator.AlveranOrchestrator;

public class EventManager {

	protected Plugin plugin;

	public EventManager(AlveranOrchestrator orchestrator) {
		this.plugin = orchestrator.getPlugin();
	}

	public boolean sendPlayerBlessEvent(Blessing blessing) throws AlveranException {
		AlveranPlayerBlessEvent event = new AlveranPlayerBlessEvent(blessing);
		plugin.getServer().getPluginManager().callEvent(event);
		if (event.isCancelled() && (event.getCause() != null)) {
			throw event.cause;
		}
		return !event.isCancelled();
	}

	public void sendNotifyEvent(Player player, boolean state) {
		AlveranNotifyEvent event = new AlveranNotifyEvent(player, state);
		plugin.getServer().getPluginManager().callEvent(event);
	}

}
