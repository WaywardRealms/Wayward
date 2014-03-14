package net.wayward_realms.waywardlib.essentials;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

/**
 * Represents an item kit
 * @author Lucariatias
 *
 */
public interface Kit extends ConfigurationSerializable {

    /**
     * Gets the items in the kit
     *
     * @return the items contained in the kit
     */
    public Collection<ItemStack> getItems();

    /**
     * Adds an item to the kit
     *
     * @param item the item to add
     */
    public void addItem(ItemStack item);

    /**
     * Removes an item from the kit
     *
     * @param item the item to remove
     */
    public void removeItem(ItemStack item);

    /**
     * Gives this kit to the given player
     *
     * @param player the player to give this kit
     */
    public void give(Player player);

}
