package net.wayward_realms.waywardlib.classes;

import net.wayward_realms.waywardlib.WaywardPlugin;
import net.wayward_realms.waywardlib.character.Character;
import org.bukkit.OfflinePlayer;

import java.util.Collection;

/**
 * Represents a classes plugin
 *
 ** @deprecated classes are going to be removed
 */
@Deprecated
public interface ClassesPlugin extends WaywardPlugin {

    /**
     * Gets a collection containing the classes
     *
     * @return a collection of all the classes
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public Collection<? extends Class> getClasses();

    /**
     * Adds a class
     *
     * @param clazz the class to add
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public void addClass(Class clazz);

    /**
     * Removes a class
     *
     * @param clazz the class to remove
     */
    public void removeClass(Class clazz);

    /**
     * Gets a player's active character's class
     *
     * @param player the player
     * @return the player's active character's class
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public Class getClass(OfflinePlayer player);

    /**
     * Sets the class of a player's active character
     *
     * @param player the player
     * @param clazz the class to set
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public void setClass(OfflinePlayer player, Class clazz);

    /**
     * Gets a class by name
     *
     * @param name the name of the class
     * @return the class
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public Class getClass(String name);

    /**
     * Gets the level of the player's active character in their current class
     *
     * @param player the player
     * @return the level of the player's current character in their current class
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public int getLevel(OfflinePlayer player);

    /**
     * Sets the level of the player's active character in their current class
     *
     * @param player the player
     * @param level the level to set
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public void setLevel(OfflinePlayer player, int level);

    /**
     * Gets the total experience a player's active character has on their current class
     *
     * @param player the player
     * @return the total experience of the player's active character in their current class
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public int getTotalExperience(OfflinePlayer player);

    /**
     * Sets the total experience a player's active character has on their current class
     *
     * @param player the player
     * @param amount the amount of experience to set
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public void setTotalExperience(OfflinePlayer player, int amount);

    /**
     * Gets the experience a player's active character has in their current class towards their next level
     *
     * @param player the player
     * @return the experience the player's active character has in their current class towards their next level
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public int getExperienceTowardsNextLevel(OfflinePlayer player);

    /**
     * Sets the experience a player's active character has in their current class towards their next level
     *
     * @param player the player
     * @param amount the amount of experience
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public void setExperienceTowardsNextLevel(OfflinePlayer player, int amount);

    /**
     * Gives the player's active character an amount of experience in their current class
     *
     * @param player the player
     * @param amount the amount of experience to give
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public void giveExperience(OfflinePlayer player, int amount);

    /**
     * Gets the level of a player's active character in a given class
     *
     * @param player the player
     * @param clazz the class
     * @return the player's level in the given class
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public int getLevel(OfflinePlayer player, Class clazz);

    /**
     * Sets the level of the player's active character in a given class
     *
     * @param player the player
     * @param clazz the class
     * @param level the level to set
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public void setLevel(OfflinePlayer player, Class clazz, int level);

    /**
     * Gets the total experience of a player's active character in a given class
     *
     * @param player the player
     * @param clazz the class
     * @return the total experience the player's active character has in the given class
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public int getTotalExperience(OfflinePlayer player, Class clazz);

    /**
     * Sets the total experience of a player's active character in a given class
     *
     * @param player the player
     * @param clazz the class
     * @param amount the amount of experience to set
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public void setTotalExperience(OfflinePlayer player, Class clazz, int amount);

    /**
     * Gets the experience towards the next level of a player's active character in a given class
     *
     * @param player the player
     * @param clazz the class
     * @return the experience of the player's active character in the given class
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public int getExperienceTowardsNextLevel(OfflinePlayer player, Class clazz);

    /**
     * Sets the experience towards the next level of a player's active character in a given class
     *
     * @param player the player
     * @param clazz the class
     * @param amount the amount of experience to set
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public void setExperienceTowardsNextLevel(OfflinePlayer player, Class clazz, int amount);

    /**
     * Gives the player's active character an amount of experience in a given class
     *
     * @param player the player
     * @param clazz the class
     * @param amount the amount of experience to give
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public void giveExperience(OfflinePlayer player, Class clazz, int amount);

    /**
     * Gets a character's class
     *
     * @param character the character
     * @return the character's class
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public Class getClass(net.wayward_realms.waywardlib.character.Character character);

    /**
     * Sets the class of a character
     *
     * @param character the character
     * @param clazz the class to set
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public void setClass(Character character, Class clazz);

    /**
     * Gets the level of a character in their current class
     *
     * @param character the character
     * @return the level of the character in their current class
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public int getLevel(Character character);

    /**
     * Sets the level of a character in their current class
     *
     * @param character the character
     * @param level the level to set
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public void setLevel(Character character, int level);

    /**
     * Gets the level of a character in a given class
     *
     * @param character the character
     * @param clazz the class
     * @return the player's level in the given class
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public int getLevel(Character character, Class clazz);

    /**
     * Sets the level of a character in a given class
     *
     * @param character the character
     * @param clazz the class
     * @param level the level to set
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public void setLevel(Character character, Class clazz, int level);

    /**
     * Gets the total experience a character has on their current class
     *
     * @param character the character
     * @return the total experience the character has in their current class
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public int getTotalExperience(Character character);

    /**
     * Sets the total experience of a character in their current class
     *
     * @param character the character
     * @param amount the amount of experience to set
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public void setTotalExperience(Character character, int amount);

    /**
     * Gets the total experience of a character in a given class
     *
     * @param character the character
     * @param clazz the class
     * @return the total experience the character has in the given class
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public int getTotalExperience(Character character, Class clazz);

    /**
     * Sets the total experience of a character in a given class
     *
     * @param character the character
     * @param clazz the class
     * @param amount the amount of experience to set
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public void setTotalExperience(Character character, Class clazz, int amount);

    /**
     * Gets the experience towards the next level of a character in their current class
     *
     * @param character the character
     * @return the experience of the character towards the next level in their current class
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public int getExperienceTowardsNextLevel(Character character);

    /**
     * Sets the experience towards the next level of a character in their current class
     *
     * @param character the character
     * @param amount the amount of experience to set
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public void setExperienceTowardsNextLevel(Character character, int amount);

    /**
     * Gets the experience towards the next level of a character in a given class
     *
     * @param character the character
     * @param clazz the class
     * @return the experience of the character towards the next level in the given class
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public int getExperienceTowardsNextLevel(Character character, Class clazz);

    /**
     * Sets the experience towards the next level of a character in a given class
     *
     * @param character the character
     * @param clazz the class
     * @param amount the amount of experience to set
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public void setExperienceTowardsNextLevel(Character character, Class clazz, int amount);

    /**
     * Gives a character an amount of experience in their current class
     *
     * @param character the character
     * @param amount the amount of experience to give
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public void giveExperience(Character character, int amount);

    /**
     * Gives a character an amount of experience in a given class
     *
     * @param character the character
     * @param clazz the class
     * @param amount the amount of experience to give
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public void giveExperience(Character character, Class clazz, int amount);

    /**
     * Gets the total experience required from level one to reach the given level
     *
     * @param level the level
     * @return the experience to get to the level
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public int getTotalExperienceForLevel(int level);

    /**
     * Gets the total amount of experience required to get to the next level, from the start of the given level
     *
     * @param level the current level
     * @return the experience required at this level to reach the next
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public int getExpToNextLevel(int level);

}
