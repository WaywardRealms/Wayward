package net.wayward_realms.waywardlib.combat;

import net.wayward_realms.waywardlib.classes.Class;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

/**
 * Represents an attack
 * @author Lucariatias
 *
 */
public interface Attack {

    /**
     * Gets the name of the attack
     *
     * @return the name
     */
    public String getName();

    /**
     * Uses the attack, playing any animations and applying damage if it hits
     *
     * @param fight the fight to use the attack in
     * @param attacking the character making the attack
     * @param defending the character defending against the attack
     * @param weapon the weapon being used to attack
     * @return whether the attack hits
     */
    public boolean use(Fight fight, Combatant attacking, Combatant defending, ItemStack weapon);

    /**
     * Gets the type of attack
     *
     * @return the attack type
     */
    public AttackType getType();

    /**
     * Gets the itemstack to be used as the icon in menus
     *
     * @return an itemstack that can be used to represent the attack in menus
     */
    public ItemStack getIcon();

    /**
     * Checks whether someone of a given class and level can use the attack
     *
     * @param clazz the class
     * @param level the level
     * @return whether the attack is usable with the given class and level
     */
    public boolean canUse(Class clazz, int level);

    /**
     * Checks whether a combatant can use the attack
     *
     * @param combatant the combatant
     * @return whether the attack is usable by the given combatant
     */
    public boolean canUse(Combatant combatant);

    /**
     * Checks whether a player's active character can use the attack
     *
     * @param player the player
     * @return whether the attack is usable by the player's active character
     */
    public boolean canUse(OfflinePlayer player);

}
