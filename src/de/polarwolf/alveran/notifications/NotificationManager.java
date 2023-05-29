package de.polarwolf.alveran.notifications;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import de.polarwolf.alveran.config.ConfigManager;
import de.polarwolf.alveran.events.EventManager;
import de.polarwolf.alveran.orchestrator.AlveranOrchestrator;
import de.polarwolf.alveran.text.Message;
import de.polarwolf.alveran.text.TextManager;

public class NotificationManager {

	protected final Plugin plugin;
	protected final TextManager textManager;
	protected final ConfigManager configManager;
	protected final EventManager eventManager;

	public NotificationManager(AlveranOrchestrator orchestrator) {
		this.plugin = orchestrator.getPlugin();
		this.textManager = orchestrator.getTextManager();
		this.configManager = orchestrator.getConfigManager();
		this.eventManager = orchestrator.getEventManager();
	}

	protected void notifyPlayer(Player player, Message message) {
		String s = textManager.getText(message, player);
		if (player.isOnline() && !s.isEmpty()) {
			player.sendMessage(s);
		}
	}

	protected void notifyAdmins(Player player, Message message) {
		List<CommandSender> admins = new ArrayList<>();
		for (CommandSender mySender : plugin.getServer().getOnlinePlayers()) {
			if (mySender.isOp()) {
				admins.add(mySender);
			}
		}
		admins.add(plugin.getServer().getConsoleSender());
		admins.remove(player);
		for (CommandSender myAdmin : admins) {
			String s = textManager.getText(message, myAdmin);
			if (!s.isEmpty()) {
				s = String.format(s, player.getName());
				myAdmin.sendMessage(s);
			}
		}
	}

	public void handleNodeAdd(String nodeKey, String playerName) {
		if (!nodeKey.equalsIgnoreCase("group." + configManager.getDestinationGroup())) {
			return;
		}
		Player player = plugin.getServer().getPlayer(playerName);
		if (player == null) {
			return;
		}
		notifyPlayer(player, Message.PLAYER_BLESSED);
		notifyAdmins(player, Message.NOTIFY_BLESSED);
		eventManager.sendNotifyEvent(player, true);
		if (configManager.getNotifySound().isEmpty()) {
			return;
		}
		try {
			Sound sound = Sound.valueOf(configManager.getNotifySound());
			player.playSound(player.getLocation(), sound, 1, 1);
		} catch (Exception e) {
			String s = textManager.getText(Message.UNKNOWN_SOUND, null);
			plugin.getLogger().warning(s);
		}
	}

	public void handleNodeRemove(String nodeKey, String playerName) {
		if (!nodeKey.equalsIgnoreCase("group." + configManager.getDestinationGroup())
				|| !configManager.isNotifyOnUnbless()) {
			return;
		}
		Player player = plugin.getServer().getPlayer(playerName);
		if (player == null) {
			return;
		}
		notifyPlayer(player, Message.PLAYER_UNBLESSED);
		notifyAdmins(player, Message.NOTIFY_UNBLESSED);
		eventManager.sendNotifyEvent(player, false);
	}

}
