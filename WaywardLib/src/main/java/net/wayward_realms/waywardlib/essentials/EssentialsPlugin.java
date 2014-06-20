package net.wayward_realms.waywardlib.essentials;

import net.wayward_realms.waywardlib.WaywardPlugin;
import net.wayward_realms.waywardlib.character.Character;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import java.util.Map;

/**
 * Represents an essentials plugin
 *
 */
public interface EssentialsPlugin extends WaywardPlugin {

    /**
     * Gets a map of names of warps to their locations
     *
     * @return a map containing the names of warps and their locations
     */
    public Map<String, Location> getWarps();

    /**
     * Gets a warp by it's name
     *
     * @param name the name of the warp
     * @return the location of the warp, or null if it doesn't exist
     */
    public Location getWarp(String name);

    /**
     * Adds a warp
     *
     * @param name the name of the warp
     * @param location the location of the warp
     */
    public void addWarp(String name, Location location);

    /**
     * Remvoes a warp
     *
     * @param name the name of the warp
     */
    public void removeWarp(String name);

    /**
     * Gets a map of names of kits to kits
     *
     * @return a map containing the names of kits and the kit
     */
    public Map<String, ? extends Kit> getKits();

    /**
     * Gets a kit by it's name
     *
     * @param name the name
     * @return the kit, or null if it doesn't exist
     */
    public Kit getKit(String name);

    /**
     * Adds a kit
     *
     * @param kit the kit to add
     */
    public void addKit(Kit kit);

    /**
     * Removes a kit
     *
     * @param kit the kit to remove
     */
    public void removeKit(Kit kit);

    /**
     * Gets a character's drunkenness
     *
     * @param character the character
     * @return how drunk the character is, between 0 and 100
     */
    public int getDrunkenness(net.wayward_realms.waywardlib.character.Character character);

    /**
     * Sets drunkenness of a character
     *
     * @param character the character
     * @param drunkenness the drunkenness to set, from 0 to 100
     */
    public void setDrunkenness(Character character, int drunkenness);

    /**
     * Gets the drunkenness of a player's active character
     *
     * @param player the player
     * @return how drunk the player's active character is, between 0 and 100
     */
    public int getDrunkenness(OfflinePlayer player);

    /**
     * Sets the drunkenness of a player's active character
     *
     * @param player the player
     * @param drunkenness the drunkenness to set, from 0 to 100
     */
    public void setDrunkenness(OfflinePlayer player, int drunkenness);

}
