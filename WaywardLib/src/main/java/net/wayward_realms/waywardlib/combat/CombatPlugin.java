package net.wayward_realms.waywardlib.combat;

import net.wayward_realms.waywardlib.WaywardPlugin;

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

}
