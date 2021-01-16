package main.java.utility;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CmdBroad implements CommandExecutor {

	@SuppressWarnings("unused")
	private Main plugin;

	public CmdBroad(Main plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
		if (commandSender.hasPermission("wmc.broad")) {
			StringBuffer broad = new StringBuffer();
			for (int i = 0; i<args.length; i++) {
				broad.append(args[i]);
			}
			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', broad.toString()));
		} else {
			commandSender.sendMessage(ChatColor.RED + "No Permission");
		}
		return true;
	}
}
