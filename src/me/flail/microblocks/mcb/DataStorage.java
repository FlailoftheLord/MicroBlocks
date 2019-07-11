package me.flail.microblocks.mcb;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;

import me.flail.microblocks.MicroBlocks;
import me.flail.microblocks.tools.DataFile;
import me.flail.microblocks.tools.Logger;
import me.flail.microblocks.user.User;


/**
 * Controls the block data storage system for MicroBlocks.
 * <br>
 * <br>
 * The two main methods are {@link DataStorage#saveBlockData(User user)} and
 * {@link DataStorage#loadBlockData(User user)} which saves the block data from local cache to
 * database, and loads from database to local cache respectively, for the specified User.
 * 
 * @author FlailoftheLord
 * @since 1.0.0
 */
@SuppressWarnings("unchecked")
public class DataStorage extends Logger {
	private static MicroBlocks plugin = JavaPlugin.getPlugin(MicroBlocks.class);

	protected DataStorage() {
	}


	public static void saveBlockData(User user) {
		Set<Location> blocks = plugin.blockData.get(user);
		DataFile dataStorage = user.getBlockData();

		if (dataStorage.hasValue("Blocks")) {
			blocks.addAll((Set<Location>) dataStorage.getObj("Blocks"));

		}

		dataStorage.setValue("Blocks", blocks);

	}

	public static void loadBlockData(User user) {
		DataFile dataStorage = user.getBlockData();
		if (dataStorage.hasValue("Blocks")) {
			plugin.blockData.put(user, (Set<Location>) dataStorage.getObj("Blocks"));

		}

	}

	public static void addBlockToUser(User user, Block block) {
		Set<Location> blocks = plugin.blockData.get(user);

		blocks.add(block.getLocation());
		plugin.blockData.put(user, blocks);
	}

	public static void removeBlockFromUser(User user, Block block) {
		Set<Location> blocks = plugin.blockData.get(user);

		blocks.remove(block.getLocation());
		plugin.blockData.put(user, blocks);
	}

}
