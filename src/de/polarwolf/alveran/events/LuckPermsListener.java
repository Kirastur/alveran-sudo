package de.polarwolf.alveran.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import de.polarwolf.alveran.config.AlveranConfig;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.event.node.NodeRemoveEvent;

public class LuckPermsListener {
	
	protected final Plugin plugin;
	protected final AlveranConfig alveranConfig;
	
	public LuckPermsListener(Plugin plugin, AlveranConfig alveranConfig) {
		this.plugin=plugin;
		this.alveranConfig=alveranConfig;
		
		// We expect NonNull here
		LuckPerms lpAPI = LuckPermsProvider.get();
		
		// Get the LuckPerms event bus
		EventBus eventBus = lpAPI.getEventBus();
		eventBus.subscribe(NodeAddEvent.class, this::onNodeAdd);
		eventBus.subscribe(NodeRemoveEvent.class, this::onNodeRemove);
	}
	
    protected void printInfo(String infoText) {
		plugin.getLogger().info(infoText);
    }
    
    protected void printPlayer(Player player, String messageText) {
		player.sendMessage(messageText);
    }
    
	public void handleNodeAdd(String nodeKey, String playerName) {
		if (!nodeKey.equalsIgnoreCase("group."+alveranConfig.getDestinationGroup())) {
			return;
		}
		Player player = plugin.getServer().getPlayer(playerName);
		if (player!=null) {
			printPlayer(player, alveranConfig.getMessgeBlessed(player));
		}
		printInfo("Player "+playerName+" has been blessed by Alveran");
	}
	
	public void handleNodeRemove(String nodeKey, String playerName) {
		if (!nodeKey.equalsIgnoreCase("group."+alveranConfig.getDestinationGroup())) {
			return;
		}
		Player player = plugin.getServer().getPlayer(playerName);
		if ((player!=null) && (alveranConfig.getNofityPlayerOnUnbless())) {
			printPlayer(player, alveranConfig.getMessgeFaded(player));
		}
		printInfo("The blessing on player "+playerName+" has faded away");
	}

	private void onNodeAdd(NodeAddEvent event) {
        // as we want to access the Bukkit API, we need to use the scheduler to jump back onto the main thread.
        Bukkit.getScheduler().runTask(plugin, () -> {
        	if (event.isUser() ) {
        		handleNodeAdd(event.getNode().getKey(), event.getTarget().getFriendlyName());
        	}
        });
    }

	private void onNodeRemove(NodeRemoveEvent event) {
        // as we want to access the Bukkit API, we need to use the scheduler to jump back onto the main thread.
        Bukkit.getScheduler().runTask(plugin, () -> {
        	if (event.isUser() ) {
        		handleNodeRemove(event.getNode().getKey(), event.getTarget().getFriendlyName());
        	}
        });
    }

}
