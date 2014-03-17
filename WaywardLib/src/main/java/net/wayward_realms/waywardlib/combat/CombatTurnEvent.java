package net.wayward_realms.waywardlib.combat;

import net.wayward_realms.waywardlib.skills.Skill;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * Called when a character is using a skill
 *
 */
public class CombatTurnEvent extends CombatEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Fight fight;
    private Combatant attacking;
    private Combatant defending;
    private Skill skill;
    private ItemStack weapon;
    private boolean cancel;

    public CombatTurnEvent(final Fight fight, final Combatant attacking, Combatant defending, Skill skill, ItemStack weapon) {
        super();
        this.fight = fight;
        this.attacking = attacking;
        this.defending = defending;
        this.skill = skill;
        this.weapon = weapon;
    }

    public CombatTurnEvent(final Turn turn) {
        super();
        this.fight = turn.getFight();
        this.attacking = turn.getAttacker();
        this.defending = turn.getDefender();
        this.skill = turn.getSkill();
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
     * Gets the character carrying out the skill
     *
     * @return the attacker
     */
    public Combatant getAttacker() {
        return attacking;
    }

    /**
     * Gets the character defending against the skill
     *
     * @return the defender
     */
    public Combatant getDefender() {
        return defending;
    }

    /**
     * Gets the skill being used
     *
     * @return the skill being used
     */
    public Skill getSkill() {
        return skill;
    }

    /**
     * Sets the skill being used
     *
     * @param skill the skill to set
     */
    public void setSkill(Skill skill) {
        this.skill = skill;
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
