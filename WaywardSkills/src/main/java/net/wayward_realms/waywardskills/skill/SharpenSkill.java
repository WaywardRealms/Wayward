package net.wayward_realms.waywardskills.skill;

import net.wayward_realms.waywardlib.character.*;
import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.combat.Combatant;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.skills.Skill;
import net.wayward_realms.waywardlib.skills.SkillType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.HashMap;
import java.util.Map;

public class SharpenSkill implements Skill {

    private String name = "Sharpen";
    private int coolDown = 60;
    private SkillType type = SkillType.MELEE_OFFENCE;

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
    public boolean use(Fight fight, Combatant attacking, Combatant defending, ItemStack weapon) {
        return false;
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
    public boolean canUse(Combatant combatant) {
        return canUse((Character) combatant);
    }

    @Override
    public boolean canUse(OfflinePlayer player) {
        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterPluginProvider != null) {
            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
            return canUse(characterPlugin.getActiveCharacter(player));
        }
        return false;
    }

    @Override
    public boolean canUse(Character character) {
        return character.getSkillPoints(SkillType.MELEE_OFFENCE) >= 1;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public SkillType getType() {
        return type;
    }

    @Override
    public void setType(SkillType type) {
        this.type = type;
    }

    @Override
    public int getCoolDown() {
        return coolDown;
    }

    @Override
    public void setCoolDown(int coolDown) {
        this.coolDown = coolDown;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialised = new HashMap<>();
        serialised.put("name", name);
        serialised.put("cooldown", coolDown);
        return serialised;
    }

    public static SharpenSkill deserialize(Map<String, Object> serialised) {
        SharpenSkill deserialised = new SharpenSkill();
        deserialised.name = (String) serialised.get("name");
        deserialised.coolDown = (int) serialised.get("cooldown");
        return deserialised;
    }

}
