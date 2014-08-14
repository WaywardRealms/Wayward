package net.wayward_realms.waywardlib.character;

import net.wayward_realms.waywardlib.combat.AI;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

/**
 * Represents a pet
 *
 */
public interface Pet extends AI, ConfigurationSerializable {

    /**
     * Gets the pet's ID
     *
     * @return the pet's ID
     */
    public int getId();

    /**
     * Gets the owner
     *
     * @return the owner
     */
    public Character getOwner();

    /**
     * Gets the race of the pet
     *
     * @return the race
     */
    public Race getRace();

}
