package main.java.utility.cmd;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import main.java.utility.Main;

public class CmdBroad implements CommandExecutor {

	@SuppressWarnings("unused")
	private Main plugin;

	public CmdBroad(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
		if (commandSender.hasPermission("wmc.broad")) {
			String msg = "";
			for (String curr : args) {
				if (curr != args[0])
					msg += " " + curr;
				else msg += curr;
			}
			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
		} else {
			commandSender.sendMessage(ChatColor.RED + "No Permission");
		}
		return true;
	}
}
