package main.java.utility;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.chat.Chat;

public class Main extends JavaPlugin {

	private static Chat chat = null;

	public FileConfiguration Config = getConfig();

	public static Main plugin;

	Random rint = new Random();
	int currentPos;

	@Override
	public VG getDefaultWorldGenerator(String worldName, String id) {
		return new VG();
	}

	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		getConfig();
		plugin = this;
		
		Bukkit.getPluginManager().registerEvents(new EventSetTab(this), this);
		
		getCommand("utilsreload").setExecutor(new CmdReload(this));
		setupChat();

		if (plugin.getConfig().getBoolean("ab.enabled")) {
			new BukkitRunnable() {
				String broadString;

				@Override
				public void run() {
					broadString = getBroadMessage();
					for (Player p : Bukkit.getOnlinePlayers()) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', broadString));
					}
					currentPos++;
				}
			}.runTaskTimer(plugin, 0L, plugin.getConfig().getInt("ab.time") * 20);
		}
	}

	public String getBroadMessage() {
		ArrayList<String> msgs = new ArrayList<String>(plugin.getConfig().getStringList("ab.strings"));
		if (currentPos >= msgs.size()) {
			currentPos = 0;
		}
		return msgs.get(currentPos);
	}

	private boolean setupChat() {
		RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager()
				.getRegistration(net.milkbowl.vault.chat.Chat.class);
		if (chatProvider != null) {
			chat = chatProvider.getProvider();
		}
		return (chat != null);
	}

	public static Chat getChat() {
		return chat;
	}
}
