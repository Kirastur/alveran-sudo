package de.polarwolf.alveran.integration;

import java.util.Set;
import java.util.TreeSet;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import de.polarwolf.alveran.cornucopia.Blessing;
import de.polarwolf.alveran.exception.AlveranException;
import de.polarwolf.alveran.integration.luckperms.LuckPermsHook;
import de.polarwolf.alveran.integration.worldguard.WorldGuardHook;
import de.polarwolf.alveran.integration.worldguard.WorldGuardRegion;
import de.polarwolf.alveran.notifications.NotificationManager;
import de.polarwolf.alveran.orchestrator.AlveranOrchestrator;
import de.polarwolf.alveran.text.Message;

public class IntegrationManager {

	public static final String WORLDGUARD_NAME = "WorldGuard";
	public static final String LUCKPERMS_NAME = "LuckPerms";

	protected final Plugin plugin;

	protected WorldGuardHook worldGuardHook = null;
	protected LuckPermsHook luckPermsHook = null;

	public IntegrationManager(AlveranOrchestrator orchestrator) {
		this.plugin = orchestrator.getPlugin();
	}

	// WorldGuard

	public boolean hasWorldGuard() {
		return (plugin.getServer().getPluginManager().getPlugin(WORLDGUARD_NAME) != null);
	}

	protected void initializeWorldGuardHook() throws AlveranException {
		if (!hasWorldGuard()) {
			throw new AlveranException(Message.WORLDGUARD_LINK_ERROR);
		}
		worldGuardHook = new WorldGuardHook();
	}

	public boolean hasWorldGuardRegion(World world, String regionName) {
		return worldGuardHook.hasRegion(world, regionName);
	}

	public WorldGuardRegion getWorldGuardRegion(World world, String regionName) throws AlveranException {
		return worldGuardHook.getRegion(world, regionName);
	}

	public boolean isInsideWorldGuardRegion(Vector vector, WorldGuardRegion region) {
		return worldGuardHook.isInside(vector, region);
	}

	// LuckPerms

	public boolean hasLuckPerms() {
		return (plugin.getServer().getPluginManager().getPlugin(LUCKPERMS_NAME) != null);
	}

	protected void initializeLuckPermsHook(NotificationManager notificationManager) throws AlveranException {
		if (!hasLuckPerms()) {
			throw new AlveranException(Message.LUCKPERM_LINK_ERROR);
		}
		if (luckPermsHook != null) {
			luckPermsHook.disable();
		}
		luckPermsHook = new LuckPermsHook(plugin, notificationManager);
	}

	public void validateLuckPermGroup(String groupName) throws AlveranException {
		luckPermsHook.validateGroup(groupName);
	}

	public boolean isPlayerLuckPermsBlessed(Player player, String blessedGroupName) {
		return luckPermsHook.isPlayerBlessed(player, blessedGroupName);
	}

	public boolean blessSinglePlayerWithLuckPerms(Blessing blessing) throws AlveranException {
		return luckPermsHook.blessSinglePlayer(blessing);
	}

	public boolean unblessSinglePlayerWithLuckPerms(Blessing blessing) throws AlveranException {
		return luckPermsHook.unblessSinglePlayer(blessing);
	}

	// Main

	public void initializeStaticIntegrations(NotificationManager notificationManager) throws AlveranException {
		initializeWorldGuardHook();
		initializeLuckPermsHook(notificationManager);
	}

	public Set<String> getActiveIntegrations() {
		Set<String> activeIntegrations = new TreeSet<>();
		if (hasWorldGuard()) {
			activeIntegrations.add(WORLDGUARD_NAME);
		}
		if (hasLuckPerms()) {
			activeIntegrations.add(LUCKPERMS_NAME);
		}
		return activeIntegrations;
	}

	public void disable() {
		if (luckPermsHook != null) {
			luckPermsHook.disable();
		}
	}

	public boolean isDisabled() {
		return ((luckPermsHook == null) || (luckPermsHook.isDisabled()));
	}

}
