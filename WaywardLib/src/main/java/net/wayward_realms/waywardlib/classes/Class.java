package net.wayward_realms.waywardlib.classes;

import net.wayward_realms.waywardlib.skills.SkillType;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.Map;

/**
 * Represents a combat class
 * @author Lucariatias
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
     * Gets the melee attack bonus of the class
     *
     * @return the melee attack bonus
     * @deprecated Obsolete
     * @see #getStatBonus(Stat)
     */
    @Deprecated
    public int getMeleeAttackBonus();

    /**
     * Sets the melee attack bonus of the class
     *
     * @param attackBonus the melee attack bonus to set
     * @deprecated Obsolete
     * @see #setStatBonus(Stat, int)
     */
    @Deprecated
    public void setMeleeAttackBonus(int attackBonus);

    /**
     * Gets the ranged attack bonus of the class
     *
     * @return the ranged attack bonus
     * @deprecated Obsolete
     * @see #getStatBonus(Stat)
     */
    @Deprecated
    public int getRangedAttackBonus();

    /**
     * Sets the ranged attack bonus of the class
     *
     * @param attackBonus the ranged attack bonus to set
     * @deprecated Obsolete
     * @see #setStatBonus(Stat, int)
     */
    @Deprecated
    public void setRangedAttackBonus(int attackBonus);

    /**
     * Gets the magic attack bonus of the class
     *
     * @return the magic attack bonus
     * @deprecated Obsolete
     * @see #getStatBonus(Stat)
     */
    @Deprecated
    public int getMagicAttackBonus();

    /**
     * Sets the magic attack bonus of the class
     *
     * @param attackBonus the magic attack bonus to set
     * @deprecated Obsolete
     * @see #setStatBonus(Stat, int)
     */
    @Deprecated
    public void setMagicAttackBonus(int attackBonus);

    /**
     * Gets the melee defence bonus of the class
     *
     * @return the melee defence bonus
     * @deprecated Obsolete
     * @see #getStatBonus(Stat)
     */
    @Deprecated
    public int getMeleeDefenceBonus();

    /**
     * Sets the melee defence bonus of the class
     *
     * @param defenceBonus the melee defence bonus to set
     * @deprecated Obsolete
     * @see #setStatBonus(Stat, int)
     */
    @Deprecated
    public void setMeleeDefenceBonus(int defenceBonus);

    /**
     * Gets the ranged defence bonus of the class
     *
     * @return the ranged defence bonus
     * @deprecated Obsolete
     * @see #getStatBonus(Stat)
     */
    @Deprecated
    public int getRangedDefenceBonus();

    /**
     * Sets the ranged defence bonus of the class
     *
     * @param defenceBonus the ranged defence bonus to set
     * @deprecated Obsolete
     * @see #setStatBonus(Stat, int)
     */
    @Deprecated
    public void setRangedDefenceBonus(int defenceBonus);

    /**
     * Gets the magic defence bonus of the class
     *
     * @return the magic defence bonus
     * @deprecated Obsolete
     * @see #getStatBonus(Stat)
     */
    @Deprecated
    public int getMagicDefenceBonus();

    /**
     * Sets the magic defence bonus of the class
     *
     * @param defenceBonus the magic defence bonus to set
     * @deprecated Obsolete
     * @see #setStatBonus(Stat, int)
     */
    @Deprecated
    public void setMagicDefenceBonus(int defenceBonus);

    /**
     * Gets the speed bonus of the class
     *
     * @return the speed bonus
     * @deprecated Obsolete
     * @see #getStatBonus(Stat)
     */
    @Deprecated
    public int getSpeedBonus();

    /**
     * Sets the speed bonus of the class
     *
     * @param speedBonus the speed bonus to set
     * @deprecated Obsolete
     * @see #setStatBonus(Stat, int)
     */
    @Deprecated
    public void setSpeedBonus(int speedBonus);

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
     * Gets the skill point bonus for a skill type
     *
     * @param type the type of skill
     * @return the skill point bonus
     */
    public int getSkillPointBonus(SkillType type);

    /**
     * Sets the skill point bonus for a given skill type
     *
     * @param type the type of skill
     * @param bonus the bonus to set
     */
    public void setSkillPointBonus(SkillType type, int bonus);

    /**
     * Gets the max level you can reach in this class
     *
     * @return the max level
     */
    public int getMaxLevel();

}
