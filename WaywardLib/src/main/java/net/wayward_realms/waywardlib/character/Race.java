package net.wayward_realms.waywardlib.character;

import net.wayward_realms.waywardlib.classes.Stat;
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

    /**
     * Gets the stat bonus for the given stat
     *
     * @param stat the stat
     * @return the bonus in the given stat
     */
    public int getStatBonus(Stat stat);

}
