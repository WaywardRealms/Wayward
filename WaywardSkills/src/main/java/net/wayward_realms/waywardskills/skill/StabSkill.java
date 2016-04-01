package net.wayward_realms.waywardskills.skill;

import com.google.common.collect.ImmutableMap;
import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.classes.Stat;
import net.wayward_realms.waywardlib.combat.Combatant;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.skills.AttackSkillBase;
import net.wayward_realms.waywardlib.skills.SkillType;
import net.wayward_realms.waywardlib.util.vector.Vector3D;
import net.wayward_realms.waywardlib.util.vector.VectorUtils;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.EnumMap;

import static java.math.BigDecimal.ZERO;

public class StabSkill extends AttackSkillBase {

    private int reach = 8;

    public StabSkill() {
        setName("Stab");
        setCoolDown(60);
        setType(SkillType.MELEE_OFFENCE);
        setAttackStat(Stat.MELEE_ATTACK);
        setDefenceStat(Stat.MELEE_DEFENCE);
        setPower(40);
        setHitChance(90);
        setCriticalChance(20);
        setCriticalMultiplier(1.5D);
    }

    public int getReach() {
        return reach;
    }

    public void setReach(int reach) {
        this.reach = reach;
    }

    @Override
    public boolean use(Player player) {
        Location observerPos = player.getEyeLocation();
        Vector3D observerDir = new Vector3D(observerPos.getDirection());
        Vector3D observerStart = new Vector3D(observerPos);
        Vector3D observerEnd = observerStart.add(observerDir.multiply(getReach()));
        // Get nearby entities
        for (LivingEntity target : player.getWorld().getLivingEntities()) {
            // Bounding box of the given player
            Vector3D targetPos = new Vector3D(target.getLocation());
            Vector3D minimum = targetPos.add(-0.5, 0, -0.5);
            Vector3D maximum = targetPos.add(0.5, 1.67, 0.5);
            if (target != player && VectorUtils.hasIntersection(observerStart, observerEnd, minimum, maximum)) {
                player.teleport(target);
                double damage = 10D;
                EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(player, target, EntityDamageEvent.DamageCause.ENTITY_ATTACK, new EnumMap(ImmutableMap.of(EntityDamageEvent.DamageModifier.BASE, Double.valueOf(damage))), new EnumMap(ImmutableMap.of(EntityDamageEvent.DamageModifier.BASE, ZERO)));
                Bukkit.getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    if (event.getEntity() instanceof LivingEntity) {
                        ((LivingEntity) event.getEntity()).damage(event.getDamage(), event.getDamager());
                        event.getEntity().setLastDamageCause(event);
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean use(Fight fight, Combatant attacking, Combatant defending, ItemStack weapon) {
        return use(fight, (Character) attacking, (Character) defending, weapon);
    }

    @Override
    public void animate(Fight fight, Character attacking, Character defending, ItemStack weapon) {

    }

    @Override
    public double getWeaponModifier(ItemStack weapon) {
        return getMeleeWeaponModifier(weapon);
    }

    @Override
    public String getFightUseMessage(Character attacking, Character defending, double damage) {
        return (attacking.isNameHidden() ? ChatColor.MAGIC + attacking.getName() + ChatColor.RESET : attacking.getName()) + ChatColor.YELLOW + " stabbed at " + (defending.isNameHidden() ? ChatColor.MAGIC + defending.getName() + ChatColor.RESET : defending.getName()) + ChatColor.YELLOW + " dealing " + (Math.round(damage * 100D) / 100D) + " points of damage.";
    }

    @Override
    public ItemStack getIcon() {
        ItemStack icon = new ItemStack(Material.IRON_SWORD, 1);
        ItemMeta iconMeta = icon.getItemMeta();
        iconMeta.setDisplayName("Stab");
        icon.setItemMeta(iconMeta);
        return icon;
    }

    @Override
    public boolean canUse(Combatant combatant) {
        return canUse((Character) combatant);
    }

    public boolean canUse(Character character) {
        return character.getSkillPoints(SkillType.MELEE_OFFENCE) >= 5;
    }

    @Override
    public boolean canUse(OfflinePlayer player) {
        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterPluginProvider != null) {
            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
            return canUse(characterPlugin.getActiveCharacter(player));
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "Deals damage equal to the difference between your melee attack roll and your target's melee defence roll";
    }

}
