package me.flail.microblocks.mcb;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import me.flail.microblocks.tools.Logger;
import me.flail.microblocks.user.User;

public class UserBlockHandler extends Logger {

	public static final int PROTECTION_RADIUS = 1;

	public static final String PLACED_METADATA = "MicroBlock-placed";

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
	 * @return true if the block was placed successfully. false otherwise.
	 */
	public boolean blockPlaceCheck(User user, Block blockPlaced, BlockState previousState) {
		boolean isOwner = false;
		MetadataValue metadata = new FixedMetadataValue(plugin, user.id());

		if (surroundingBlocksAreUnowned(user, blockPlaced)) {
			for (Block block : getSurroundingBlocks(blockPlaced)) {

				block.setMetadata(PLACED_METADATA, metadata);
			}

			isOwner = true;
		}


		if (!isOwner) {
			blockPlaced.setBlockData(previousState.getBlockData());
			blockPlaced.setType(previousState.getType());

			return isOwner;
		}

		blockPlaced.setMetadata(PLACED_METADATA, metadata);
		return isOwner;
	}


	/**
	 * Checks if the block destroyed is owned by this User.
	 * If it is not, the block destruction is cancelled.
	 * 
	 * @param user
	 *                  the User who destroyed the block.
	 * @param block
	 *                  the target block
	 * @return true if the block was broken and the user was the owner of the block or had a bypass.
	 *         false otherwise.
	 */
	public boolean blockDestroyCheck(User user, Block block) {
		boolean isOwner = true;

		if (block.hasMetadata(PLACED_METADATA)) {

			MetadataValue dataValue = block.getMetadata(PLACED_METADATA).get(0);
			if (dataValue.getOwningPlugin().getName().equalsIgnoreCase("MicroBlocks")) {
				UUID blockOwner = UUID.fromString(dataValue.asString());

				if ((blockOwner != null) && !blockOwner.equals(user.uuid())) {
					isOwner = false;
				}

			}

		}

		if (isOwner) {
			block.removeMetadata(PLACED_METADATA, plugin);
			for (Block b : getSurroundingBlocks(block)) {
				b.removeMetadata(PLACED_METADATA, plugin);
			}
		}

		return isOwner;
	}

	private boolean surroundingBlocksAreUnowned(User user, Block block) {
		Set<Boolean> ownedBlocks = new HashSet<>();

		for (Block relativeBlock : getSurroundingBlocks(block)) {

			if ((getOwner(relativeBlock) != null) && !getOwner(relativeBlock).equals(user.uuid())) {
				ownedBlocks.add(true);
			}
		}

		return ownedBlocks.isEmpty();
	}

	private Set<Block> getSurroundingBlocks(Block block) {
		Set<Block> blocks = new HashSet<>();

		int radius = PROTECTION_RADIUS * -1;

		for (int x = radius * 1; x <= PROTECTION_RADIUS; x++) {
			for (int y = radius * 1; y <= PROTECTION_RADIUS; y++) {
				for (int z = radius * 1; z <= PROTECTION_RADIUS; z++) {

					blocks.add(block.getRelative(x, y, z));
				}
			}
		}

		return blocks;
	}

	private UUID getOwner(Block block) {
		if (block.hasMetadata(PLACED_METADATA)) {
			List<MetadataValue> dataValues = block.getMetadata(PLACED_METADATA);
			UUID blockOwner;

			for (MetadataValue value : dataValues) {
				try {
					blockOwner = UUID.fromString(value.asString());

					return blockOwner;
				} catch (Throwable t) {
					continue;
				}

			}

		}

		return null;
	}

	public static boolean isBlockOwnedBy(User user, Block block) {
		if (plugin.blockData.containsKey(user)) {

			return plugin.blockData.get(user).contains(block.getLocation());
		}

		return false;
	}

}
