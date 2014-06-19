package net.wayward_realms.waywardlib.character;

import net.wayward_realms.waywardlib.WaywardPlugin;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * Represents a character plugin
 *
 */
public interface CharacterPlugin extends WaywardPlugin {

    /**
     * Gets the active character of the given player
     *
     * @param player the player
     * @return the player's active character
     */
    public Character getActiveCharacter(OfflinePlayer player);

    /**
     * Sets a player's active character.
     * Should also manage changing their location, inventory, class, etc.
     *
     * @param player the player
     * @param character the character to set
     */
    public void setActiveCharacter(Player player, Character character);

    /**
     * Gets a collection of the player's characters
     *
     * @param player the player
     * @return a collection containing all of the player's characters
     */
    public Collection<? extends Character> getCharacters(OfflinePlayer player);

    /**
     * Adds a character to a player
     *
     * @param player the player
     * @param character the character to add
     */
    public void addCharacter(OfflinePlayer player, Character character);

    /**
     * Removes a character from a player
     *
     * @param player the player
     * @param character the character to remove
     */
    public void removeCharacter(OfflinePlayer player, Character character);

    /**
     * Attempts to completely remove a character from any players to which it is assigned
     *
     * @param character the character to remove
     */
    public void removeCharacter(Character character);

    /**
     * Creates a new character
     *
     * @param player the player to create the character for
     * @return the character
     */
    public Character createNewCharacter(OfflinePlayer player);

    /**
     * Gets a character by ID
     *
     * @param id the id of the character
     * @return the character
     */
    public Character getCharacter(int id);

    /**
     * Gets a collection of all the races
     *
     * @return a collection containing all the races
     */
    public Collection<? extends Race> getRaces();

    /**
     * Gets a race by name
     *
     * @param name the name
     * @return the race
     */
    public Race getRace(String name);

    /**
     * Adds a race
     *
     * @param race the race to add
     */
    public void addRace(Race race);

    /**
     * Removes a race
     *
     * @param race the race to remove
     */
    public void removeRace(Race race);

    /**
     * Gets a collection of all the genders
     *
     * @return a collection containing all the genders
     */
    public Collection<? extends Gender> getGenders();

    /**
     * Gets a gender by name
     *
     * @param name the name
     * @return the gender
     */
    public Gender getGender(String name);

    /**
     * Adds a gender
     *
     * @param gender the gender to add
     */
    public void addGender(Gender gender);

    /**
     * Removes a gender
     *
     * @param gender the gender to remove
     */
    public void removeGender(Gender gender);

    /**
     * Gets the next available ID to assign characters
     *
     * @return the next available ID
     */
    public int getNextAvailableId();

    /**
     * Sets the next available ID to assign characters
     *
     * @param id the ID to set
     */
    public void setNextAvailableId(int id);

    /**
     * Increments the next ID to assign characters
     */
    public void incrementNextAvailableId();

    /**
     * Gets the party a character is in
     *
     * @param character the character
     * @return the party, or null if the character does not have a party
     */
    public Party getParty(Character character);

}
