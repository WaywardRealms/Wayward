package net.wayward_realms.waywardlib.economy;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.Date;

/**
 * Represents a bidWaywardPlugin
 *
 */
public interface Bid extends ConfigurationSerializable {

    /**
     * Gets the character who made the bid
     *
     * @return the character
     */
    public net.wayward_realms.waywardlib.character.Character getCharacter();

    /**
     * Gets the amount bidded
     *
     * @return the amount
     */
    public int getAmount();

    /**
     * Gets the time of the bid
     *
     * @return the time the bid was made
     */
    public Date getTime();

}
