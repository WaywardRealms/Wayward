package net.wayward_realms.waywardlib.donation;

import net.wayward_realms.waywardlib.WaywardPlugin;
import org.bukkit.OfflinePlayer;

import java.util.Collection;

/**
 * Represents a donation management pluginWaywardPlugin
 *
 */
public interface DonationPlugin extends WaywardPlugin {

    /**
     * Gets a collection containing the donation ranks of a given player
     *
     * @param player the player
     * @return a collection of the donation ranks of the player
     */
    public Collection<? extends DonationRank> getDonationRanks(OfflinePlayer player);

    /**
     * Adds a donation rank to a player
     *
     * @param player the player
     * @param rank the donation rank
     */
    public void addDonationRank(OfflinePlayer player, DonationRank rank);

    /**
     * Removes a donation rank from a player
     *
     * @param player the player
     * @param rank the donation rank
     */
    public void removeDonationRank(OfflinePlayer player, DonationRank rank);

    /**
     * Adds a donation rank
     *
     * @param rank the rank to add
     */
    public void addDonationRank(DonationRank rank);

    /**
     * Removes a donation rank
     *
     * @param rank the donation rank to remove
     */
    public void removeDonationRank(DonationRank rank);

    /**
     * Gets a donation rank by name
     *
     * @param name the name
     * @return the donation rank
     */
    public DonationRank getDonationRank(String name);

}
