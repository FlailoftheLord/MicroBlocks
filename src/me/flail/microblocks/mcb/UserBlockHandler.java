package me.flail.microblocks.mcb;

import java.util.UUID;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import me.flail.microblocks.tools.Logger;
import me.flail.microblocks.user.User;

public class UserBlockHandler extends Logger {

	public static final int PROTECTION_RADIUS = 1;

	public static final String PLACED_METADATA = "MicroBlock-placed";
	public static final String DESTROYED_METADATA = "MicoBlock-destroyed";

	/**
	 * Checks when a user places a block, if the block is nearby other player's blocks.<br>
	 * Prevents it if it doesn't meet requirements.
	 * 
	 * @param user
	 *                          the User who is placing the block.
	 * @param blockPlaced
	 *                          the block in question.
	 * @param previousState
	 *                          the previous state of this Block.
	 * @param itemUsed
	 *                          the item used to place the Block.
	 * @return true if the block was placed successfully. false otherwise.
	 */
	public boolean blockPlaceCheck(User user, Block blockPlaced, BlockState previousState, ItemStack itemUsed) {
		boolean isOwner = true;
		MetadataValue metadata = new FixedMetadataValue(plugin, user.id());

		for (int radius = PROTECTION_RADIUS * -1; radius <= PROTECTION_RADIUS; radius++) {
			Block block = blockPlaced.getRelative(radius, radius, radius);

			if (block.hasMetadata(PLACED_METADATA)) {

				for (MetadataValue dataValue : block.getMetadata(PLACED_METADATA)) {
					if (dataValue.getOwningPlugin().getName().equalsIgnoreCase("MicroBlocks")) {
						UUID blockOwner = UUID.fromString(dataValue.asString());

						if ((blockOwner != null) && !blockOwner.equals(user.uuid())) {
							isOwner = false;
						}

					}

				}

				continue;
			}

			block.setMetadata(PLACED_METADATA, metadata);

		}

		if (!isOwner) {
			blockPlaced.setBlockData(previousState.getBlockData());
			blockPlaced.setType(previousState.getType());

			ItemStack item = new ItemStack(itemUsed);
			item.setAmount(1);
			itemUsed.setAmount(itemUsed.getAmount() - 1);
			user.player().getInventory().addItem(item);

			return isOwner;
		}

		blockPlaced.setMetadata(PLACED_METADATA, metadata);
		return isOwner;
	}

	public boolean blockDestroyCheck(User user, Block block) {
		boolean isOwner = true;

		return isOwner;
	}

}
