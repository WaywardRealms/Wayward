package net.wayward_realms.waywardskills.spell;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.skills.AttackSpellBase;
import net.wayward_realms.waywardlib.skills.Stat;
import net.wayward_realms.waywardlib.util.lineofsight.LineOfSightUtils;
import net.wayward_realms.waywardskills.WaywardSkills;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ExplosiveSpellSpell extends AttackSpellBase {

    private WaywardSkills plugin;

    public ExplosiveSpellSpell(WaywardSkills plugin) {
        this.plugin = plugin;
        setName("ExplosiveSpell");
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
        return (attacking.isNameHidden() ? ChatColor.MAGIC + attacking.getName() + ChatColor.RESET : attacking.getName()) + ChatColor.YELLOW + " blew " + (defending.isNameHidden() ? ChatColor.MAGIC + defending.getName() + ChatColor.RESET : defending.getName()) + ChatColor.YELLOW + " up with explosive magic, dealing " + damage + " damage.";
    }

    @Override
    public String getFightFailManaMessage(Character attacking, Character defending) {
        return (attacking.isNameHidden() ? ChatColor.MAGIC + attacking.getName() + ChatColor.RESET : attacking.getName()) + ChatColor.YELLOW + " attempted to blow " + (defending.isNameHidden() ? ChatColor.MAGIC + defending.getName() + ChatColor.RESET : defending.getName()) + ChatColor.YELLOW + " up, but did not have enough mana.";
    }

    @Override
    public boolean use(Player player) {
        Location target = LineOfSightUtils.getTargetBlock(player, null, 64).getLocation();
        double x = target.getX();
        double y = target.getY();
        double z = target.getZ();
        target.getWorld().createExplosion(x, y, z, 8F, true, false);
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
        return hasScroll(character) && plugin.getSpecialisationValue(character, plugin.getSpecialisation("Fire Magic")) >= 75;
    }

    @Override
    public String getDescription() {
        return "Deal damage equal to five times the difference between your magic attack roll and your opponent's magic defence roll";
    }
}
