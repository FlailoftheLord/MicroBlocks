package me.flail.microblocks.listeners;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.flail.microblocks.mcb.UserBlockHandler;
import me.flail.microblocks.tools.Logger;
import me.flail.microblocks.tools.Message;
import me.flail.microblocks.user.User;

public class BlockListener extends Logger implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlace(BlockPlaceEvent event) {
		if (!event.isCancelled()) {

			User user = new User(event.getPlayer().getUniqueId());

			Block block = event.getBlock();
			BlockState previousBlock = event.getBlockReplacedState();

			boolean blockPlaced = new UserBlockHandler().blockPlaceCheck(user, block, previousBlock);
			if (!blockPlaced) {

				new Message("CantPlaceBlockHere").send(user, null);
			}

		}

	}

	@EventHandler(priority = EventPriority.LOW)
	public void onDestroy(BlockBreakEvent event) {
		if (!event.isCancelled()) {
			User user = new User(event.getPlayer().getUniqueId());

			Block block = event.getBlock();

			boolean canBeBroken = new UserBlockHandler().blockDestroyCheck(user, block);

			event.setCancelled(!canBeBroken);

			if (!canBeBroken) {

				new Message("CantBreakBlockHere").send(user, null);
			}
		}

	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onInteract(PlayerInteractEvent event) {


	}

}
