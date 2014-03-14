package net.wayward_realms.waywardlib.combat;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.skills.Skill;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

/**
 * Represents a fight
 *
 */
public interface Fight {

    /**
     * Starts the fight, teleporting all players to where they need to be and establishing the turn order
     */
    public void start();

    /**
     * Ends the fight, returning each player to their original location
     */
    public void end();

    /**
     * Gets which character whose turn it is next
     *
     * @return the character who will next make a move
     */
    public Combatant getNextTurn();

    /**
     * Performs the next turn
     *
     * @param turn the turn to perform
     */
    public void doTurn(Turn turn);

    /**
     * Performs the next turn
     *
     * @param attacking the character making the attack
     * @param defending the character being attacked
     * @param weapon the weapon being used to attack
     * @param skill the skill being used
     */
    public void doTurn(Combatant attacking, Combatant defending, ItemStack weapon, Skill skill);

    /**
     * Gets the characters involved in this fight. Does not return other combatants, such as entities
     *
     * @return a collection containing all the characters involved
     * @see Fight#getCombatants
     */
    public Collection<? extends Character> getCharacters();

    /**
     * Gets the combatants involved in the fight
     *
     * @return a collection containing all the combatants involved
     */
    public Collection<? extends Combatant> getCombatants();

    /**
     * Adds a combatant to the fight. If the fight has started, it will also teleport them to the arena and add them to the turn order
     *
     * @param combatant the combatant to add
     */
    public void addCombatant(Combatant combatant);

    /**
     * Removes a combatant from the fight. If the fight has started, it will also teleport them back to their original location
     *
     * @param combatant the combatant to remove
     */
    public void removeCombatant(Combatant combatant);

    /**
     * Checks whether the fight is active
     *
     * @return whether the fight is active
     */
    public boolean isActive();

}
