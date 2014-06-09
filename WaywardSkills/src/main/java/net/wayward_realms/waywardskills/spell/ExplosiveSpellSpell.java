package net.wayward_realms.waywardskills.spell;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.classes.Stat;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.skills.AttackSpellBase;
import net.wayward_realms.waywardlib.skills.SkillType;
import net.wayward_realms.waywardlib.util.lineofsight.LineOfSightUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ExplosiveSpellSpell extends AttackSpellBase {

    public ExplosiveSpellSpell() {
        setName("ExplosiveSpell");
        setType(SkillType.MAGIC_OFFENCE);
        setAttackStat(Stat.MAGIC_ATTACK);
        setDefenceStat(Stat.MAGIC_DEFENCE);
        setHitChance(100);
        setCriticalChance(20);
        setCriticalMultiplier(2D);
        setPower(120D);
        setManaCost(50);
        setCoolDown(300);
    }

    @Override
    public void animate(Fight fight, Character attacking, Character defending, ItemStack weapon) {
        Player defendingPlayer = defending.getPlayer().getPlayer();
        defendingPlayer.getWorld().createExplosion(defendingPlayer.getLocation(), 0F, false);
    }

    @Override
    public double getWeaponModifier(ItemStack weapon) {
        return getMagicWeaponModifier(weapon);
    }

    @Override
    public String getFightUseMessage(Character attacking, Character defending, double damage) {
        return attacking.getName() + " blew " + defending.getName() + " up with explosive magic, dealing " + damage + " damage.";
    }

    @Override
    public String getFightFailManaMessage(Character attacking, Character defending) {
        return attacking + " attempted to blow " + defending.getName() + " up, but did not have enough mana.";
    }

    @Override
    public boolean use(Player player) {
        Block targetBlock = LineOfSightUtils.getTargetBlock(player, null, 64);
        targetBlock.getWorld().createExplosion(targetBlock.getLocation(), 8F, true);
        return true;
    }

    @Override
    public ItemStack getIcon() {
        ItemStack icon = new ItemStack(Material.TNT);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName("Explosive Spell");
        icon.setItemMeta(meta);
        return icon;
    }

    @Override
    public boolean canUse(Character character) {
        return character.getSkillPoints(SkillType.MAGIC_OFFENCE) >= 24;
    }
}
