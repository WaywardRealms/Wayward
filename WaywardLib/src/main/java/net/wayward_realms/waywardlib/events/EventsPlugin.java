package net.wayward_realms.waywardlib.events;

import net.wayward_realms.waywardlib.WaywardPlugin;
import net.wayward_realms.waywardlib.character.Race;
import org.bukkit.OfflinePlayer;

import java.util.Collection;

/**
 * Represents an events plugin
 *
 */
public interface EventsPlugin extends WaywardPlugin {

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

    /**
     * Gets the event character with the given ID
     *
     * @param id the id
     * @return the event character if there is one by the ID, otherwise null if the character doesn't exist, or is a normal character
     */
    public EventCharacter getEventCharacter(long id);

    /**
     * Creates a new event character for the given player
     *
     * @param player the player
     * @return the event character created
     */
    public EventCharacter createNewEventCharacter(OfflinePlayer player);

    /**
     * Gets an event character template by name
     *
     * @param name the name of the template
     * @return the event character template
     */
    public EventCharacterTemplate getEventCharacterTemplate(String name);

    /**
     * Adds an event character template
     *
     * @param template the template to add
     */
    public void addEventCharacterTemplate(EventCharacterTemplate template);

    /**
     * Removes an event character template
     *
     * @param template the template to remove
     */
    public void removeEventCharacterTemplate(EventCharacterTemplate template);

    /**
     * Gets an event race by name
     *
     * @param name the name
     * @return the race
     */
    public Race getRace(String name);

    /**
     * Adds an event race
     *
     * @param race the race to add
     */
    public void addRace(Race race);

    /**
     * Removes an event race
     *
     * @param race the race to remove
     */
    public void removeRace(Race race);

    /**
     * Gets a collection of all quests
     *
     * @return a {@link java.util.Collection} containing all available quests
     */
    public Collection<? extends Quest> getQuests();

    /**
     * Gets a quest by name
     *
     * @param name the name of the quest
     * @return the quest
     */
    public Quest getQuest(String name);

    /**
     * Adds a quest
     *
     * @param quest the quest to add
     */
    public void addQuest(Quest quest);

    /**
     * Removes a quest
     *
     * @param quest the quest to remove
     */
    public void removeQuest(Quest quest);

}
