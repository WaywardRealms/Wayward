package net.wayward_realms.waywardlib.skills;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.classes.ClassesPlugin;
import net.wayward_realms.waywardlib.classes.Stat;
import net.wayward_realms.waywardlib.combat.Combatant;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.combat.StatusEffect;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

public abstract class AttackSkillBase extends SkillBase {

    private double power;
    private Stat attackStat;
    private Stat defenceStat;
    private int criticalChance = 5;
    private double criticalMultiplier = 2D;
    private int hitChance = 90;

    @Override
    public boolean use(Fight fight, Character attacking, Character defending, ItemStack weapon) {
        Player defendingPlayer = defending.getPlayer().getPlayer();
        Random random = new Random();
        if (random.nextInt(100) < getHitChance()) {
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
            boolean critical = random.nextInt(100) + 1 < getCriticalChance();
            double modifier = (critical ? getCriticalMultiplier() : 1D) * weaponModifier * armourModifier * (((double) random.nextInt(15) + 85D) / 100D);
            int damage = (int) Math.round((a * b * power) + 2D * modifier);
            defending.setHealth(defending.getHealth() - damage);
            defending.getPlayer().getPlayer().setHealth(Math.max(defending.getHealth(), 0D));
            if (critical) {
                fight.sendMessage(ChatColor.YELLOW + "Critical hit!");
            }
            fight.sendMessage(ChatColor.YELLOW + getFightUseMessage(attacking, defending, Math.round(damage * 100D) / 100D));
            for (Map.Entry<StatusEffect, Integer> entry : getStatusEffects().entrySet()) {
                if (random.nextInt(100) + 1 < getStatusEffectChance(entry.getKey())) {
                    fight.setStatusTurns(defending, entry.getKey(), entry.getValue());
                    switch (entry.getKey()) {
                        case POISON:
                            fight.sendMessage(ChatColor.DARK_PURPLE + defending.getName() + " was poisoned for " + entry.getValue() + " turns");
                            break;
                        case PARALYSIS:
                            fight.sendMessage(ChatColor.GOLD + defending.getName() + " was paralysed for " + entry.getValue() + " turns");
                            break;
                        case BURNED:
                            fight.sendMessage(ChatColor.DARK_RED + defending.getName() + " was burned for " + entry.getValue() + " turns");
                            break;
                        case FROZEN:
                            fight.sendMessage(ChatColor.AQUA + defending.getName() + " was frozen for " + entry.getValue() + " turns");
                            break;
                        case CONFUSED:
                            fight.sendMessage(ChatColor.YELLOW + defending.getName() + " was confused for " + entry.getValue() + " turns");
                            break;
                        case ASLEEP:
                            fight.sendMessage(ChatColor.GRAY + defending.getName() + " was sent to sleep for " + entry.getValue() + " turns");
                            break;
                        case BLIND:
                            fight.sendMessage(ChatColor.DARK_PURPLE + defending.getName() + " was blinded for " + entry.getValue() + " turns");
                            break;
                        case DOOM:
                            fight.sendMessage(ChatColor.DARK_PURPLE + defending.getName() + " was doomed, and will die in " + entry.getValue() + " turns");
                            break;
                        case SILENCED:
                            fight.sendMessage(ChatColor.GRAY + defending.getName() + " was silenced for " + entry.getValue() + " turns");
                            break;
                    }
                }
            }
            return true;
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
        if (helmet != null) {
            switch (helmet.getType()) {
                case LEATHER_HELMET:
                    armourModifier -= 0.05D;
                    break;
                case GOLD_HELMET:
                case CHAINMAIL_HELMET:
                case IRON_HELMET:
                    armourModifier -= 0.1D;
                    break;
                case DIAMOND_HELMET:
                    armourModifier -= 0.15D;
                    break;
            }
        }
        if (chestplate != null) {
            switch (chestplate.getType()) {
                case LEATHER_CHESTPLATE:
                    armourModifier -= 0.15D;
                    break;
                case GOLD_CHESTPLATE:
                    armourModifier -= 0.25D;
                    break;
                case CHAINMAIL_CHESTPLATE:
                    armourModifier -= 0.25D;
                    break;
                case IRON_CHESTPLATE:
                    armourModifier -= 0.3D;
                    break;
                case DIAMOND_CHESTPLATE:
                    armourModifier -= 0.4D;
                    break;
            }
        }
        if (leggings != null) {
            switch (leggings.getType()) {
                case LEATHER_LEGGINGS:
                    armourModifier -= 0.1D;
                    break;
                case GOLD_LEGGINGS:
                    armourModifier -= 0.15D;
                    break;
                case CHAINMAIL_LEGGINGS:
                    armourModifier -= 0.2D;
                    break;
                case IRON_LEGGINGS:
                    armourModifier -= 0.25D;
                    break;
                case DIAMOND_LEGGINGS:
                    armourModifier -= 0.3D;
                    break;
            }
        }
        if (boots != null) {
            switch (boots.getType()) {
                case LEATHER_BOOTS:
                case GOLD_BOOTS:
                case CHAINMAIL_BOOTS:
                    armourModifier -= 0.05D;
                    break;
                case IRON_BOOTS:
                    armourModifier -= 0.1D;
                    break;
                case DIAMOND_BOOTS:
                    armourModifier -= 0.15D;
                    break;
            }
        }
        return Math.max(armourModifier, 0.01D);
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
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

    public double getCriticalMultiplier() {
        return criticalMultiplier;
    }

    public void setCriticalMultiplier(double criticalMultiplier) {
        this.criticalMultiplier = criticalMultiplier;
    }

    public int getHitChance() {
        return hitChance;
    }

    public void setHitChance(int hitChance) {
        this.hitChance = hitChance;
    }

    public abstract String getFightUseMessage(Character attacking, Character defending, double damage);

    public Map<StatusEffect, Integer> getStatusEffects() {
        return new EnumMap<>(StatusEffect.class);
    }

    public int getStatusEffectChance(StatusEffect statusEffect) {
        return 100;
    }

}
