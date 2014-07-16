package net.wayward_realms.waywardlib.events;

import net.wayward_realms.waywardlib.character.Character;

import java.util.Collection;

/**
 * Represents a quest
 */
public interface Quest {

    /**
     * Gets the name
     *
     * @return the name
     */
    public String getName();

    /**
     * Sets the name
     *
     * @param name the name to set
     */
    public void setName(String name);

    /**
     * Gets the description
     *
     * @return the description
     */
    public String getDescription();

    /**
     * Sets the description
     *
     * @param description the description to set
     */
    public void setDescription(String description);

    /**
     * Gets a collection of characters currently on the quest
     *
     * @return a {@link java.util.Collection} containing all characters on the quest
     */
    public Collection<? extends Character> getCharacters();

    /**
     * Adds a character
     *
     * @param character the character to add
     */
    public void addCharacter(Character character);

    /**
     * Removes a character
     *
     * @param character the character to remove
     */
    public void removeCharacter(Character character);

    /**
     * Gives the quest's reward to a character
     *
     * @param character the character to reward
     */
    public void giveReward(Character character);

    /**
     * Gets whether a character has completed the quest
     *
     * @param character the character
     * @return whether the character has completed the quest
     */
    public boolean hasCompleted(Character character);

    /**
     * Sets whether the character has completed the quest
     *
     * @param character the character
     * @param completed whether the character has completed the quest
     */
    public void setCompleted(Character character, boolean completed);

    /**
     * Gets the quest's objectives
     *
     * @return a {@link java.util.Collection} of the {@link net.wayward_realms.waywardlib.events.QuestObjective}s required in order to complete the quest
     */
    public Collection<? extends QuestObjective> getObjectives();

    /**
     * Adds an objective to the quest
     *
     * @param objective the objective to add
     */
    public void addObjective(QuestObjective objective);

    /**
     * Removes an objective from the quest
     *
     * @param objective the objective to remove
     */
    public void removeObjective(QuestObjective objective);

}
