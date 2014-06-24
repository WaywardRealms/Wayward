package net.wayward_realms.waywardskills.spell;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.classes.Stat;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.professions.ToolType;
import net.wayward_realms.waywardlib.skills.AttackSpellBase;
import net.wayward_realms.waywardlib.skills.SkillType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class LightningSwordSpell extends AttackSpellBase {

    public LightningSwordSpell() {
        setName("LightningSword");
        setManaCost(100);
        setCoolDown(3600);
        setType(SkillType.MAGIC_SWORD);
        setCriticalMultiplier(4D);
        setCriticalChance(15);
        setPower(90);
        setHitChance(95);
        setAttackStat(Stat.MELEE_ATTACK);
        setDefenceStat(Stat.MAGIC_DEFENCE);
    }

    @Override
    public void animate(Fight fight, Character attacking, Character defending, ItemStack weapon) {
        Player defendingPlayer = defending.getPlayer().getPlayer();
        defendingPlayer.getWorld().strikeLightningEffect(defendingPlayer.getLocation());
    }

    @Override
    public double getWeaponModifier(ItemStack weapon) {
        return getMeleeWeaponModifier(weapon);
    }

    @Override
    public String getFightUseMessage(Character attacking, Character defending, double damage) {
        return (attacking.isNameHidden() ? ChatColor.MAGIC + attacking.getName() + ChatColor.RESET : attacking.getName()) + ChatColor.YELLOW + " imbued their weapon with lightning and hit " + (defending.isNameHidden() ? ChatColor.MAGIC + defending.getName() + ChatColor.RESET : defending.getName()) + ChatColor.YELLOW + ", dealing " + damage;
    }

    @Override
    public String getFightFailManaMessage(Character attacking, Character defending) {
        return (attacking.isNameHidden() ? ChatColor.MAGIC + attacking.getName() + ChatColor.RESET : attacking.getName()) + ChatColor.YELLOW + " attempted to imbue their weapon with lightning, but did not have enough mana.";
    }

    @Override
    public boolean use(Player player) {
        if (player.getItemInHand() != null) {
            if (ToolType.getToolType(player.getItemInHand().getType()) == ToolType.SWORD) {
                ItemMeta meta = player.getItemInHand().getItemMeta();
                List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<String>();
                lore.add("lightning:" + (System.currentTimeMillis() + 60000));
                meta.setLore(lore);
                player.getItemInHand().setItemMeta(meta);
                player.sendMessage(ChatColor.GREEN + "Imbued weapon with lightning for 1 minute.");
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack getIcon() {
        ItemStack icon = new ItemStack(Material.GOLD_SWORD, 1);
        ItemMeta iconMeta = icon.getItemMeta();
        iconMeta.setDisplayName("Lightning Sword");
        icon.setItemMeta(iconMeta);
        return icon;
    }

    @Override
    public boolean canUse(Character character) {
        return character.getSkillPoints(SkillType.MAGIC_SWORD) >= 80;
    }
}
