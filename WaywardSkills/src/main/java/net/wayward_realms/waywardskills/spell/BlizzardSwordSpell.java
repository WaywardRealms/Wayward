package net.wayward_realms.waywardskills.spell;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.professions.ToolType;
import net.wayward_realms.waywardlib.skills.SkillType;
import net.wayward_realms.waywardlib.skills.SpellBase;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class BlizzardSwordSpell extends SpellBase {

    public BlizzardSwordSpell() {
        setName("BlizzardSword");
        setManaCost(100);
        setCoolDown(1800);
        setType(SkillType.MAGIC_SWORD);
    }

    @Override
    public boolean use(Player player) {
        if (player.getItemInHand() != null) {
            if (ToolType.getToolType(player.getItemInHand().getType()) == ToolType.SWORD) {
                ItemMeta meta = player.getItemInHand().getItemMeta();
                List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<String>();
                lore.add("blizzard:" + (System.currentTimeMillis() + 60000));
                meta.setLore(lore);
                player.getItemInHand().setItemMeta(meta);
                player.sendMessage(ChatColor.GREEN + "Imbued weapon with blizzard for 1 minute.");
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean use(Fight fight, Character attacking, Character defending, ItemStack weapon) {
        //TODO Status effects, requires #37
        return false;
    }

    @Override
    public ItemStack getIcon() {
        ItemStack icon = new ItemStack(Material.DIAMOND_SWORD, 1);
        ItemMeta iconMeta = icon.getItemMeta();
        iconMeta.setDisplayName("Blizzard Sword");
        icon.setItemMeta(iconMeta);
        return icon;
    }

    @Override
    public boolean canUse(Character character) {
        return character.getSkillPoints(SkillType.MAGIC_SWORD) >= 70;
    }

}
