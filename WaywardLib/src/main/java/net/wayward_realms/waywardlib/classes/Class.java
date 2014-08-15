package net.wayward_realms.waywardlib.classes;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.Map;

/**
 * Represents a combat classWaywardPlugin
 *
 */
public interface Class extends ConfigurationSerializable {

    /**
     * Gets the name of the class
     *
     * @return the name
     */
    public String getName();

    /**
     * Sets the name of the class
     *
     * @param name the name to set
     */
    public void setName(String name);

    /**
     * Checks whether a character has the prerequisites to use the class
     *
     * @param character the character
     * @return whether the character has the prerequisites
     */
    public boolean hasPrerequisites(net.wayward_realms.waywardlib.character.Character character);

    /**
     * Gets the requirements for the class
     *
     * @return a map of classes required to the levels they are required at
     */
    public Map<Class, Integer> getPrerequisites();

    /**
     * Adds a prerequisite to the class
     *
     * @param clazz the class to require
     * @param level the level to require the class at
     */
    public void addPrerequisite(Class clazz, int level);

    /**
     * Removes a prerequisite on a class
     *
     * @param clazz the class to remove
     */
    public void removePrerequisite(Class clazz);

    /**
     * Gets the HP bonus of the class
     *
     * @return the hp bonus
     */
    public double getHpBonus();

    /**
     * Sets the HP bonus of the class
     *
     * @param hpBonus the hp bonus to set
     */
    public void setHpBonus(double hpBonus);

    /**
     * Gets the bonus in a given stat
     *
     * @param stat the stat to get the bonus for
     * @return the bonus in the stat
     */
    public int getStatBonus(Stat stat);

    /**
     * Sets the bonus in a given stat
     *
     * @param stat the stat to set the bonus for
     * @param bonus the bonus to set
     */
    public void setStatBonus(Stat stat, int bonus);

    /**
     * Gets the mana bonus of the class
     *
     * @return the mana bonus
     */
    public int getManaBonus();

    /**
     * Sets the mana bonus of the class
     *
     * @param manaBonus the mana bonus to set
     */
    public void setManaBonus(int manaBonus);

    /**
     * Gets the max level you can reach in this class
     *
     * @return the max level
     */
    public int getMaxLevel();

}
