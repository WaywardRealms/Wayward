package net.wayward_realms.waywardlib.lock;

import net.wayward_realms.waywardlib.WaywardPlugin;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a lock pluginWaywardPlugin
 *
 */
public interface LockPlugin extends WaywardPlugin {

    /**
     * Gets the ID of the lock of a block
     *
     * @param block the block
     * @return the id of the lock
     */
    public int getLockId(Block block);

    /**
     * Checks whether the block is locked
     *
     * @param block the block
     * @return true if the block is locked, otherwise false
     */
    public boolean isLocked(Block block);

    /**
     * Locks a block
     *
     * @param block the block
     * @return the key used to unlock the block
     */
    public ItemStack lock(Block block);

    /**
     * Removes the lock from a block
     *
     * @param block the block to unlock
     */
    public void unlock(Block block);

}
