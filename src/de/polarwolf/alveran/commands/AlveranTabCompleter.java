package de.polarwolf.alveran.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;

public class AlveranTabCompleter implements TabCompleter {
	
	protected final Plugin plugin;
	
	public AlveranTabCompleter(Plugin plugin) {
		this.plugin=plugin;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		ArrayList<String> worldNames = new ArrayList<>();
		if (args.length == 1) {
			List<World> worlds = plugin.getServer().getWorlds();
			for (World world : worlds) {
				worldNames.add(world.getName());
			}
		}
		return worldNames;
	}
	
}
