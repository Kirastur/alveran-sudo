package de.polarwolf.alveran.integration.luckperms;

import org.bukkit.plugin.Plugin;

import de.polarwolf.alveran.notifications.NotificationManager;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.EventSubscription;
import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.event.node.NodeRemoveEvent;

public class LuckPermsListener {

	protected final Plugin plugin;
	protected final NotificationManager notificationManager;
	protected EventSubscription<NodeAddEvent> nodeAddEventHandler = null;
	protected EventSubscription<NodeRemoveEvent> nodeRemoveEventHandler = null;

	public LuckPermsListener(Plugin plugin, NotificationManager notificationManager) {
		this.plugin = plugin;
		this.notificationManager = notificationManager;
	}

	public void enableListener() {
		// We expect NonNull here.
		LuckPerms lpAPI = LuckPermsProvider.get();

		// Get the LuckPerms event bus.
		EventBus eventBus = lpAPI.getEventBus();

		// Register for the events we are interested in.
		nodeAddEventHandler = eventBus.subscribe(NodeAddEvent.class, this::onNodeAdd);
		nodeRemoveEventHandler = eventBus.subscribe(NodeRemoveEvent.class, this::onNodeRemove);
	}

	public void disableListener() {
		if (nodeAddEventHandler != null) {
			nodeAddEventHandler.close();
			nodeAddEventHandler = null;
		}
		if (nodeRemoveEventHandler != null) {
			nodeRemoveEventHandler.close();
			nodeRemoveEventHandler = null;
		}
	}

	private void onNodeAdd(NodeAddEvent event) {
		// LuckPerms calls this async.
		// As we want to access the Bukkit API, we need to use the scheduler to jump
		// back into the main thread.
		String nodeKey = event.getNode().getKey();
		String playerName = event.getTarget().getFriendlyName();
		new LuckPermsNodeAddScheduler(plugin, notificationManager, nodeKey, playerName);
	}

	private void onNodeRemove(NodeRemoveEvent event) {
		// LuckPerms calls this async.
		// As we want to access the Bukkit API, we need to use the scheduler to jump
		// back into the main thread.
		String nodeKey = event.getNode().getKey();
		String playerName = event.getTarget().getFriendlyName();
		new LuckPermsNodeRemoveScheduler(plugin, notificationManager, nodeKey, playerName);
	}

}
