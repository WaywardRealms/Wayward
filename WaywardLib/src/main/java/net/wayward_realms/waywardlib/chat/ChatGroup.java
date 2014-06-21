package net.wayward_realms.waywardlib.chat;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * Represents a chat group
 */
public interface ChatGroup {

    /**
     * Gets the name
     *
     * @return the name
     */
    public String getName();

    /**
     * Gets the players
     *
     * @return the players
     */
    public Collection<OfflinePlayer> getPlayers();

    /**
     * Adds a player
     *
     * @param player the player to add
     */
    public void addPlayer(OfflinePlayer player);

    /**
     * Removes a player
     *
     * @param player the player to remove
     */
    public void removePlayer(OfflinePlayer player);

    /**
     * Gets players who have been invited
     *
     * @return the players who have been invited
     */
    public Collection<OfflinePlayer> getInvited();

    /**
     * Checks if a player has been invited
     *
     * @param player the player
     * @return whether the player is invited
     */
    public boolean isInvited(Player player);

    /**
     * Invites a player
     *
     * @param player the player to invite
     */
    public void invite(OfflinePlayer player);

    /**
     * Uninvites a player
     *
     * @param player the player to uninvite
     */
    public void uninvite(OfflinePlayer player);

    /**
     * Sends a message to the chat group
     *
     * @param sender the sender of the message
     * @param message the message
     */
    public void sendMessage(Player sender, String message);

}
