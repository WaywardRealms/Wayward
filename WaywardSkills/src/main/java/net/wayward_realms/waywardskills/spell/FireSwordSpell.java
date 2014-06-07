package net.wayward_realms.waywardskills.spell;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.skills.SkillType;
import net.wayward_realms.waywardlib.skills.SpellBase;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FireSwordSpell extends SpellBase {

    public FireSwordSpell() {
        setName("FireSword");
        setCoolDown(1800);
        setManaCost(100);
        setType(SkillType.MAGIC_SWORD);
    }

    @Override
    public boolean use(Player player) {
        if (player.getItemInHand() != null) {
            try {
                player.getItemInHand().addEnchantment(Enchantment.FIRE_ASPECT, 1);
                return true;
            } catch (IllegalArgumentException exception) {
                player.sendMessage(ChatColor.RED + "Fire aspect cannot be applied to that weapon.");
            }
        } else {
            player.sendMessage(ChatColor.RED + "You must be holding an item to apply fire aspect to.");
        }
        return false;
    }

    @Override
    public boolean use(Fight fight, Character attacking, Character defending, ItemStack weapon) {
        return use(defending.getPlayer().getPlayer());
    }

    @Override
    public ItemStack getIcon() {
        ItemStack icon = new ItemStack(Material.GOLD_SWORD);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName("Fire Sword");
        icon.setItemMeta(meta);
        return icon;
    }

    @Override
    public boolean canUse(Character character) {
        return character.getSkillPoints(SkillType.MAGIC_SWORD) >= 25;
    }

}
