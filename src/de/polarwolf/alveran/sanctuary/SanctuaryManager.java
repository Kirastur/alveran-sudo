package de.polarwolf.alveran.sanctuary;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

import de.polarwolf.alveran.config.ConfigManager;
import de.polarwolf.alveran.cornucopia.CornucopiaManager;
import de.polarwolf.alveran.exception.AlveranException;
import de.polarwolf.alveran.integration.IntegrationManager;
import de.polarwolf.alveran.integration.worldguard.WorldGuardRegion;
import de.polarwolf.alveran.orchestrator.AlveranOrchestrator;

public class SanctuaryManager {

	protected final Plugin plugin;
	protected final IntegrationManager integrationManager;
	protected final ConfigManager configManager;
	protected final CornucopiaManager cornucopiaManager;

	protected SanctuaryListener sanctuaryListener = null;

	public SanctuaryManager(AlveranOrchestrator orchestrator) {
		this.plugin = orchestrator.getPlugin();
		this.integrationManager = orchestrator.getIntegrationManager();
		this.configManager = orchestrator.getConfigManager();
		this.cornucopiaManager = orchestrator.getCornucopiaManager();
	}

	protected boolean isValidMaterial(Material material) {
		return material.toString().contains("PRESSURE_PLATE");
	}

	public void requestBlessing(Block block) throws AlveranException {
		if (!isValidMaterial(block.getType())) {
			return;
		}
		Location location = block.getLocation();
		World world = location.getWorld();
		String regionName = configManager.getRegion();
		if (!integrationManager.hasWorldGuardRegion(world, regionName)) {
			return;
		}
		WorldGuardRegion region = integrationManager.getWorldGuardRegion(world, regionName);
		if (!integrationManager.isInsideWorldGuardRegion(location.toVector(), region)) {
			return;
		}
		cornucopiaManager.performBlessing(world);
	}

	public void startup() {
		sanctuaryListener = new SanctuaryListener(plugin, this);
	}

	public void disable() {
		if (sanctuaryListener != null) {
			sanctuaryListener.disableListener();
			sanctuaryListener = null;
		}
	}

	public boolean isDisabled() {
		return sanctuaryListener == null;
	}

}
