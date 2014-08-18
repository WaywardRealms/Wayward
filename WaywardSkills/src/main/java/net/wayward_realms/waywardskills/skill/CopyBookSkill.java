package net.wayward_realms.waywardskills.skill;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.skills.SkillBase;
import net.wayward_realms.waywardskills.WaywardSkills;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CopyBookSkill extends SkillBase {

    private WaywardSkills plugin;

    public CopyBookSkill(WaywardSkills plugin) {
        this.plugin = plugin;
        setName("CopyBook");
        setCoolDown(1800);
    }

    @Override
    public boolean use(Player player) {
        if (player.getItemInHand().getType() == Material.WRITTEN_BOOK || player.getItemInHand().getType() == Material.BOOK_AND_QUILL) {
            player.getInventory().addItem(new ItemStack(player.getItemInHand()));
            player.sendMessage(ChatColor.GREEN + "Made a copy of the book.");
            return true;
        } else {
            player.sendMessage(ChatColor.RED + "You must be holding a book to make a copy of.");
            return false;
        }
    }

    @Override
    public boolean use(Fight fight, Character attacking, Character defending, ItemStack weapon) {
        return false;
    }

    @Override
    public ItemStack getIcon() {
        ItemStack icon = new ItemStack(Material.BOOK);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName("Copy Book");
        icon.setItemMeta(meta);
        return icon;
    }

    @Override
    public boolean canUse(Character character) {
        return plugin.getSpecialisationValue(character, plugin.getSpecialisation("Scribe")) >= 5;
    }

    @Override
    public String getDescription() {
        return "Copies a book.";
    }

    @Override
    public String getSpecialisationInfo() {
        return ChatColor.GRAY + "5 Scribe points required";
    }

}
