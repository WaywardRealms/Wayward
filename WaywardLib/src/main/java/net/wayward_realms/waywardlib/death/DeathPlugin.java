package net.wayward_realms.waywardlib.death;

import net.wayward_realms.waywardlib.WaywardPlugin;
import net.wayward_realms.waywardlib.character.Character;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a death management plugin
 * @author Lucariatias
 *
 */
public interface DeathPlugin extends WaywardPlugin {

    /**
     * Gets an array of itemstacks containing the contents of the player's inventory when they died.
     * If they have not died or if they have reclaimed their items, this should return an empty array.
     *
     * @param player the player
     * @return an array of itemstacks containing the contents of their inventory the last time they died. If they have not died, or have reclaimed their items, this should return an empty array.
     */
    public ItemStack[] getDeathInventory(OfflinePlayer player);

    /**
     * Restores the items a player lost on death back to the player and returns them to the point of death
     *
     * @param player the player
     */
    public void wake(OfflinePlayer player);

    /**
     * Checks whether a player's active character is unconscious
     *
     * @param player the player
     * @return whether the player's active character is unconscious
     */
    public boolean isUnconscious(OfflinePlayer player);

    /**
     * Gets an array of itemstacks containing the contents of the character's inventory when they died.
     * If they have not died or if they have reclaimed their items, this should return an empty array.
     *
     * @param character the character
     * @return an array of itemstack containing the contents of their inventory the last time they died. If they have not died, or have reclaimed their items, this should return an empty array.
     */
    public ItemStack[] getDeathInventory(Character character);

    /**
     * Restores the items a character lost on death back to the character, and if active, returns them to the point of death
     *
     * @param character the character
     */
    public void wake(Character character);

    /**
     * Checks whether a character is unconscious
     *
     * @param character the character
     * @return whether the character is unconscious
     */
    public boolean isUnconscious(Character character);

}
