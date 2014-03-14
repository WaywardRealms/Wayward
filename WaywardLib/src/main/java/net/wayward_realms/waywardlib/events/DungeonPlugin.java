package net.wayward_realms.waywardlib.events;

import net.wayward_realms.waywardlib.WaywardPlugin;

import java.util.Collection;

/**
 * Represents a dungeon plugin
 * @author Lucariatias
 *
 */
public interface DungeonPlugin extends WaywardPlugin {

    /**
     * Gets a collection containing the dungeons
     *
     * @return collection containing all dungeons
     */
    public Collection<? extends Dungeon> getDungeons();

    /**
     * Gets a dungeon by name
     *
     * @param name the name of the dungeon
     * @return the dungeon with the given name
     */
    public Dungeon getDungeon(String name);

    /**
     * Removes a dungeon
     *
     * @param dungeon the dungeon to remove
     */
    public void removeDungeon(Dungeon dungeon);

    /**
     * Adds a dungeon
     *
     * @param dungeon the dungeon to add
     */
    public void addDungeon(Dungeon dungeon);

}
