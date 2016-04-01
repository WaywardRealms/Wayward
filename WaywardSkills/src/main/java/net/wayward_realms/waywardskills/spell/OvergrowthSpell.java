package net.wayward_realms.waywardskills.spell;

import com.google.common.collect.ImmutableMap;
import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.classes.Stat;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.skills.AttackSpellBase;
import net.wayward_realms.waywardlib.skills.SkillType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.EnumMap;

import static java.math.BigDecimal.ZERO;
import static net.wayward_realms.waywardlib.util.lineofsight.LineOfSightUtils.getTargetBlock;

public class OvergrowthSpell extends AttackSpellBase {

    public OvergrowthSpell() {
        setName("Overgrowth");
        setPower(65);
        setCoolDown(1500);
        setManaCost(100);
        setCriticalChance(20);
        setAttackStat(Stat.MAGIC_ATTACK);
        setDefenceStat(Stat.MAGIC_DEFENCE);
        setHitChance(80);
        setCriticalMultiplier(2.5D);
        setType(SkillType.MAGIC_NATURE);
    }

    @Override
    public void animate(Fight fight, Character attacking, Character defending, ItemStack weapon) {
        Player defendingPlayer = defending.getPlayer().getPlayer();
        defendingPlayer.getWorld().generateTree(defendingPlayer.getLocation(), TreeType.JUNGLE);
        defendingPlayer.getWorld().getBlockAt(defendingPlayer.getLocation()).setType(Material.AIR);
        defendingPlayer.getWorld().getBlockAt(defendingPlayer.getEyeLocation()).setType(Material.AIR);
    }

    @Override
    public double getWeaponModifier(ItemStack weapon) {
        return getMagicWeaponModifier(weapon);
    }

    @Override
    public String getFightUseMessage(Character attacking, Character defending, double damage) {
        return (attacking.isNameHidden() ? ChatColor.MAGIC + attacking.getName() + ChatColor.RESET : attacking.getName()) + ChatColor.YELLOW + " caused a huge tree to grow around " + (defending.isNameHidden() ? ChatColor.MAGIC + defending.getName() + ChatColor.RESET : defending.getName()) + ChatColor.YELLOW + ", dealing " + damage + " damage.";
    }

    @Override
    public String getFightFailManaMessage(Character attacking, Character defending) {
        return (attacking.isNameHidden() ? ChatColor.MAGIC + attacking.getName() + ChatColor.RESET : attacking.getName()) + ChatColor.YELLOW + " attempted to rapidly grow a huge tree, but did not have enough mana.";
    }

    @Override
    public boolean use(Player player) {
        Block targetBlock = getTargetBlock(player, null, 32);
        boolean successful = targetBlock.getWorld().generateTree(targetBlock.getRelative(BlockFace.UP).getLocation(), TreeType.JUNGLE);
        if (successful) {
            for (LivingEntity entity : player.getWorld().getLivingEntities()) {
                if (entity.getLocation().distanceSquared(targetBlock.getLocation()) <= 256) {
                    double damage = 15D;
                    EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(player, entity, EntityDamageEvent.DamageCause.MAGIC, new EnumMap(ImmutableMap.of(EntityDamageEvent.DamageModifier.BASE, Double.valueOf(damage))), new EnumMap(ImmutableMap.of(EntityDamageEvent.DamageModifier.BASE, ZERO)));
                    Bukkit.getPluginManager().callEvent(event);
                    if (!event.isCancelled()) {
                        if (event.getEntity() instanceof LivingEntity) {
                            ((LivingEntity) event.getEntity()).damage(event.getDamage(), event.getDamager());
                            event.getEntity().setLastDamageCause(event);
                        }
                    }
                }
            }
        } else {
            player.sendMessage(ChatColor.RED + "Could not grow a tree there.");
        }
        return successful;
    }

    @Override
    public ItemStack getIcon() {
        ItemStack icon = new ItemStack(Material.SAPLING);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName("Overgrowth");
        icon.setItemMeta(meta);
        return icon;
    }

    @Override
    public boolean canUse(Character character) {
        return character.getSkillPoints(SkillType.MAGIC_NATURE) >= 80;
    }

    @Override
    public String getDescription() {
        return "Deals damage equal to 3 times the difference between your magic attack roll and target's magic defence roll to up to 3 targets, and prevents them from making one move";
    }

}
