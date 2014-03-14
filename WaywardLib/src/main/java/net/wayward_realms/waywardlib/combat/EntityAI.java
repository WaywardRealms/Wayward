package net.wayward_realms.waywardlib.combat;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

/**
 * Represents an entity's combat AIWaywardPlugin
 *
 */
public interface EntityAI extends AI {

    /**
     * Gets the entity this AI is for
     *
     * @return the entity
     */
    public Entity getEntity();

    /**
     * Gets the entity type this AI is for
     *
     * @return the entity type
     */
    public EntityType getEntityType();

    /**
     * Gets the level of entity this AI is for
     *
     * @return the level
     */
    public int getLevel();

}
