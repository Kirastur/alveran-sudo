package de.polarwolf.alveran.commands;

import static de.polarwolf.alveran.text.Message.BLESS_REPORT_0;
import static de.polarwolf.alveran.text.Message.BLESS_REPORT_1;
import static de.polarwolf.alveran.text.Message.BLESS_REPORT_X;
import static de.polarwolf.alveran.text.Message.ERROR;
import static de.polarwolf.alveran.text.Message.NOT_AS_PLAYER;
import static de.polarwolf.alveran.text.Message.NO_API;
import static de.polarwolf.alveran.text.Message.NO_AUTOWORLD;
import static de.polarwolf.alveran.text.Message.UNKNOWN_SOURCE;
import static de.polarwolf.alveran.text.Message.WORLD_NOT_FOUND;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;

import de.polarwolf.alveran.api.AlveranProvider;
import de.polarwolf.alveran.exception.AlveranException;
import de.polarwolf.alveran.main.Main;
import de.polarwolf.alveran.orchestrator.AlveranAPI;
import de.polarwolf.alveran.text.Message;

public class AlveranCommand implements CommandExecutor {

	protected final Main main;
	protected final String commandName;
	protected AlveranTabCompleter tabCompleter;

	public AlveranCommand(Main main, String commandName) {
		this.main = main;
		this.commandName = commandName;
		main.getCommand(commandName).setExecutor(this);
		tabCompleter = new AlveranTabCompleter(main, this);
	}

	public String getCommandName() {
		return commandName;
	}

	public AlveranAPI getAPI() {
		return AlveranProvider.getAPI();
	}

	public void sendMessage(CommandSender sender, Message message) {
		if ((sender == null) || (sender instanceof BlockCommandSender)) {
			sender = Bukkit.getConsoleSender();
		}
		sender.sendMessage(getAPI().getText(message, sender));
	}

	protected World getWorldFromCommandline(String[] args) {
		if (args.length > 0) {
			String worldName = args[0];
			return Bukkit.getWorld(worldName);
		}
		return null;
	}

	protected void commandFromPlayer(Player player, String[] args) throws AlveranException {
		World world;
		if (!getAPI().canPlayerExecuteCommand()) {
			sendMessage(player, NOT_AS_PLAYER);
			return;
		}
		if (args.length > 0) {
			world = getWorldFromCommandline(args);
			if (world == null) {
				sendMessage(player, WORLD_NOT_FOUND);
				return;
			}
		} else {
			world = player.getWorld();
		}
		int count = getAPI().performBlessing(world);
		String s = "";
		if (count == 0) {
			s = getAPI().getText(BLESS_REPORT_0, player);
		}
		if (count == 1) {
			s = getAPI().getText(BLESS_REPORT_1, player);
		}
		if (count > 1) {
			s = String.format(getAPI().getText(BLESS_REPORT_X, player), count);
		}
		player.sendMessage(s);
	}

	protected void commandFromBlock(Block block, String[] args) throws AlveranException {
		World world;
		if (args.length > 0) {
			world = getWorldFromCommandline(args);
			if (world == null) {
				sendMessage(null, WORLD_NOT_FOUND);
				return;
			}
		} else {
			world = block.getWorld();
		}
		int count = getAPI().performBlessing(world);
		String s = String.format("Players blessed: %d", count);
		main.getLogger().info(s);
	}

	protected void commandFromConsole(CommandSender sender, String[] args) throws AlveranException {
		World world;
		if (args.length > 0) {
			world = getWorldFromCommandline(args);
			if (world == null) {
				sendMessage(sender, WORLD_NOT_FOUND);
				return;
			}
		} else {
			world = Bukkit.getWorld("world");
			if (world == null) {
				sendMessage(sender, NO_AUTOWORLD);
				return;
			}
		}
		int count = getAPI().performBlessing(world);
		String s = String.format("Players blessed: %d", count);
		sender.sendMessage(s);
	}

	protected void dispatchCommand(CommandSender sender, String[] args) {
		try {
			boolean isValidSender = false;

			// Sender is Player
			if (sender instanceof Player player) {
				isValidSender = true;
				commandFromPlayer(player, args);
			}

			// Sender is Command Block
			if (sender instanceof BlockCommandSender blockCommandSender) {
				isValidSender = true;
				commandFromBlock(blockCommandSender.getBlock(), args);
			}

			// Sender is Console
			if ((sender instanceof ConsoleCommandSender) || (sender instanceof RemoteConsoleCommandSender)) {
				isValidSender = true;
				commandFromConsole(sender, args);
			}

			if (!isValidSender) {
				sendMessage(null, UNKNOWN_SOURCE);
			}
		} catch (AlveranException ae) {
			sender.sendMessage(getAPI().getLocalizedExceptionText(ae, sender));
			if (ae.getCause() != null) {
				ae.printStackTrace();
			}
			String s = String.format("%s (%s)", getAPI().getLocalizedExceptionText(ae, null), ae.getMessage());
			main.getLogger().warning(s);
		} catch (Exception e) {
			sender.sendMessage(getAPI().getText(ERROR, sender));
			e.printStackTrace();
			String s = String.format("%s (%s)", getAPI().getText(Message.JAVA_EXCEPTION, null),
					Message.JAVA_EXCEPTION.toString());
			main.getLogger().warning(s);
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (getAPI() == null) {
			sendMessage(sender, NO_API);
		} else {
			dispatchCommand(sender, args);
		}
		return true;
	}

}
