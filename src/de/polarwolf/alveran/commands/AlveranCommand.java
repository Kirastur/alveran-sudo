package de.polarwolf.alveran.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.polarwolf.alveran.main.Main;

public class AlveranCommand implements CommandExecutor{
	
	private final Main main;
	
	public AlveranCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {

		if ((arg0 instanceof Player) && (!main.getAlveranConfig().getExecuteAsPlayer())) {
			arg0.sendMessage(main.getAlveranConfig().getMessageExecutedAsPlayer((Player) arg0));
			return true;
		}
		
		main.getAlveranAPI().PerformBlessing();
		return true;
	}			

}
