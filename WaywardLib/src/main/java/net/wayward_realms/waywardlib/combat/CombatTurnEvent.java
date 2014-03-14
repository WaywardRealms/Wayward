package net.wayward_realms.waywardlib.combat;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * Called when a character is making an attack
 * @author Lucariatias
 *
 */
public class CombatTurnEvent extends CombatEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Fight fight;
    private Combatant attacking;
    private Combatant defending;
    private Attack attack;
    private ItemStack weapon;
    private boolean cancel;

    public CombatTurnEvent(final Fight fight, final Combatant attacking, Combatant defending, Attack attack, ItemStack weapon) {
        super();
        this.fight = fight;
        this.attacking = attacking;
        this.defending = defending;
        this.attack = attack;
        this.weapon = weapon;
    }

    public CombatTurnEvent(final Turn turn) {
        super();
        this.fight = turn.getFight();
        this.attacking = turn.getAttacker();
        this.defending = turn.getDefender();
        this.attack = turn.getAttack();
        this.weapon = turn.getWeapon();
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Gets the fight
     *
     * @return the fight
     */
    public Fight getFight() {
        return fight;
    }

    /**
     * Gets the character carrying out the attack
     *
     * @return the attacker
     */
    public Combatant getAttacker() {
        return attacking;
    }

    /**
     * Gets the character defending against the attack
     *
     * @return the defender
     */
    public Combatant getDefender() {
        return defending;
    }

    /**
     * Gets the attack being used
     *
     * @return the attack being used
     */
    public Attack getAttack() {
        return attack;
    }

    /**
     * Sets the attack being used
     *
     * @param attack the attack to set
     */
    public void setAttack(Attack attack) {
        this.attack = attack;
    }

    /**
     * Gets the weapon being used
     *
     * @return the weapon
     */
    public ItemStack getWeapon() {
        return weapon;
    }

    /**
     * Sets the weapon being used
     *
     * @param weapon the weapon to set
     */
    public void setWeapon(ItemStack weapon) {
        this.weapon = weapon;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

}
