package net.wayward_realms.waywardlib.classes;

import net.wayward_realms.waywardlib.skills.Stat;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.Map;

/**
 * Represents a combat class
 *
 * @deprecated classes are going to be removed
 */
@Deprecated
public interface Class extends ConfigurationSerializable {

    /**
     * Gets the name of the class
     *
     * @return the name
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public String getName();

    /**
     * Sets the name of the class
     *
     * @param name the name to set
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public void setName(String name);

    /**
     * Checks whether a character has the prerequisites to use the class
     *
     * @param character the character
     * @return whether the character has the prerequisites
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public boolean hasPrerequisites(net.wayward_realms.waywardlib.character.Character character);

    /**
     * Gets the requirements for the class
     *
     * @return a map of classes required to the levels they are required at
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public Map<Class, Integer> getPrerequisites();

    /**
     * Adds a prerequisite to the class
     *
     * @param clazz the class to require
     * @param level the level to require the class at
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public void addPrerequisite(Class clazz, int level);

    /**
     * Removes a prerequisite on a class
     *
     * @param clazz the class to remove
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public void removePrerequisite(Class clazz);

    /**
     * Gets the HP bonus of the class
     *
     * @return the hp bonus
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public double getHpBonus();

    /**
     * Sets the HP bonus of the class
     *
     * @param hpBonus the hp bonus to set
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public void setHpBonus(double hpBonus);

    /**
     * Gets the bonus in a given stat
     *
     * @param stat the stat to get the bonus for
     * @return the bonus in the stat
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public int getStatBonus(Stat stat);

    /**
     * Sets the bonus in a given stat
     *
     * @param stat the stat to set the bonus for
     * @param bonus the bonus to set
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public void setStatBonus(Stat stat, int bonus);

    /**
     * Gets the mana bonus of the class
     *
     * @return the mana bonus
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public int getManaBonus();

    /**
     * Sets the mana bonus of the class
     *
     * @param manaBonus the mana bonus to set
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public void setManaBonus(int manaBonus);

    /**
     * Gets the max level you can reach in this class
     *
     * @return the max level
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public int getMaxLevel();

}
