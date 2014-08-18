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

import java.util.Arrays;
import java.util.List;

public class CopyScrollSkill extends SkillBase {

    private WaywardSkills plugin;

    public CopyScrollSkill(WaywardSkills plugin) {
        this.plugin = plugin;
        setName("CopyScroll");
        setCoolDown(1800);
    }

    @Override
    public boolean use(Player player) {
        if (player.getItemInHand().getType() == Material.PAPER && player.getItemInHand().hasItemMeta() && player.getItemInHand().getItemMeta().hasDisplayName() && player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.YELLOW + "Scroll")) {
            player.getInventory().addItem(new ItemStack(player.getItemInHand()));
            player.sendMessage(ChatColor.GREEN + "Made a copy of the scroll.");
            return true;
        } else {
            player.sendMessage(ChatColor.RED + "You must be holding a scroll to make a copy of.");
            return false;
        }
    }

    @Override
    public boolean use(Fight fight, Character attacking, Character defending, ItemStack weapon) {
        return false;
    }

    @Override
    public ItemStack getIcon() {
        ItemStack icon = new ItemStack(Material.PAPER);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName("Copy Scroll");
        icon.setItemMeta(meta);
        return icon;
    }

    @Override
    public boolean canUse(Character character) {
        return plugin.getSpecialisationValue(character, plugin.getSpecialisation("Scribe")) >= 25;
    }

    @Override
    public String getDescription() {
        return "Copies a scroll.";
    }

    @Override
    public List<String> getSpecialisationInfo() {
        return Arrays.asList(ChatColor.GRAY + "25 Scribe points required");
    }

}
