package net.wayward_realms.waywardlib.economy;

import net.wayward_realms.waywardlib.WaywardPlugin;
import net.wayward_realms.waywardlib.character.Character;
import org.bukkit.OfflinePlayer;

import java.util.Collection;

/**
 * Represents an economy plugin
 *
 */
public interface EconomyPlugin extends WaywardPlugin {

    /**
     * Gets the money owned by a player's active character in the primary currency
     *
     * @param player the player
     * @return the money owned by the player's active character
     */
    public int getMoney(OfflinePlayer player);

    /**
     * Sets the money owned by a player's active character in the primary currency
     *
     * @param player the player
     * @param amount the amount to set
     */
    public void setMoney(OfflinePlayer player, int amount);

    /**
     * Gives money to a player's active character in the primary currency
     *
     * @param player the player
     * @param amount the amount to add
     */
    public void addMoney(OfflinePlayer player, int amount);

    /**
     * Transfers money from one player's character to another in the primary currency
     *
     * @param takeFrom the player whose character the money should be taken from
     * @param giveTo the player whose character the money should be given to
     * @param amount the amount to transfer
     * @return whether successful. False indicates a transfer failure.
     */
    public boolean transferMoney(OfflinePlayer takeFrom, OfflinePlayer giveTo, int amount);

    /**
     * Gets the money owned by a character in the primary currency
     *
     * @param character the character
     * @return the money owned by the character
     */
    public int getMoney(Character character);

    /**
     * Sets the money owned by a character in the primary currency
     *
     * @param character the character
     * @param amount the amount to set
     */
    public void setMoney(Character character, int amount);

    /**
     * Gives money to a character in the primary currency
     *
     * @param character the character
     * @param amount the amount to add
     */
    public void addMoney(Character character, int amount);

    /**
     * Transfers money from one character to another in the primary currency
     *  @param takeFrom the character to take the money from
     * @param giveTo the character to give the money to
     * @param amount the amount to transfer
     */
    public boolean transferMoney(Character takeFrom, Character giveTo, int amount);

    /**
     * Gets the primary currency
     *
     * @return the primary currency
     */
    public Currency getPrimaryCurrency();

    /**
     * Sets the primary currency
     *
     * @param currency the currency to set
     */
    public void setPrimaryCurrency(Currency currency);

    /**
     * Gets all available currencies
     *
     * @return a collection containing all available currencies
     */
    public Collection<? extends Currency> getCurrencies();

    /**
     * Gets a currency by name
     *
     * @param name the name of the currency
     * @return the currency
     */
    public Currency getCurrency(String name);

    /**
     * Removes a currency
     *
     * @param currency the currency to remove
     */
    public void removeCurrency(Currency currency);

    /**
     * Adds a currency
     *
     * @param currency the currency to add
     */
    public void addCurrency(Currency currency);

    /**
     * Gets the active auctions
     *
     * @return a collection containing all active auctions
     */
    public Collection<? extends Auction> getAuctions();

    /**
     * Adds an auction
     *
     * @param auction the auction to add
     */
    public void addAuction(Auction auction);

    /**
     * Removes an auction
     *
     * @param auction the auction to remove
     */
    public void removeAuction(Auction auction);

    /**
     * Gets a character's active auction
     *
     * @param character the character
     * @return the auction being held currently by the character, if available, otherwise null
     */
    public Auction getAuction(Character character);

    /**
     * Gets the money owned by a player's active character in a given currency
     *
     * @param player the player
     * @param currency the currency
     * @return the money owned by the player's active character
     */
    public int getMoney(OfflinePlayer player, Currency currency);

    /**
     * Sets the money owned by a player's active character in a given currency
     *
     * @param player the player
     * @param currency the currency
     * @param amount the amount to set
     */
    public void setMoney(OfflinePlayer player, Currency currency, int amount);

    /**
     * Gives money to a player's active character in a given currency
     *
     * @param player the player
     * @param currency the currency
     * @param amount the amount to add
     */
    public void addMoney(OfflinePlayer player, Currency currency, int amount);

    /**
     * Transfers money from one player's character to another in a given currency
     *  @param takeFrom the player whose character the money should be taken from
     * @param giveTo the player whose character the money should be given to
     * @param currency the currency
     * @param amount the amount to transfer
     */
    public boolean transferMoney(OfflinePlayer takeFrom, OfflinePlayer giveTo, Currency currency, int amount);

    /**
     * Gets the money owned by a character in a given currency
     *
     * @param character the character
     * @param currency the currency
     * @return the money owned by the character
     */
    public int getMoney(Character character, Currency currency);

    /**
     * Sets the money owned by a character in a given currency
     *
     * @param character the character
     * @param currency the currency
     * @param amount the amount to set
     */
    public void setMoney(Character character, Currency currency, int amount);

    /**
     * Gives money to a character in a given currency
     *
     * @param character the character
     * @param currency the currency
     * @param amount the amount to add
     */
    public void addMoney(Character character, Currency currency, int amount);

    /**
     * Transfers money from one character to another in a given currency
     *  @param takeFrom the character to take the money from
     * @param giveTo the character to give the money to
     * @param currency the currency
     * @param amount the amount to transfer
     */
    public boolean transferMoney(Character takeFrom, Character giveTo, Currency currency, int amount);

    /**
     * Gets a character's bank balance in the default currency
     *
     * @param character the character
     * @return the character's bank balance
     */
    public int getBankBalance(Character character);

    /**
     * Gets a character's bank balance
     *
     * @param character the character
     * @param currency the currency
     * @return the character's bank balance
     */
    public int getBankBalance(Character character, Currency currency);

    /**
     * Sets a character's bank balance in the default currency
     *
     * @param character the character
     * @param amount the amount to set
     */
    public void setBankBalance(Character character, int amount);

    /**
     * Sets a character's bank balance
     *
     * @param character the character
     * @param currency the currency
     * @param amount the amount to set
     */
    public void setBankBalance(Character character, Currency currency, int amount);

}
