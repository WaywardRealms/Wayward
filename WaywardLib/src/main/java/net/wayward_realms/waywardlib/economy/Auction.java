package net.wayward_realms.waywardlib.economy;

import net.wayward_realms.waywardlib.character.Character;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

/**
 * Represents an auctionWaywardPlugin
 *
 */
public interface Auction extends ConfigurationSerializable {

    /**
     * Gets the item under auction
     *
     * @return the item under auction
     */
    public ItemStack getItem();

    /**
     * Sets the item under auction
     *
     * @param item the item under auction
     */
    public void setItem(ItemStack item);

    /**
     * Gets the currency this auction is accepting
     *
     * @return the currency
     */
    public Currency getCurrency();

    /**
     * Sets the currency this auction is accepting
     *
     * @param currency the currency to set
     */
    public void setCurrency(Currency currency);

    /**
     * Gets the location at which the auction is occuring
     *
     * @return the location of the auction
     */
    public Location getLocation();

    /**
     * Sets the location of the auction
     *
     * @param location the location to set
     */
    public void setLocation(Location location);

    /**
     * Gets the character auctioning the item
     *
     * @return the character
     */
    public net.wayward_realms.waywardlib.character.Character getCharacter();

    /**
     * Sets the character auctioning the item
     *
     * @param character the character to set
     */
    public void setCharacter(Character character);

    /**
     * Gets the highest bidder so far
     *
     * @return the highest bidder
     */
    public Bid getHighestBid();

    /**
     * Gets a collection of the bids made so far
     *
     * @return a collection containing the bids
     */
    public Collection<? extends Bid> getBids();

    /**
     * Removes a bid
     *
     * @param bid the bid to remove
     */
    public void removeBid(Bid bid);

    /**
     * Adds a bid
     *
     * @param bid the bid to add
     */
    public void addBid(Bid bid);

    /**
     * Gets the minimum amount a bid must be increased by with each new bid
     *
     * @return the minimum bid increment
     */
    public int getMinimumBidIncrement();

    /**
     * Sets the minimum amount a bid must be increased by with each new bid
     *
     * @param minBidIncrement the minimum bid increment to set
     */
    public void setMinimumBidIncrement(int minBidIncrement);

    /**
     * Opens bidding, allowing bids to be made
     */
    public void openBidding();

    /**
     * Closes bidding, giving the item to the character who made the highest bid
     */
    public void closeBidding();

    /**
     * Gets whether bidding is currently open
     *
     * @return whether bidding is open
     */
    public boolean isBiddingOpen();

}
