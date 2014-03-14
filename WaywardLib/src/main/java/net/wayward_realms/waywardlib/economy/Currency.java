package net.wayward_realms.waywardlib.economy;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

/**
 * Represents a currency
 * @author Lucariatias
 *
 */
public interface Currency extends ConfigurationSerializable {

    /**
     * Gets the name of the currency
     *
     * @return the name of the currency
     */
    public String getName();

    /**
     * Sets the name of the currency
     *
     * @param name the name to set
     */
    public void setName(String name);

    /**
     * Gets the singular name for the currency
     *
     * @return the singular name
     */
    public String getNameSingular();

    /**
     * Sets the singular name for the currency
     *
     * @param name the name to set
     */
    public void setNameSingular(String name);

    /**
     * Gets the plural name for the currency
     *
     * @return the plural name
     */
    public String getNamePlural();

    /**
     * Sets the plural name for the currency
     *
     * @param name the name to set
     */
    public void setNamePlural(String name);

    /**
     * Converts an amount of the currency to another currency
     *
     * @param amount the amount of the currency to convert
     * @param currency the currency to convert to
     * @return the amount the currency is worth in the new currency
     */
    public int convert(int amount, Currency currency);

    /**
     * Gets the rate the currency can be converted to
     *
     * @return the rate the currency can be converted to
     */
    public int getRate();

    /**
     * Sets the rate the currency can be converted to
     *
     * @param rate the rate to set
     */
    public void setRate(int rate);

    /**
     * Gets the default amount of the currency possessed by a character upon starting out
     *
     * @return the default amount of the currency owned
     */
    public int getDefaultAmount();

    /**
     * Sets the default amount of the currency possessed by a character upon starting out
     *
     * @param amount the amount to set
     */
    public void setDefaultAmount(int amount);

}
