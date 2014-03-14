package net.wayward_realms.waywardlib.character;

import net.wayward_realms.waywardlib.classes.SkillType;
import net.wayward_realms.waywardlib.classes.Stat;
import net.wayward_realms.waywardlib.combat.Combatant;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a character
 * 
 */
public interface Character extends Combatant, ConfigurationSerializable {

    /**
     * Gets the character's unique ID
     *
     * @return the ID
     */
    public int getId();

    /**
     * Gets the name of the character
     *
     * @return the character's name
     */
    public String getName();

    /**
     * Sets the name of the character
     *
     * @param name the name to set
     */
    public void setName(String name);

    /**
     * Gets the age of the character
     *
     * @return the character's age
     */
    public int getAge();

    /**
     * Sets the age of the character
     *
     * @param age the age to set
     */
    public void setAge(int age);

    /**
     * Gets the gender of the character
     *
     * @return the character's gender
     */
    public Gender getGender();

    /**
     * Sets the gender of the character
     *
     * @param gender the gender to set
     */
    public void setGender(Gender gender);

    /**
     * Gets the race of the character
     *
     * @return the character's race
     */
    public Race getRace();

    /**
     * Sets the race of the character
     *
     * @param race the race to set
     */
    public void setRace(Race race);

    /**
     * Gets the description of the character
     *
     * @return the character's description
     */
    public String getDescription();

    /**
     * Sets the description of the character
     *
     * @param info the info to set
     */
    public void setDescription(String info);

    /**
     * Adds to the description of the character
     *
     * @param info the info to add
     */
    public void addDescription(String info);

    /**
     * Gets the player currently playing this character
     *
     * @return the player currently playing this character, or null if this character is not being played
     */
    public OfflinePlayer getPlayer();

    /**
     * Sets the player currently playing this character
     *
     * @param player the player to set, or null if no player is playing this character
     */
    public void setPlayer(OfflinePlayer player);

    /**
     * Gets the health of the character
     *
     * @return the health
     */
    public double getHealth();

    /**
     * Sets the health of the character
     *
     * @param health the health to set
     */
    public void setHealth(double health);

    /**
     * Gets the food level of the character
     *
     * @return the food level
     */
    public int getFoodLevel();

    /**
     * Sets the food level of the character
     *
     * @param foodLevel the food level to set
     */
    public void setFoodLevel(int foodLevel);

    /**
     * Gets the thirst level of the character
     *
     * @return the thirst level
     */
    public int getThirst();

    /**
     * Sets the thirst level of the character
     *
     * @param thirstLevel the thirst level to set
     */
    public void setThirst(int thirstLevel);

    /**
     * Gets the max health of the character
     *
     * @return the max health
     */
    public double getMaxHealth();

    /**
     * Sets the max health of the character
     *
     * @param maxHealth the max health to set
     */
    public void setMaxHealth(double maxHealth);

    /**
     * Gets the character's mana
     *
     * @return the mana
     */
    public int getMana();

    /**
     * Sets the character's mana
     *
     * @param mana the mana to set
     */
    public void setMana(int mana);

    /**
     * Gets the character's max mana
     *
     * @return the max mana
     */
    public int getMaxMana();

    /**
     * Gets the location of the character
     *
     * @return the location of the character
     */
    public Location getLocation();

    /**
     * Sets the location of the character
     *
     * @param location the location to set
     */
    public void setLocation(Location location);

    /**
     * Gets the inventory contents of this character
     *
     * @return an array of itemstacks containing the inventory contents of this character
     */
    public ItemStack[] getInventoryContents();

    /**
     * Sets the inventory contents of this character
     *
     * @param contents the contents to set
     */
    public void setInventoryContents(ItemStack[] contents);

    /**
     * Gets whether the character is dead or not
     *
     * @return true if the character is dead, otherwise false
     */
    public boolean isDead();

    /**
     * Sets whether the character is dead or not
     *
     * @param dead whether the character should be dead or not. Use false for resurrections and true when the character is killed.
     */
    public void setDead(boolean dead);

    /**
     * Gets the value of a stat
     *
     * @param stat the stat to get the value of
     * @return the value of the stat
     */
    public int getStatValue(Stat stat);

    /**
     * Gets the amount of skill points the character has for a certain skill type
     *
     * @param type the type of skill
     * @return the amount of skill points the character has of the skill type
     */
    public int getSkillPoints(SkillType type);

}
