package main.java.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.utility.cmd.CmdBroad;
import main.java.utility.cmd.CmdWmsg;
import main.java.utility.cmd.CmdReload;
import main.java.utility.generator.VG;
import main.java.utility.votes.EventPlayerVote;
import net.ess3.api.IEssentials;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.chat.Chat;

public class Main extends JavaPlugin {

	private static Chat chat = null;
	private static IEssentials ess;

	public FileConfiguration Config = getConfig();
	public static Main plugin;

	Random rint = new Random();
	int currentPos;

	static String host, port, database, username, password;
	static Connection connection;
	static Statement vlstatement;

	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		getConfig();
		plugin = this;

		Bukkit.getPluginManager().registerEvents(new EventSetTab(this), this);
		/*try {
			Bukkit.getPluginManager().registerEvents(new EventPlayerVote(this), this);
		} catch (Exception noVotifier) {
			System.out.println("No votifier found or other exceptions, read below\n" + noVotifier);
		} */

		try {
			ess = (IEssentials) Bukkit.getPluginManager().getPlugin("Essentials");
		} catch (Exception ignore) {
		}
		getCommand("utilsreload").setExecutor(new CmdReload(this));
		getCommand("wmcbroad").setExecutor(new CmdBroad(this));
		getCommand("wmsg").setExecutor(new CmdWmsg());
		setupChat();

		if (Config.getBoolean("ab.enabled")) {
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
			}.runTaskTimer(plugin, 0L, Config.getInt("ab.time") * 20);
		}

		/*host = Config.getString("vl.host");
		port = Config.getString("vl.port");
		database = Config.getString("vl.database");
		username = Config.getString("vl.username");
		password = Config.getString("vl.password");
		if (host != "") {
			try {
				openConnection();
				vlstatement = connection.createStatement();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			(new BukkitRunnable() {
				@Override
				public void run() {
					try {
						if (connection != null && !connection.isClosed()) {
							connection.createStatement().execute("SELECT 1");
						}
					} catch (SQLException e) {
						try {
							openConnection();
						} catch (ClassNotFoundException e1) {
							e1.printStackTrace();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					}
				}
			}).runTaskTimerAsynchronously(plugin, 60 * 20, 60 * 20); 
		} */
	}

	public void openConnection() throws SQLException, ClassNotFoundException {
		if (connection != null && !connection.isClosed()) {
			return;
		}
		Class.forName("com.mysql.jdbc.Driver");
		connection = DriverManager.getConnection("jdbc:mysql://" + Main.host + ":" + Main.port + "/" + Main.database,
				Main.username, Main.password);
	}

	@Override
	public VG getDefaultWorldGenerator(String worldName, String id) {
		return new VG();
	}

	public String getBroadMessage() {
		ArrayList<String> msgs = new ArrayList<String>(Config.getStringList("ab.strings"));
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

	public static IEssentials getEss() {
		return ess;
	}

	public static void setUUIDDBValue(String table, String uuid, String columnToSet, String value) throws SQLException {
		if (readUUIDDBvalue(table, uuid, columnToSet) != "") {
			vlstatement.executeUpdate(
					"REPLACE INTO " + table + " (UUID, " + columnToSet + ") VALUES (" + uuid + "," + value + ");");
			return;
		}
		vlstatement.executeUpdate(
				"INSERT INTO " + table + " (UUID, " + columnToSet + ") VALUES (" + uuid + "," + value + ");");
		vlstatement.executeUpdate("IF COL_LENGTH('" + table + "','" + columnToSet + "') IS NULL BEGIN ALTER TABLE "
				+ table + "ADD " + columnToSet + " varchar(255) END");
	}

	public static String readUUIDDBvalue(String table, String uuid, String columnToRead) throws SQLException {
		String t = vlstatement.executeQuery("SELECT " + columnToRead + " FROM " + table + " WHERE UUID LIKE" + uuid)
				.toString();
		return (t != null || t == "" ? t : "");
	}
}
