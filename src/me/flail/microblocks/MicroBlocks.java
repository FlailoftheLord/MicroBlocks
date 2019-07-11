package me.flail.microblocks;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.flail.microblocks.listeners.BlockListener;
import me.flail.microblocks.mcb.DataStorage;
import me.flail.microblocks.tools.Logger;
import me.flail.microblocks.tools.Settings;
import me.flail.microblocks.user.User;

public class MicroBlocks extends JavaPlugin {

	public Map<UUID, User> userMap = new LinkedHashMap<>();
	public Map<User, Set<Location>> blockData = new HashMap<>();

	public Set<UUID> msgCooldowns = new LinkedHashSet<>();

	public Server server;
	public PluginManager pluginManager;
	private Logger logger;
	public Settings settings;

	@Override
	public void onLoad() {

		server = getServer();
		pluginManager = server.getPluginManager();

		logger = new Logger();
		settings = new Settings();
	}

	@Override
	public void onEnable() {
		long startTime = System.currentTimeMillis();

		registerListeners();

		for (Player player : server.getOnlinePlayers()) {
			User user = new User(player.getUniqueId());

			userMap.put(user.uuid(), user);
		}

		settings.load();

		for (OfflinePlayer offlineUser : server.getOfflinePlayers()) {
			User user = new User(offlineUser.getUniqueId());

			DataStorage.loadBlockData(user);
		}

		logger.console("&aStartup complete. &8(&7" + (System.currentTimeMillis() - startTime) + "ms&8)");
	}

	@Override
	public void onDisable() {
		for (User user : blockData.keySet()) {

			DataStorage.saveBlockData(user);
		}

	}

	public void registerListeners() {
		pluginManager.registerEvents(new BlockListener(), this);

	}

}
