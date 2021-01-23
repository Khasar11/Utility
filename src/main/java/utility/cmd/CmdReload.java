package main.java.utility.cmd;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import main.java.utility.Main;

public class CmdReload implements CommandExecutor {

	private Main plugin;

	public CmdReload(Main plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
		if (commandSender.hasPermission("wmc.reload")) {
			plugin.reloadConfig();
			commandSender.sendMessage(ChatColor.YELLOW + "Reloaded the config");
		} else {
			commandSender.sendMessage(ChatColor.RED + "No Permission");
		}
		return true;
	}
}
