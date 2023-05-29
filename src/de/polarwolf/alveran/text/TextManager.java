package de.polarwolf.alveran.text;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import de.polarwolf.alveran.orchestrator.AlveranOrchestrator;

public class TextManager {

	protected final Plugin plugin;
	protected Map<String, String> alveranText = new HashMap<>();

	public TextManager(AlveranOrchestrator orchestrator) {
		this.plugin = orchestrator.getPlugin();
	}

	public void clear() {
		alveranText.clear();
	}

	public void addText(String name, String value) {
		alveranText.put(name, value);
	}

	public String getText(String name) {
		String s = alveranText.get(name);
		if (s == null) {
			return "";
		}
		return s;
	}

	public String getText(String name, String locale) {
		if (locale != null) {

			// 1st try: take the full language (e.g. "de_de")
			if (locale.length() >= 5) {
				String s = getText(name + '_' + locale.substring(0, 5));
				if (!s.isEmpty()) {
					return s;
				}
			}

			// 2nd try: take the group language (e.g. "de")
			if (locale.length() >= 2) {
				String s = getText(name + '_' + locale.substring(0, 2));
				if (!s.isEmpty()) {
					return s;
				}
			}
		}

		// No localized string found, return default
		return getText(name);
	}

	public String getText(String name, CommandSender sender) {
		if (sender instanceof Player player) {
			String locale = player.getLocale();
			return getText(name, locale);
		} else {
			return getText(name);
		}
	}

	public String getText(Message message, CommandSender sender) {
		String name = message.name();
		String s = getText(name, sender);
		if (s.isEmpty()) {
			return name;
		}
		return s;
	}

}
