package net.wayward_realms.waywardlib.character;

import net.wayward_realms.waywardlib.classes.Stat;
import net.wayward_realms.waywardlib.combat.Combatant;
import net.wayward_realms.waywardlib.skills.SkillType;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

/**
 * Represents a character
 * 
 */
public interface Character extends Combatant {

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
     * Checks whether name is hidden on character cards
     *
     * @return whether the name is hidden
     */
    public boolean isNameHidden();

    /**
     * Sets whether to hide the name on character cards
     *
     * @param hidden whether to hide the name
     */
    public void setNameHidden(boolean hidden);

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
     * Checks whether the age is hidden on character cards
     *
     * @return whether the age is hidden
     */
    public boolean isAgeHidden();

    /**
     * Sets whether the age is hidden
     *
     * @param hidden whether to hide the age
     */
    public void setAgeHidden(boolean hidden);

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
     * Checks whether the gender is hidden
     *
     * @return whether the gender is hidden
     */
    public boolean isGenderHidden();

    /**
     * Sets whether the gender is hidden
     *
     * @param hidden whether to hide the gender
     */
    public void setGenderHidden(boolean hidden);

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
     * Checks whether the race is hidden
     *
     * @return whether the race is hidden
     */
    public boolean isRaceHidden();

    /**
     * Sets whether the race is hidden
     *
     * @param hidden whether to hide the race
     */
    public void setRaceHidden(boolean hidden);

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
     * Checks whether the description is hidden
     *
     * @return whether the description is hidden
     */
    public boolean isDescriptionHidden();

    /**
     * Sets whether the description is hidden
     *
     * @param hidden whether to hide the description
     */
    public void setDescriptionHidden(boolean hidden);

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
     * Gets the helmet the character is wearing
     *
     * @return the helmet
     */
    public ItemStack getHelmet();

    /**
     * Sets the helmet the character is wearing
     *
     * @param helmet the helmet to set
     */
    public void setHelmet(ItemStack helmet);

    /**
     * Gets the chestplate the character is wearing
     *
     * @return the chestplate
     */
    public ItemStack getChestplate();

    /**
     * Sets the chestplate the character is wearing
     *
     * @param chestplate the chestplate to set
     */
    public void setChestplate(ItemStack chestplate);

    /**
     * Gets the leggings the character is wearing
     *
     * @return the leggings
     */
    public ItemStack getLeggings();

    /**
     * Sets the leggings the character is wearing
     *
     * @param leggings the leggings to set
     */
    public void setLeggings(ItemStack leggings);

    /**
     * Gets the boots the character is wearing
     *
     * @return the boots
     */
    public ItemStack getBoots();

    /**
     * Sets the boots the character is wearing
     *
     * @param boots the boots to set
     */
    public void setBoots(ItemStack boots);

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
     * Gets the temporary stat modifications currently applying to the character
     *
     * @return the temporary stat modifications
     */
    public Collection<TemporaryStatModification> getTemporaryStatModifications();

    /**
     * Adds a temporary stat modification to the character
     *
     * @param modification the modification to add
     */
    public void addTemporaryStatModification(TemporaryStatModification modification);

    /**
     * Removes a temporary stat modification from a character
     *
     * @param modification the modification to remove
     */
    public void removeTemporaryStatModification(TemporaryStatModification modification);

    /**
     * Gets the amount of skill points the character has for a certain skill type
     *
     * @param type the type of skill
     * @return the amount of skill points the character has of the skill type
     */
    public int getSkillPoints(SkillType type);

    /**
     * Checks whether the class is hidden
     *
     * @return whether the class is hidden
     */
    public boolean isClassHidden();

    /**
     * Sets whether to hide the class
     *
     * @param hidden whether to hide the class
     */
    public void setClassHidden(boolean hidden);

    /**
     * Gets what to use for the name plate
     *
     * @return the name plate
     */
    public String getNamePlate();

    /**
     * Sets what to use for the name plate
     *
     * @param namePlate the name plate to set
     */
    public void setNamePlate(String namePlate);

}
