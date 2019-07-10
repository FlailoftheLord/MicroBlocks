package me.flail.microblocks;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.flail.microblocks.listeners.BlockListener;
import me.flail.microblocks.tools.Logger;
import me.flail.microblocks.user.User;

public class MicroBlocks extends JavaPlugin {

	public Map<UUID, User> userMap = new LinkedHashMap<>();

	public Set<UUID> msgCooldowns = new LinkedHashSet<>();

	public Server server;
	public PluginManager pluginManager;
	private Logger logger;

	@Override
	public void onLoad() {

		server = getServer();
		pluginManager = server.getPluginManager();

		logger = new Logger();
	}

	@Override
	public void onEnable() {

		registerListeners();

		for (Player player : server.getOnlinePlayers()) {
			User user = new User(player.getUniqueId());

			userMap.put(user.uuid(), user);
		}

	}

	public void registerListeners() {
		pluginManager.registerEvents(new BlockListener(), this);

	}

}
