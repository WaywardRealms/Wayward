package net.wayward_realms.waywardskills.skill;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.classes.Stat;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.combat.StatusEffect;
import net.wayward_realms.waywardlib.skills.AttackSkillBase;
import net.wayward_realms.waywardlib.skills.SkillType;
import net.wayward_realms.waywardlib.skills.SkillsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.HashMap;
import java.util.Map;

public class SnareShotSkill extends AttackSkillBase {

    public SnareShotSkill() {
        setName("SnareShot");
        setCoolDown(180);
        setType(SkillType.RANGED_DEFENCE);
        setPower(5);
        setAttackStat(Stat.RANGED_ATTACK);
        setDefenceStat(Stat.RANGED_DEFENCE);
        setHitChance(90);
        setCriticalChance(1);
    }

    @Override
    public boolean use(Player player) {
        boolean containsBow = false;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null) {
                if (item.getType() == Material.BOW) {
                    containsBow = true;
                    break;
                }
            }
        }
        if (containsBow) {
            if (player.getInventory().containsAtLeast(new ItemStack(Material.ARROW), 1) && player.getInventory().containsAtLeast(new ItemStack(Material.STRING), 1)) {
                Plugin plugin = Bukkit.getServer().getServicesManager().getRegistration(SkillsPlugin.class).getProvider();
                final Arrow arrow = player.launchProjectile(Arrow.class);
                arrow.setMetadata("isSnareShot", new FixedMetadataValue(plugin, true));
                player.getInventory().removeItem(new ItemStack(Material.ARROW), new ItemStack(Material.STRING));
                return true;
            } else {
                player.sendMessage(ChatColor.RED + "You require some string and an arrow to create a snare shot.");
            }
        } else {
            player.sendMessage(ChatColor.RED + "You require a bow to launch a snare shot.");
        }
        return false;
    }

    @Override
    public ItemStack getIcon() {
        ItemStack icon = new ItemStack(Material.ARROW);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName("Snare Shot");
        icon.setItemMeta(meta);
        return icon;
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
    public String getDescription() {
        return "If your ranged attack roll is higher than your target's ranged defence roll, prevent your target from making a move for 3 turns";
    }

    @Override
    public boolean canUse(Character character) {
        return character.getSkillPoints(SkillType.RANGED_DEFENCE) >= 8;
    }

    @Override
    public void animate(Fight fight, Character attacking, Character defending, ItemStack weapon) {
        attacking.getPlayer().getPlayer().launchProjectile(Arrow.class);
    }

    @Override
    public double getWeaponModifier(ItemStack weapon) {
        return getRangedWeaponModifier(weapon);
    }

    @Override
    public String getFightUseMessage(Character attacking, Character defending, double damage) {
        return (attacking.isNameHidden() ? ChatColor.MAGIC + attacking.getName() + ChatColor.RESET : attacking.getName()) + ChatColor.YELLOW + " launched a snare shot at " + (defending.isNameHidden() ? ChatColor.MAGIC + defending.getName() + ChatColor.RESET : defending.getName()) + ChatColor.YELLOW + " dealing " + damage + " points of damage, and paralysing them.";
    }

    @Override
    public Map<StatusEffect, Integer> getStatusEffects() {
        Map<StatusEffect, Integer> statusEffects = new HashMap<>();
        statusEffects.put(StatusEffect.PARALYSIS, 3);
        return statusEffects;
    }
}
