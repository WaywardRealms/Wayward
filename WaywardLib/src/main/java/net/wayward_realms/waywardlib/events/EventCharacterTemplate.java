package net.wayward_realms.waywardlib.events;

import net.wayward_realms.waywardlib.character.Gender;
import net.wayward_realms.waywardlib.character.Race;
import net.wayward_realms.waywardlib.classes.Stat;
import net.wayward_realms.waywardlib.skills.SkillType;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

public interface EventCharacterTemplate {

    /**
     * Gets the player who created the template
     *
     * @return the player who created the template
     */
    public OfflinePlayer getCreator();

    /**
     * Sets the player who created the template
     *
     * @param creator the player who created the template
     */
    public void setCreator(OfflinePlayer creator);

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
     * Gets the age
     *
     * @return the age
     */
    public int getAge();

    /**
     * Sets the age
     *
     * @param age the age to set
     */
    public void setAge(int age);

    /**
     * Gets the gender
     *
     * @return the gender
     */
    public Gender getGender();

    /**
     * Sets the gender
     *
     * @param gender the gender to set
     */
    public void setGender(Gender gender);

    /**
     * Gets the race
     *
     * @return the race
     */
    public Race getRace();

    /**
     * Sets the race
     *
     * @param race the race to set
     */
    public void setRace(Race race);

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
     * Adds info to the description
     *
     * @param info the info to add
     */
    public void addDescription(String info);

    /**
     * Gets the max health
     *
     * @return the max health
     */
    public double getMaxHealth();

    /**
     * Sets the max health
     *
     * @param maxHealth the max health to set
     */
    public void setMaxHealth(double maxHealth);

    /**
     * Gets the max mana
     *
     * @return the max mana
     */
    public int getMaxMana();

    /**
     * Sets the max mana
     *
     * @param maxMana the max mana to set
     */
    public void setMaxMana(int maxMana);

    /**
     * Gets the helmet
     *
     * @return the helmet
     */
    public ItemStack getHelmet();

    /**
     * Sets the helmet
     *
     * @param helmet the helmet to set
     */
    public void setHelmet(ItemStack helmet);

    /**
     * Gets the chestplate
     *
     * @return the chestplate
     */
    public ItemStack getChestplate();

    /**
     * Sets the chestplate
     *
     * @param chestplate the chestplate to set
     */
    public void setChestplate(ItemStack chestplate);

    /**
     * Gets the leggings
     *
     * @return the leggings
     */
    public ItemStack getLeggings();

    /**
     * Sets the leggings
     *
     * @param leggings the leggings to set
     */
    public void setLeggings(ItemStack leggings);

    /**
     * Gets the boots
     *
     * @return the boots
     */
    public ItemStack getBoots();

    /**
     * Sets the boots
     *
     * @param boots the boots to set
     */
    public void setBoots(ItemStack boots);

    /**
     * Gets the inventory contents
     *
     * @return the inventory contents
     */
    public ItemStack[] getInventoryContents();

    /**
     * Sets the inventory contents
     *
     * @param contents the inventory contents to set
     */
    public void setInventoryContents(ItemStack[] contents);

    /**
     * Gets a stat value
     *
     * @param stat the stat
     * @return the stat's value
     */
    public int getStatValue(Stat stat);

    /**
     * Sets a stat value
     *
     * @param stat the stat
     * @param value the value to set
     */
    public void setStatValue(Stat stat, int value);

    /**
     * Gets the skill points of a given type
     *
     * @param type the type
     * @return the amount of skill points
     */
    public int getSkillPoints(SkillType type);

    /**
     * Sets the skill points of a given type
     *
     * @param type the type
     * @param skillPoints the amount of skill points to set
     */
    public void setSkillPoints(SkillType type, int skillPoints);

}
