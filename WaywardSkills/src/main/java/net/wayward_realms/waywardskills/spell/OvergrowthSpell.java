package net.wayward_realms.waywardskills.spell;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.classes.Stat;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.skills.AttackSpellBase;
import net.wayward_realms.waywardlib.skills.SkillType;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
        return attacking.getName() + " caused a huge tree to grow around " + defending.getName() + ", dealing " + damage + " damage.";
    }

    @Override
    public String getFightFailManaMessage(Character attacking, Character defending) {
        return attacking.getName() + " attempted to rapidly grow a huge tree, but did not have enough mana.";
    }

    @Override
    public boolean use(Player player) {
        Block targetBlock = getTargetBlock(player, null, 32);
        targetBlock.getWorld().generateTree(targetBlock.getLocation(), TreeType.JUNGLE);
        for (LivingEntity entity : player.getWorld().getLivingEntities()) {
            if (entity.getLocation().distanceSquared(targetBlock.getLocation()) <= 256) {
                entity.damage(15D, player);
            }
        }
        return true;
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

}
