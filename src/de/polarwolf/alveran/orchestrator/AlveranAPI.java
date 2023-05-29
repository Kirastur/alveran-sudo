package de.polarwolf.alveran.orchestrator;

import java.util.UUID;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import de.polarwolf.alveran.exception.AlveranException;
import de.polarwolf.alveran.text.Message;

public class AlveranAPI {

	protected final Plugin plugin;
	protected final UUID apiToken;
	protected final AlveranOrchestrator orchestrator;

	public AlveranAPI(Plugin plugin, UUID apiToken, StartOptions startOptions) throws AlveranException {
		this.plugin = plugin;
		this.apiToken = apiToken;
		orchestrator = createOrchestrator(plugin, startOptions);
		try {
			orchestrator.startup();
		} catch (Exception e) {
			orchestrator.disable();
			if (e instanceof AlveranException) {
				throw e;
			}
			throw new AlveranException("Orchestrator initialization", Message.JAVA_EXCEPTION, null, e);
		}
		orchestrator.getConfigManager().reloadMessages();
		orchestrator.getConfigManager().reloadConfig();
	}

	// Configuration Manager
	public boolean canPlayerExecuteCommand() {
		return orchestrator.getConfigManager().canPlayerExecuteCommand();
	}

	public boolean isDebug() {
		return orchestrator.getConfigManager().isEnableDebug();
	}

	public void reload() throws AlveranException {
		plugin.reloadConfig();
		orchestrator.getConfigManager().reloadMessages();
		orchestrator.getConfigManager().reloadConfig();
	}

	// Text Manager
	public String getText(Message message, CommandSender sender) {
		return orchestrator.getTextManager().getText(message, sender);
	}

	public String getLocalizedExceptionText(AlveranException e, CommandSender sender) {
		return e.getLocalizedFullErrorMessage(orchestrator.getTextManager(), sender);
	}

	// Cornucopia Manager
	public boolean isPlayerBlessed(Player player) {
		return orchestrator.getCornucopiaManager().isPlayerBlessed(player);
	}

	public int performBlessing(World world) throws AlveranException {
		return orchestrator.getCornucopiaManager().performBlessing(world);
	}

	public boolean unblessPlayer(Player player) throws AlveranException {
		return orchestrator.getCornucopiaManager().unblessPlayer(player);
	}

	/**
	 * Internally used for startup
	 */
	protected AlveranOrchestrator createOrchestrator(Plugin plugin, StartOptions startOptions) {
		return new AlveranOrchestrator(plugin, startOptions);
	}

	/**
	 * Check if the sequencer has already shut down (e.g. because server is
	 * stopping). In this case you cannot start new sequences.
	 *
	 * @return TRUE if the sequencer is disabled and cannot start new sequences,
	 *         otherwise FALSE
	 */
	public boolean isDisabled() {
		return orchestrator.isDisabled();
	}

	/**
	 * Used internally to perform a clean shutdown
	 */
	public boolean disable(UUID currentApiToken) {
		if (!apiToken.equals(currentApiToken)) {
			return false;
		}
		orchestrator.disable();
		return true;
	}

}
