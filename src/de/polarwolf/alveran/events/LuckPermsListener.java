package de.polarwolf.alveran.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.polarwolf.alveran.main.Main;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.event.node.NodeRemoveEvent;

public class LuckPermsListener {
	
	private final Main main;
	
	public LuckPermsListener(Main main) {
		this.main=main;
		
		LuckPerms lpapi = LuckPermsProvider.get();
		if(lpapi==null) {
			main.getLogger().warning("LuckPerms provider not found.");
			return;
		}
		
		// get the LuckPerms event bus
		EventBus eventBus = lpapi.getEventBus();
		eventBus.subscribe(NodeAddEvent.class, this::onNodeAdd);
		eventBus.subscribe(NodeRemoveEvent.class, this::onNodeRemove);
	}
	
	public void HandleNodeAdd(String nodeKey, String playerName) {
		if (!nodeKey.equalsIgnoreCase("group."+main.getAlveranConfig().getDestinationGroup())) {
			return;
		}
		Player player = main.getServer().getPlayer(playerName);
		if (!(player==null)) {
			player.sendMessage(main.getAlveranConfig().getMessgeBlessed(player));
		}
		main.getLogger().info("Player "+playerName+" has been blessed by Alveran");
	}
	
	public void HandleNodeRemove(String nodeKey, String playerName) {
		if (!nodeKey.equalsIgnoreCase("group."+main.getAlveranConfig().getDestinationGroup())) {
			return;
		}
		Player player = main.getServer().getPlayer(playerName);
		if (!(player==null)) {
			if (main.getAlveranConfig().getNofityPlayerOnUnbless()) {
				player.sendMessage(main.getAlveranConfig().getMessgeFaded(player));
			}
		}
		main.getLogger().info("The blessing on player "+playerName+" has faded away");
	}

	private void onNodeAdd(NodeAddEvent event) {
        // as we want to access the Bukkit API, we need to use the scheduler to jump back onto the main thread.
        Bukkit.getScheduler().runTask(main, () -> {
        	if (event.isUser() ) {
        		HandleNodeAdd(event.getNode().getKey(), event.getTarget().getFriendlyName());
        	}
        });
    }

	private void onNodeRemove(NodeRemoveEvent event) {
        // as we want to access the Bukkit API, we need to use the scheduler to jump back onto the main thread.
        Bukkit.getScheduler().runTask(main, () -> {
        	if (event.isUser() ) {
        		HandleNodeRemove(event.getNode().getKey(), event.getTarget().getFriendlyName());
        	}
        });
    }
}
