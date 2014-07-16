package net.wayward_realms.waywardlib.monsters;

import net.wayward_realms.waywardlib.WaywardPlugin;
import net.wayward_realms.waywardlib.classes.Stat;
import org.bukkit.entity.Entity;

/**
 * Represents a monsters plugin
 */
public interface MonstersPlugin extends WaywardPlugin {

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
