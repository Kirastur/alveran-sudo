package de.polarwolf.alveran.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.polarwolf.alveran.cornucopia.Blessing;
import de.polarwolf.alveran.exception.AlveranException;

public class AlveranPlayerBlessEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	protected final Blessing blessing;

	private boolean cancelled = false;
	protected AlveranException cause = null;

	protected AlveranPlayerBlessEvent(Blessing blessing) {
		this.blessing = blessing;
	}

	public Blessing getBlessing() {
		return blessing;
	}

	public Player getPlayer() {
		return blessing.player();
	}

	public String getDestinationGroupName() {
		return blessing.destinationGroupName();
	}

	public int getBlessingDuration() {
		return blessing.blessingDuration();
	}

	AlveranException getCause() {
		return cause;
	}

	public void cancelWithReason(AlveranException cause) {
		this.cause = cause;
		setCancelled(true);
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		if (cancelled) {
			this.cancelled = cancelled;
		}
	}

	@Override
	public HandlerList getHandlers() {
		return getHandlerList();
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
