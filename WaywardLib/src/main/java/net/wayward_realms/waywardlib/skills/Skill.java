package net.wayward_realms.waywardlib.skills;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.combat.Combatant;
import net.wayward_realms.waywardlib.combat.Fight;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a skill
 */
public interface Skill extends ConfigurationSerializable {

    /**
     * Uses the skill in a PvE situation
     *
     * @param player the player using the skill
     * @return whether successful
     */
    public boolean use(Player player);

    /**
     * Uses the skill in a fight situation
     *
     * @param fight the fight to use the attack in
     * @param attacking the character making the attack
     * @param defending the character defending against the attack
     * @param weapon the weapon being used to attack
     * @return whether the attack hits
     */
    public boolean use(Fight fight, Combatant attacking, Combatant defending, ItemStack weapon);

    /**
     * Gets the itemstack to be used as the icon in menus
     *
     * @return an itemstack that can be used to represent the attack in menus
     */
    public ItemStack getIcon();

    /**
     * Checks whether a character can use the skill
     *
     * @param character the character
     * @return whether the character can use the skill
     */
    public boolean canUse(Character character);

    /**
     * Checks whether a combatant can use the skill
     *
     * @param combatant the combatant
     * @return whether the combatant can use the skill
     */
    public boolean canUse(Combatant combatant);

    /**
     * Checks whether a player's active character can use the skill
     *
     * @param player the player
     * @return whether the player's active character can use the skill
     */
    public boolean canUse(OfflinePlayer player);

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
     * Gets the type of skill
     *
     * @return the type
     */
    public SkillType getType();

    /**
     * Sets the type of skill
     *
     * @param type the type to set
     */
    public void setType(SkillType type);

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
