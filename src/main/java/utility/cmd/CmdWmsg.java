package main.java.utility.cmd;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CmdWmsg implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		String msg = "";
		for (String curr : args) {
			if (curr != args[0])
				msg += " " + curr;
			else
				msg += curr;
		}
		s.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
		return true;
	}

}
