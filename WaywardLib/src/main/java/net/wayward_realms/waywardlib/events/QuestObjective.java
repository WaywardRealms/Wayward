package net.wayward_realms.waywardlib.events;

import net.wayward_realms.waywardlib.character.Character;

import java.util.Collection;

/**
 * Represents an objective for a quest
 */
public interface QuestObjective {

    /**
     * Gets the characters who have completed the objective
     *
     * @return a {@link java.util.Collection} of {@link net.wayward_realms.waywardlib.character.Character}s who have completed the objective
     */
    public Collection<? extends Character> getCharacters();

    /**
     * Gets whether the character has completed the objective
     *
     * @param character the character
     * @return whether the character has completed the objective
     */
    public boolean hasCompleted(Character character);

    /**
     * Sets whether a character has completed the objective
     *
     * @param character the character
     * @param completed whether the character has completed the objective
     */
    public void setCompleted(Character character, boolean completed);

}
