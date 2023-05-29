package de.polarwolf.alveran.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import de.polarwolf.alveran.main.Main;

public class AlveranTabCompleter implements TabCompleter {

	protected final AlveranCommand command;

	public AlveranTabCompleter(Main main, AlveranCommand command) {
		this.command = command;
		main.getCommand(command.getCommandName()).setTabCompleter(this);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		ArrayList<String> worldNames = new ArrayList<>();
		if (args.length == 1) {
			List<World> worlds = Bukkit.getWorlds();
			for (World world : worlds) {
				worldNames.add(world.getName());
			}
		}
		return worldNames;
	}

}
