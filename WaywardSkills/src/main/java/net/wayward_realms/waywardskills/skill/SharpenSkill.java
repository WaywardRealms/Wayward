package net.wayward_realms.waywardskills.skill;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.skills.SkillBase;
import net.wayward_realms.waywardskills.WaywardSkills;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SharpenSkill extends SkillBase {

    private WaywardSkills plugin;

    public SharpenSkill(WaywardSkills plugin) {
        this.plugin = plugin;
        setName("Sharpen");
        setCoolDown(60);
    }

    @Override
    public boolean use(Player player) {
        if (player.getItemInHand() != null) {
            if (player.getItemInHand().getType() == Material.WOOD_SWORD ||
                    player.getItemInHand().getType() == Material.STONE_SWORD ||
                    player.getItemInHand().getType() == Material.IRON_SWORD ||
                    player.getItemInHand().getType() == Material.DIAMOND_SWORD) {
                if (player.getInventory().containsAtLeast(new ItemStack(Material.FLINT), 10)) {
                    player.getItemInHand().addEnchantment(Enchantment.DAMAGE_ALL, 1);
                    player.getInventory().removeItem(new ItemStack(Material.FLINT, 10));
                    return true;
                } else {
                    player.sendMessage(ChatColor.RED + "You require 10 flint to sharpen a sword.");
                }
            } else {
                player.sendMessage(ChatColor.RED + "You need to be holding a sword.");
            }
        } else {
            player.sendMessage(ChatColor.RED + "You need to be holding a sword.");
        }
        return false;
    }

    @Override
    public boolean use(Fight fight, Character attacking, Character defending, ItemStack weapon) {
        return use(defending.getPlayer().getPlayer());
    }

    @Override
    public ItemStack getIcon() {
        ItemStack icon = new ItemStack(Material.ANVIL);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName("Sharpen");
        icon.setItemMeta(meta);
        return icon;
    }

    @Override
    public String getDescription() {
        return "Add 5 to your melee attack roll for 5 turns if you are holding a sword";
    }

    @Override
    public boolean canUse(Character character) {
        return false;//plugin.getSpecialisationValue(character, plugin.getSpecialisation("Smithing")) >= 5;
    }

    @Override
    public String getSpecialisationInfo() {
        return ChatColor.GRAY + "Not currently obtainable";
    }

}
