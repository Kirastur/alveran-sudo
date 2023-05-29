package de.polarwolf.alveran.cornucopia;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import de.polarwolf.alveran.config.ConfigManager;
import de.polarwolf.alveran.events.EventManager;
import de.polarwolf.alveran.exception.AlveranException;
import de.polarwolf.alveran.integration.IntegrationManager;
import de.polarwolf.alveran.integration.worldguard.WorldGuardRegion;
import de.polarwolf.alveran.orchestrator.AlveranOrchestrator;
import de.polarwolf.alveran.text.Message;
import de.polarwolf.alveran.text.TextManager;

public class CornucopiaManager {

	protected final Plugin plugin;
	protected final TextManager textManager;
	protected final ConfigManager configManager;
	protected final EventManager eventManager;
	protected final IntegrationManager integrationManager;

	protected CornucopiaListener cornucopiaListener = null;

	public CornucopiaManager(AlveranOrchestrator orchestrator) {
		this.plugin = orchestrator.getPlugin();
		this.textManager = orchestrator.getTextManager();
		this.configManager = orchestrator.getConfigManager();
		this.eventManager = orchestrator.getEventManager();
		this.integrationManager = orchestrator.getIntegrationManager();
	}

	protected void printDebug(String s) {
		if (configManager.isEnableDebug()) {
			plugin.getLogger().info(s);
		}
	}

	protected boolean isPlayerAuthorizable(Player player, WorldGuardRegion region, String neededPermissionName) {

		// 1. Check if player has priest permission
		if (!player.hasPermission(neededPermissionName)) {
			String s = String.format("Player %s doesn't have the actor permission", player.getName());
			printDebug(s);
			return false;
		}
		Location location = player.getLocation();
		if (!location.getWorld().equals(region.world())) {
			String s = String.format("Priest %s is not in world", player.getName());
			printDebug(s);
			return false;
		}
		if (!integrationManager.isInsideWorldGuardRegion(location.toVector(), region)) {
			String s = String.format("Priest %s is not in region", player.getName());
			printDebug(s);
			return false;
		}
		String s = String.format("Player %s has authorizied blessing", player.getName());
		printDebug(s);
		return true;
	}

	protected boolean isPlayerReceiveable(Blessing blessing, WorldGuardRegion region, String neededPermissionName)
			throws AlveranException {

		// 1. Check if player has acolyte permission
		Player player = blessing.player();
		if (!player.hasPermission(neededPermissionName)) {
			String s = String.format("Player %s doesn't have the receive permission", player.getName());
			printDebug(s);
			return false;
		}

		// 2: Check if the player is inside the region
		Location location = player.getLocation();
		if (!location.getWorld().equals(region.world())) {
			String s = String.format("Player %s is not in world", player.getName());
			printDebug(s);
			return false;
		}
		if (!integrationManager.isInsideWorldGuardRegion(location.toVector(), region)) {
			String s = String.format("Player %s is not in region", player.getName());
			printDebug(s);
			return false;
		}

		// 3. Send event
		if (!eventManager.sendPlayerBlessEvent(blessing)) {
			String s = String.format("Player %s is blocked for blessing authorization by event", player.getName());
			printDebug(s);
			return false;
		}
		return true;
	}

	public boolean isPlayerBlessed(Player player) {
		return integrationManager.isPlayerLuckPermsBlessed(player, configManager.getDestinationGroup());
	}

	public int performBlessing(World world) throws AlveranException {
		printDebug("Prepare blessing");

		// Get values from configuration
		String regionName = configManager.getRegion();
		String actorPermissionName = configManager.getActorPermission();
		String receivePermissionName = configManager.getReceivePermission();
		String destinationGroupName = configManager.getDestinationGroup();
		Integer blessingDuration = configManager.getBlessingDuration();

		// Get WorldGuard region
		WorldGuardRegion region = integrationManager.getWorldGuardRegion(world, regionName);

		// Check Destination Group
		integrationManager.validateLuckPermGroup(destinationGroupName);

		// Check if at least one priest is staying inside the region
		boolean canStartBlessing = false;
		for (Player myPlayer : world.getPlayers()) {
			if (isPlayerAuthorizable(myPlayer, region, actorPermissionName)) {
				canStartBlessing = true;
			}
		}
		if (!canStartBlessing) {
			throw new AlveranException(Message.NOT_AUTHORIZED);
		}

		// Loop for all players
		printDebug("Perform blessing");
		int count = 0;
		AlveranException lastException = null;
		for (Player myPlayer : world.getPlayers()) {
			try {

				// Build the Blessing
				Blessing myBlessing = new Blessing(myPlayer, destinationGroupName, blessingDuration);

				// Check if player is able to get blessed
				boolean isAuthorized = isPlayerReceiveable(myBlessing, region, receivePermissionName);

				// If player is authorized, perform the blessing
				if (isAuthorized) {
					String s = String.format("Perform blessing on Player %s", myPlayer.getName());
					printDebug(s);
					integrationManager.blessSinglePlayerWithLuckPerms(myBlessing);
					count = count + 1;
				}
			} catch (AlveranException ae) {
				lastException = ae;
			} catch (Exception e) {
				lastException = new AlveranException(myPlayer.getName(), Message.JAVA_EXCEPTION, e.getMessage(), e);
			}
		}
		String s = String.format("We've blessed %d players", count);
		printDebug(s);
		if (lastException != null) {
			throw lastException;
		}

		return count;
	}

	public boolean unblessPlayer(Player player) throws AlveranException {
		String destinationGroupName = configManager.getDestinationGroup();
		Integer blessingDuration = configManager.getBlessingDuration();
		Blessing blessing = new Blessing(player, destinationGroupName, blessingDuration);

		String s = String.format("Unblessing Player %s", player.getName());
		printDebug(s);
		return integrationManager.unblessSinglePlayerWithLuckPerms(blessing);
	}

	public void startup() {
		cornucopiaListener = new CornucopiaListener(plugin, configManager, this);
	}

	public void disable() {
		if (cornucopiaListener != null) {
			cornucopiaListener.disableListener();
			cornucopiaListener = null;
		}
	}

	public boolean isDisabled() {
		return cornucopiaListener == null;
	}

}
