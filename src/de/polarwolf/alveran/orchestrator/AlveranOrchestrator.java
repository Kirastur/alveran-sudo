package de.polarwolf.alveran.orchestrator;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import de.polarwolf.alveran.config.ConfigManager;
import de.polarwolf.alveran.cornucopia.CornucopiaManager;
import de.polarwolf.alveran.events.EventManager;
import de.polarwolf.alveran.exception.AlveranException;
import de.polarwolf.alveran.integration.IntegrationManager;
import de.polarwolf.alveran.notifications.NotificationManager;
import de.polarwolf.alveran.sanctuary.SanctuaryManager;
import de.polarwolf.alveran.text.TextManager;

public class AlveranOrchestrator {

	public static final String PLUGIN_NAME = "Alveran";

	private final Plugin plugin;
	private final TextManager textManager;
	private final ConfigManager configManager;
	private final EventManager eventManager;
	private final IntegrationManager integrationManager;
	private final NotificationManager notificationManager;
	private final CornucopiaManager cornucopiaManager;
	private final SanctuaryManager sanctuaryManager;

	protected final StartOptions startOptions;

	protected AlveranOrchestrator(Plugin plugin, StartOptions startOptions) {
		if (plugin != null) {
			this.plugin = plugin;
		} else {
			this.plugin = Bukkit.getPluginManager().getPlugin(PLUGIN_NAME);
		}
		this.startOptions = startOptions;

		textManager = createTextManager();
		configManager = createConfigManager();
		eventManager = createEventManager();
		integrationManager = createIntegrationManager();
		notificationManager = createNotificationManager();
		cornucopiaManager = createCornucopiaManager();
		sanctuaryManager = createSanctuaryManager();
	}

	// Getter
	public Plugin getPlugin() {
		return plugin;
	}

	public TextManager getTextManager() {
		return textManager;
	}

	public ConfigManager getConfigManager() {
		return configManager;
	}

	public EventManager getEventManager() {
		return eventManager;
	}

	public IntegrationManager getIntegrationManager() {
		return integrationManager;
	}

	public NotificationManager getNotificationManager() {
		return notificationManager;
	}

	public CornucopiaManager getCornucopiaManager() {
		return cornucopiaManager;
	}

	public SanctuaryManager getSanctuaryManager() {
		return sanctuaryManager;
	}

	// Creator
	protected TextManager createTextManager() {
		return new TextManager(this);
	}

	protected ConfigManager createConfigManager() {
		return new ConfigManager(this);
	}

	protected EventManager createEventManager() {
		return new EventManager(this);
	}

	protected IntegrationManager createIntegrationManager() {
		return new IntegrationManager(this);
	}

	protected NotificationManager createNotificationManager() {
		return new NotificationManager(this);
	}

	protected CornucopiaManager createCornucopiaManager() {
		return new CornucopiaManager(this);
	}

	protected SanctuaryManager createSanctuaryManager() {
		return new SanctuaryManager(this);
	}

	// Validate
	public void startup() throws AlveranException {
		integrationManager.initializeStaticIntegrations(notificationManager);
		cornucopiaManager.startup();
		if (startOptions.enableSanctuary()) {
			sanctuaryManager.startup();
		}
	}

	// Deactivation
	public boolean isDisabled() {
		return cornucopiaManager.isDisabled();
	}

	public void disable() {
		sanctuaryManager.disable();
		cornucopiaManager.disable();
		integrationManager.disable();
	}

}
