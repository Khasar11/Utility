package main.java.utility;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;

public class EventSetTab implements Listener {

	private Main plugin;

	public EventSetTab(Main plugin) {
		this.plugin = plugin;
	}

	String header, footer, player;

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (plugin.getConfig().getBoolean("tab.enabled")) {
			Player p = event.getPlayer();
			for (Player subP : Bukkit.getOnlinePlayers()) {
				updateTab(subP);
			}
			new BukkitRunnable() {
				@Override
				public void run() {
					try {
						updateTab(p);
					} catch (Exception cancel) {
						cancel();
					}
				}
			}.runTaskTimer(plugin, 0L, 200L);
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		if (plugin.getConfig().getBoolean("tab.enabled")) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				updateTab(p);
			}
		}
	}

	public void updateTab(Player p) {
		header = replaceVariables(plugin.getConfig().getString("tab.header"), p);
		footer = replaceVariables(plugin.getConfig().getString("tab.footer"), p);
		player = replaceVariables(plugin.getConfig().getString("tab.username"), p);
		p.setPlayerListHeader(header);
		p.setPlayerListName(player);
		p.setPlayerListFooter(footer);
	}

	public String replaceVariables(String replaceString, Player p) {
		String[] pGroups = Main.getChat().getPlayerGroups(p);
		replaceString = replaceString.replace("{MAXPLAYERS}", plugin.getServer().getMaxPlayers() + "")
				.replace("{PLAYERS}", plugin.getServer().getOnlinePlayers().size() + "")
				.replace("{USERNAME}", p.getName()).replace("{DISPLAYNAME}", p.getDisplayName())
				.replace("{PREFIX}", Main.getChat().getPlayerPrefix(p))
				.replace("{SUFFIX}", Main.getChat().getPlayerSuffix(p));
		if (Main.getChat().getPlayerPrefix(p) == "") {
			replaceString = replaceString.replace("{PREFIX}",
					Main.getChat().getGroupPrefix(p.getWorld(), pGroups[0]));
		}
		if (Main.getChat().getPlayerSuffix(p) == "") {
			replaceString = replaceString.replace("{SUFFIX}",
					Main.getChat().getGroupSuffix(p.getWorld(), pGroups[0]));
		}
		return ChatColor.translateAlternateColorCodes('&', replaceString);
	}
}
