package net.wayward_realms.waywardlib.skills;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

/**
 * Represents a skill
 */
public interface Skill extends ConfigurationSerializable {

    /**
     * Uses the skill
     *
     * @param player the player using the skill
     * @return whether successful
     */
    public boolean use(Player player);

    /**
     * Checks whether a character can use a skill
     *
     * @param character the character
     * @return whether the character can use the skill
     */
    public boolean canUse(net.wayward_realms.waywardlib.character.Character character);

    /**
     * Gets the name of the skill
     *
     * @return the name of the skill
     */
    public String getName();

    /**
     * Sets the name of the skill
     *
     * @param name the name to set
     */
    public void setName(String name);

    /**
     * Gets the cooldown for the skill
     *
     * @return the cooldown for the skill, in seconds
     */
    public int getCoolDown();

    /**
     * Sets the cooldown for the skill
     *
     * @param coolDown the cooldown to set, in seconds
     */
    public void setCoolDown(int coolDown);

}
