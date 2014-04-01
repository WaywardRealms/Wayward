package net.wayward_realms.waywardlib.skills;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.classes.ClassesPlugin;
import net.wayward_realms.waywardlib.classes.Stat;
import net.wayward_realms.waywardlib.combat.Combatant;
import net.wayward_realms.waywardlib.combat.Fight;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.Random;

public abstract class AttackSpellBase extends SpellBase {

    private int power;
    private Stat attackStat;
    private Stat defenceStat;
    private int criticalChance = 5;
    private int hitChance = 90;

    @Override
    public boolean use(Fight fight, net.wayward_realms.waywardlib.character.Character attacking, Character defending, ItemStack weapon) {
        Player defendingPlayer = defending.getPlayer().getPlayer();
        Random random = new Random();
        if (random.nextInt(100) < getHitChance()) {
            if (attacking.getMana() > getManaCost()) {
                animate(fight, attacking, defending, weapon);
                int attackerLevel = 0;
                RegisteredServiceProvider<ClassesPlugin> classesPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(ClassesPlugin.class);
                if (classesPluginProvider != null) {
                    ClassesPlugin classesPlugin = classesPluginProvider.getProvider();
                    attackerLevel = classesPlugin.getLevel(attacking);
                }
                int attack = attacking.getStatValue(getAttackStat());
                int defence = defending.getStatValue(getDefenceStat());
                double a = (2D * (double) attackerLevel + 10D) / 250D;
                double b = (double) attack / Math.max((double) defence, 1D);
                double power = getPower();
                double weaponModifier = getWeaponModifier(weapon);
                double armourModifier = getArmourModifier(defendingPlayer.getInventory().getHelmet(), defendingPlayer.getInventory().getChestplate(), defendingPlayer.getInventory().getLeggings(), defendingPlayer.getInventory().getBoots());
                boolean critical = random.nextInt(100) < getCriticalChance();
                double modifier = (critical ? 2D : 1D) * weaponModifier * armourModifier * (((double) random.nextInt(15) + 85D) / 100D);
                int damage = (int) Math.round((a * b * power) + 2D * modifier);
                defending.setHealth(defending.getHealth() - damage);
                defending.getPlayer().getPlayer().setHealth(Math.max(defending.getHealth(), 0D));
                if (critical) {
                    fight.sendMessage(ChatColor.YELLOW + "Critical hit!");
                }
                fight.sendMessage(getFightUseMessage(attacking, defending, Math.round(damage * 100D) / 100D));
                return true;
            } else {
                fight.sendMessage(getFightFailManaMessage(attacking, defending));
            }
        }
        return false;
    }

    public void animate(Fight fight, Combatant attacking, Combatant defending, ItemStack weapon) {
        if (attacking instanceof Character && defending instanceof Character) animate(fight, (Character) attacking, (Character) defending, weapon);
    }

    public abstract void animate(Fight fight, Character attacking, Character defending, ItemStack weapon);

    public abstract double getWeaponModifier(ItemStack weapon);

    public double getArmourModifier(ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots) {
        double armourModifier = 1D;
        switch (helmet.getType()) {
            case LEATHER_HELMET: armourModifier -= 0.05D; break;
            case GOLD_HELMET:case CHAINMAIL_HELMET:case IRON_HELMET: armourModifier -= 0.1D; break;
            case DIAMOND_HELMET: armourModifier -= 0.15D; break;
        }
        switch (chestplate.getType()) {
            case LEATHER_CHESTPLATE: armourModifier -= 0.15D; break;
            case GOLD_CHESTPLATE: armourModifier -= 0.25D; break;
            case CHAINMAIL_CHESTPLATE: armourModifier -= 0.25D; break;
            case IRON_CHESTPLATE: armourModifier -= 0.3D; break;
            case DIAMOND_CHESTPLATE: armourModifier -= 0.4D; break;
        }
        switch (leggings.getType()) {
            case LEATHER_LEGGINGS: armourModifier -= 0.1D; break;
            case GOLD_LEGGINGS: armourModifier -= 0.15D; break;
            case CHAINMAIL_LEGGINGS: armourModifier -= 0.2D; break;
            case IRON_LEGGINGS: armourModifier -= 0.25D; break;
            case DIAMOND_LEGGINGS: armourModifier -= 0.3D; break;
        }
        switch (boots.getType()) {
            case LEATHER_BOOTS:case GOLD_BOOTS:case CHAINMAIL_BOOTS: armourModifier -= 0.05D; break;
            case IRON_BOOTS: armourModifier -= 0.1D; break;
            case DIAMOND_BOOTS: armourModifier -= 0.15D; break;
        }
        return Math.max(armourModifier, 0.01D);
    }

    public double getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public Stat getAttackStat() {
        return attackStat;
    }

    public void setAttackStat(Stat attackStat) {
        this.attackStat = attackStat;
    }

    public Stat getDefenceStat() {
        return defenceStat;
    }

    public void setDefenceStat(Stat defenceStat) {
        this.defenceStat = defenceStat;
    }

    public int getCriticalChance() {
        return criticalChance;
    }

    public void setCriticalChance(int criticalChance) {
        this.criticalChance = criticalChance;
    }

    public abstract String getFightUseMessage(Character attacking, Character defending, double damage);

    public abstract String getFightFailManaMessage(Character attacking, Character defending);

    public int getHitChance() {
        return hitChance;
    }

    public void setHitChance(int hitChance) {
        this.hitChance = hitChance;
    }

}
