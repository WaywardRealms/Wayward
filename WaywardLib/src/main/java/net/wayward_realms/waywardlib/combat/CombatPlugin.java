package net.wayward_realms.waywardlib.combat;

import net.wayward_realms.waywardlib.WaywardPlugin;

import java.io.File;
import java.util.Collection;

/**
 * Represents a combat pluginWaywardPlugin
 *
 */
public interface CombatPlugin extends WaywardPlugin {

    /**
     * Gets a collection containing any fights currently taking place
     *
     * @return the fights currently taking place
     */
    public Collection<? extends Fight> getActiveFights();

    /**
     * Adds a fight
     *
     * @param fight the fight to add
     */
    public void addFight(Fight fight);

    /**
     * Removes a fight
     *
     * @param fight the fight to remove
     */
    public void removeFight(Fight fight);

    /**
     * Gets the fight a combatant is currently participating in
     *
     * @param combatant the combatant
     * @return the fight they are currently participating, or null if they are not participating in a fight
     */
    public Fight getActiveFight(Combatant combatant);

    /**
     * Gets a collection of all attacks currently available
     *
     * @return a collection of attacks
     */
    public Collection<? extends Attack> getAttacks();

    /**
     * Gets an attack by name
     *
     * @param name the name of the attack
     * @return the attack with the given name
     */
    public Attack getAttack(String name);

    /**
     * Adds an attack
     *
     * @param attack the attack to add
     */
    public void addAttack(Attack attack);

    /**
     * Removes an attack
     *
     * @param attack the attack to remove
     */
    public void removeAttack(Attack attack);

    /**
     * Loads an attack from the given file
     *
     * @param file the file to load from
     * @return the attack
     */
    public Attack loadAttack(File file);

    /**
     * Loads an attack from the given class
     *
     * @param clazz the class to load from
     * @return the attack
     */
    public Attack loadAttack(Class<? extends Attack> clazz);

}
