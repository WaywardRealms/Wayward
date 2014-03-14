package net.wayward_realms.waywardlib.character;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

/**
 * Represents a race
 *
 */
public interface Race extends ConfigurationSerializable {

    /**
     * Gets the name of the race
     *
     * @return the name
     */
    public String getName();

}
