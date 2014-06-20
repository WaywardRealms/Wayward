package net.wayward_realms.waywardlib.moderation;

import net.wayward_realms.waywardlib.WaywardPlugin;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * Represents a moderation plugin
 *
 */
public interface ModerationPlugin extends WaywardPlugin {

    /**
     * Gets a map of dates to the type of material which the block in question changed to at the given date.
     *
     * @param block the block to get material changes for
     * @return a map of dates to materials at the given date
     */
    public Map<Date, Material> getBlockMaterialChanges(Block block);

    /**
     * Gets a map of dates to the data associated with the block at the given date.
     *
     * @param block the block to get the data changes for
     * @return a map of the dates to the data at the given date
     * @deprecated magic value
     */
    @Deprecated
    public Map<Date, Byte> getBlockDataChanges(Block block);

    /**
     * Gets a map of dates to the content changes
     *
     * @param inventory the inventory
     * @return a map of dates to itemstack changes in the inventory
     */
    public Map<Date, ItemStack> getInventoryContentChanges(Inventory inventory);

    /**
     * Gets a block's material at a given time
     *
     * @param block the block to get the material of
     * @param date the date to get the material of the block at
     * @return the block's material at the given date
     */
    public Material getBlockMaterialAtTime(Block block, Date date);

    /**
     * Gets a block's data at a given time
     *
     * @param block the block to check the data of
     * @param date the date to get the data at
     * @return the block's data at the given date
     * @deprecated magic value
     */
    @Deprecated
    public Byte getBlockDataAtTime(Block block, Date date);

    /**
     * Gets an inventory's contents at a given time
     *
     * @param inventory the inventory to get the contents of
     * @param date the date to get the contents at
     * @return an array containing the contents of the inventory at the given date
     */
    public ItemStack[] getInventoryContentsAtTime(Inventory inventory, Date date);

    /**
     * Returns whether a player is vanished
     *
     * @param player the player to check
     * @return true if the player is vanished, false if the player is visible
     */
    public boolean isVanished(Player player);

    /**
     * Sets whether a player is vanished
     *
     * @param player the player
     * @param vanished the vanished state to set the player to
     */
    public void setVanished(Player player, boolean vanished);

    /**
     * Gets the warnings a player has recieved
     *
     * @return a collection of the warnings
     */
    public Collection<? extends Warning> getWarnings(OfflinePlayer player);

    /**
     * Adds a warning to the player
     *
     * @param player the player to add a warning to
     * @param warning the warning to add
     */
    public void addWarning(OfflinePlayer player, Warning warning);

    /**
     * Removes a warning from a player
     *
     * @param player the player
     * @param warning the warning to remove
     */
    public void removeWarning(OfflinePlayer player, Warning warning);

    /**
     * Gets the tickets filed
     *
     * @return a collection of all the tickets filed
     */
    public Collection<? extends Ticket> getTickets();

    /**
     * Gets the tickets filed by a specific player
     *
     * @param player the player to get tickets from
     * @return a collection of the tickets filed by the given player
     */
    public Collection<? extends Ticket> getTickets(OfflinePlayer player);

    /**
     * Files a ticket
     *
     * @param player the player who is filing the ticket
     * @param ticket the ticket being filed
     */
    public void addTicket(OfflinePlayer player, Ticket ticket);

    /**
     * Removes a ticket
     *
     * @param ticket the ticket to remove
     */
    public void removeTicket(Ticket ticket);

    /**
     * Gets a player's reputation
     *
     * @param player the player
     * @return the reputation
     */
    public int getReputation(OfflinePlayer player);

    /**
     * Sets a player's reputation
     *
     * @param player the player
     * @param reputation the reputation to set
     */
    public void setReputation(OfflinePlayer player, int reputation);

}
