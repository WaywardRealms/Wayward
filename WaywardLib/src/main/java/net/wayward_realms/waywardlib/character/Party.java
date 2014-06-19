package net.wayward_realms.waywardlib.character;

import java.util.Collection;

/**
 * Represents a party of characters
 */
public interface Party {

    /**
     * Gets characters in the party
     *
     * @return the characters
     */
    public Collection<? extends Character> getCharacters();

    /**
     * Adds a character to the party
     *
     * @param character the character to add
     */
    public void addCharacter(Character character);

    /**
     * Removes a character from the party
     *
     * @param character the character to remove
     */
    public void removeCharacter(Character character);

}
