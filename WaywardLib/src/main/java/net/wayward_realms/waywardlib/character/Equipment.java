package net.wayward_realms.waywardlib.character;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

/**
 * Represents equipment carried by a character
 *
 */
public interface Equipment extends ConfigurationSerializable {

    /**
     * Gets the item held in the character's preferred hand.
     *
     * @return the onhand item
     */
    public ItemStack getOnHandItem();

    /**
     * Gets the item held in the character's offhand
     *
     * @return the offhand item
     */
    public ItemStack getOffHandItem();

    /**
     * Gets the character's pet
     *
     * @return the pet
     */
    public Pet getPet();

    /**
     * Gets an array of size 9 containing the scrolls assigned. Unassigned slots are null.
     *
     * @return an array containing assigned scroll slots
     */
    public ItemStack[] getScrolls();

}
