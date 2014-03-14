package net.wayward_realms.waywardlib.character;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

/**
 * Represents a gender
 *
 */
public interface Gender extends ConfigurationSerializable {

    /**
     * Gets the name of the gender
     *
     * @return the name of the gender
     */
    public String getName();

}
