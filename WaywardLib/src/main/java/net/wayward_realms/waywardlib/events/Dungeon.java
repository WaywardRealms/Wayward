package net.wayward_realms.waywardlib.events;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.Collection;

/**
 * Represents a dungeon
 *
 */
public interface Dungeon extends ConfigurationSerializable {

    /**
     * Gets a collection of the dungeon masters responsible for the dungeon
     *
     * @return a collection containing the dungeon masters responsible for the dungeon
     */
    public Collection<OfflinePlayer> getDungeonMasters();

    /**
     * Adds a dungeon master to the dungeon masters responsible for this dungeon
     *
     * @param dungeonMaster the dungeon master to add
     */
    public void addDungeonMaster(OfflinePlayer dungeonMaster);

    /**
     * Removes a dungeon master from the dungeon masters responsible for this dungeon
     *
     * @param dungeonMaster the dungeon master to remove
     */
    public void removeDungeonMaster(OfflinePlayer dungeonMaster);

    /**
     * Gets a collection of the players playing in this dungeon
     *
     * @return a collection containing the players playing this dungeon
     */
    public Collection<OfflinePlayer> getPlayers();

    /**
     * Adds a player to the players playing in this dungeon
     *
     * @param player the player to add
     */
    public void addPlayer(OfflinePlayer player);

    /**
     * Removes a player from the players playing in this dungeon
     *
     * @param player the player to remove
     */
    public void removePlayer(OfflinePlayer player);

    /**
     * Gets whether the dungeon is currently active or not
     *
     * @return true if the dungeon is active, otherwise false
     */
    public boolean isActive();

    /**
     * Sets whether the dungeon is currently active or not
     *
     * @param active true if the dungeon is being started, false if the dungeon is stopping
     */
    public void setActive(boolean active);

}
