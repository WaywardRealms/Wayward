package net.wayward_realms.waywardlib.monsters;

import net.wayward_realms.waywardlib.WaywardPlugin;
import net.wayward_realms.waywardlib.classes.Stat;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.Collection;

/**
 * Represents a monsters plugin
 */
public interface MonstersPlugin extends WaywardPlugin {

    /**
     * Gets the locations of all the points at which the mob level is zero
     *
     * @return a collection containing all the zero-points
     */
    public Collection<Location> getZeroPoints();

    /**
     * Adds a location at which mobs will be level zero
     *
     * @param zeroPoint the point to add
     */
    public void addZeroPoint(Location zeroPoint);

    /**
     * Removes a location at which mobs will be level zero
     *
     * @param zeroPoint the point to remove
     */
    public void removeZeroPoint(Location zeroPoint);

    /**
     * Gets an entity level
     *
     * @param entity the entity
     * @return the level of the entity
     */
    public int getEntityLevel(Entity entity);

    /**
     * Gets the stat value for an entity
     *
     * @param entity the entity
     * @param stat the stat
     * @return the value of the stat for the given entity
     */
    public int getEntityStatValue(Entity entity, Stat stat);

}
