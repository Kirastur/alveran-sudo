package de.polarwolf.alveran.commands;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import de.polarwolf.alveran.api.AlveranAPI;
import de.polarwolf.alveran.config.AlveranConfig;

public class AlveranCommand implements CommandExecutor{
	
	protected final Plugin plugin;
	protected final AlveranConfig alveranConfig;
	protected final AlveranAPI alveranAPI;
	
	public AlveranCommand(Plugin plugin, AlveranConfig alveranConfig, AlveranAPI alveranAPI) {
		this.plugin=plugin;
		this.alveranConfig=alveranConfig;
		this.alveranAPI=alveranAPI;
	}
	
	protected World getWorldNameFromCommandline(String[] args) {
		if (args.length > 0) {
			String worldName=args[0];
			return plugin.getServer().getWorld(worldName);
		}
		return null;
	}
	
	protected void commandFromPlayer(Player player, String[] args) {
		World world;
		if (!alveranConfig.getExecuteAsPlayer()) {
			player.sendMessage(alveranConfig.getMessageExecutedAsPlayer(player));
			return;
		}
		if (args.length > 0) {
			String worldName=args[0];
			world=plugin.getServer().getWorld(worldName);
			if (world==null) {
				player.sendMessage(alveranConfig.getMessageWorldNotFound(player));
				return;
			}
		} else {
			world=player.getWorld();
		}
		alveranAPI.performBlessing(world);
	}
		
	protected void commandFromBlock(Block block, String[] args) {
		World world;
		if (args.length > 0) {
			String worldName=args[0];
			world=plugin.getServer().getWorld(worldName);
			if (world==null) {
				plugin.getLogger().warning(alveranConfig.getMessageWorldNotFound(null));
				return;
			}
		} else {
			world = block.getWorld();
		}
		alveranAPI.performBlessing(world);
	}
			
	protected void commandFromConsole(CommandSender sender, String[] args) {
		World world;
		if (args.length==0) {
			world=plugin.getServer().getWorld("world");
			if (world==null) {
				sender.sendMessage("The world could not be identified. Please enter the world name as command parameter.");
				return;
			}
		} else {
			String worldName=args[0];
			world=plugin.getServer().getWorld(worldName);
			if (world==null) {
				plugin.getLogger().warning(alveranConfig.getMessageWorldNotFound(null));
				return;
			}
		}
		alveranAPI.performBlessing(world);
	}


	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Boolean isValidSender=false;

		// Sender is Player
		if (sender instanceof Player) {
			Player player = (Player) sender;
			isValidSender=true;
			commandFromPlayer(player, args);
		}

		// Sender is Command Block
		if (sender instanceof BlockCommandSender) {
			BlockCommandSender blockCommandSender = (BlockCommandSender) sender;
			isValidSender=true;
			commandFromBlock(blockCommandSender.getBlock(), args);
		}
		
		// Sender is Console
		if ((sender instanceof ConsoleCommandSender) || (sender instanceof RemoteConsoleCommandSender)) {
			isValidSender=true;
			commandFromConsole(sender, args);
		}
		
		if (!isValidSender) {
			plugin.getLogger().warning("Alveran command called from unknown source");
		}
		return true;
	}			

}
