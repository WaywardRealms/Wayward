package net.wayward_realms.waywardlib.events;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.classes.Stat;
import net.wayward_realms.waywardlib.skills.SkillType;

/**
 * Represents a character used in events
 */
public interface EventCharacter extends Character {

    /**
     * Sets the value of a stat
     *
     * @param stat the stat
     * @param value the value to set
     */
    public void setStatValue(Stat stat, int value);

    /**
     * Sets the amount of skill points for a skill type
     *
     * @param skillType the skill type
     * @param points the amount of points to set
     */
    public void setSkillPoints(SkillType skillType, int points);

    /**
     * Sets the max health of the character
     *
     * @param maxHealth the max health to set
     */
    public void setMaxHealth(double maxHealth);

    /**
     * Sets the max mana of the character
     *
     * @param maxMana the max mana to set
     */
    public void setMaxMana(int maxMana);

    /**
     * Creates a template from this event character
     */
    public EventCharacterTemplate createTemplate();

    /**
     * Assigns fields based on the template
     *
     * @param template the template to assign
     */
    public void assignTemplate(EventCharacterTemplate template);

}
