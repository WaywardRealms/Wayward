package net.wayward_realms.waywardlib.permissions;

import net.wayward_realms.waywardlib.WaywardPlugin;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;

import java.util.Set;

/**
 * Represents a permissions pluginWaywardPlugin
 *
 */
public interface PermissionsPlugin extends WaywardPlugin {

    /**
     * Gets the names of the global groups of which the player is in
     *
     * @param player the player
     * @return the names of the player's groups
     */
    public Set<String> getGroups(OfflinePlayer player);

    /**
     * Gets the names of the world-specific groups of which the player is in
     *
     * @param player the player
     * @param world the world
     * @return the names of the player's world-specific groups
     */
    public Set<String> getGroups(OfflinePlayer player, World world);

    /**
     * Sets a player's group to the one of the given name
     *
     * @param player the player
     * @param groupName the name of the group
     */
    public void setGroup(OfflinePlayer player, String groupName);

    /**
     * Sets a player's world-specific group to the one of the given name
     *
     * @param player the player
     * @param world the world
     * @param groupName the name of the group
     */
    public void setGroup(OfflinePlayer player, World world, String groupName);

    /**
     * Adds a group to a player
     *
     * @param player the player
     * @param groupName the name of the group
     */
    public void addGroup(OfflinePlayer player, String groupName);

    /**
     * Adds a world-specific group to a player
     *
     * @param player the player
     * @param world the world
     * @param groupName the name of the group
     */
    public void addGroup(OfflinePlayer player, World world, String groupName);

    /**
     * Removes a group from a player
     *
     * @param player the player
     * @param groupName the name of the group
     */
    public void removeGroup(OfflinePlayer player, String groupName);

    /**
     * Removes a world-specific group from a player
     *
     * @param player the player
     * @param world the world
     * @param groupName the name of the group
     */
    public void removeGroup(OfflinePlayer player, World world, String groupName);

}
