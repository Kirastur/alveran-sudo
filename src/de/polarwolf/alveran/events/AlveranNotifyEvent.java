package de.polarwolf.alveran.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AlveranNotifyEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	protected final Player player;
	protected final boolean state;

	AlveranNotifyEvent(Player player, boolean state) {
		this.player = player;
		this.state = state;
	}

	public Player getPlayer() {
		return player;
	}

	public boolean newState() {
		return state;
	}

	@Override
	public HandlerList getHandlers() {
		return getHandlerList();
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
