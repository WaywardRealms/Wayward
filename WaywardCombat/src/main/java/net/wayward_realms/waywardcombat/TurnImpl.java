package net.wayward_realms.waywardcombat;

import net.wayward_realms.waywardlib.combat.Combatant;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.combat.Turn;
import net.wayward_realms.waywardlib.skills.Skill;
import org.bukkit.inventory.ItemStack;

public class TurnImpl implements Turn {

    private Fight fight;
    private Combatant attacking;
    private Combatant defending;
    private ItemStack weapon;
    private Skill skill;

    public TurnImpl(Fight fight) {
        this.fight = fight;
    }

    @Override
    public Fight getFight() {
        return fight;
    }

    @Override
    public Combatant getAttacker() {
        return attacking;
    }

    @Override
    public void setAttacker(Combatant attacker) {
        this.attacking = attacker;
    }

    @Override
    public Combatant getDefender() {
        return defending;
    }

    @Override
    public void setDefender(Combatant defender) {
        this.defending = defender;
    }

    @Override
    public ItemStack getWeapon() {
        return weapon;
    }

    @Override
    public void setWeapon(ItemStack weapon) {
        this.weapon = weapon;
    }

    @Override
    public Skill getSkill() {
        return skill;
    }

    @Override
    public void setSkill(Skill skill) {
        this.skill = skill;
    }

}
