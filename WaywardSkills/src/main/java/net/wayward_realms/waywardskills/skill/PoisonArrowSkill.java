package net.wayward_realms.waywardskills.skill;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.classes.Stat;
import net.wayward_realms.waywardlib.combat.Combatant;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.skills.AttackSkillBase;
import net.wayward_realms.waywardlib.skills.SkillType;
import net.wayward_realms.waywardlib.skills.SkillsPlugin;
import org.bukkit.*;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public class PoisonArrowSkill extends AttackSkillBase {

    public PoisonArrowSkill() {
        setName("PoisonArrow");
        setCoolDown(20);
        setType(SkillType.RANGED_OFFENCE);
        setPower(30);
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
            if (player.getInventory().containsAtLeast(new ItemStack(Material.ARROW), 1) && player.getInventory().containsAtLeast(new ItemStack(Material.FERMENTED_SPIDER_EYE), 1)) {
                Plugin plugin = Bukkit.getServer().getServicesManager().getRegistration(SkillsPlugin.class).getProvider();
                final Arrow arrow = player.launchProjectile(Arrow.class);
                arrow.setMetadata("isPoisonArrow", new FixedMetadataValue(plugin, true));
                plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        arrow.getLocation().getWorld().playEffect(arrow.getLocation(), Effect.ENDER_SIGNAL, 0);
                    }
                }, 5L, 5L);
                player.getInventory().removeItem(new ItemStack(Material.ARROW), new ItemStack(Material.FERMENTED_SPIDER_EYE));
                return true;
            } else {
                player.sendMessage(ChatColor.RED + "You require a fermented spider eye and an arrow to create a poisoned arrow.");
            }
        } else {
            player.sendMessage(ChatColor.RED + "You require a bow to launch a poisoned arrow.");
        }
        return false;
    }

    @Override
    public ItemStack getIcon() {
        ItemStack icon = new ItemStack(Material.ARROW);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName("Poison Arrow");
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
        return character.getSkillPoints(SkillType.RANGED_OFFENCE) >= 1;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialised = new HashMap<>();
        serialised.put("name", getName());
        serialised.put("cooldown", getCoolDown());
        return serialised;
    }

    public static PoisonArrowSkill deserialize(Map<String, Object> serialised) {
        PoisonArrowSkill deserialised = new PoisonArrowSkill();
        deserialised.setName((String) serialised.get("name"));
        deserialised.setCoolDown((int) serialised.get("cooldown"));
        return deserialised;
    }

    @Override
    public void animate(Fight fight, Character attacking, Character defending, ItemStack weapon) {
        RegisteredServiceProvider<SkillsPlugin> skillsPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(SkillsPlugin.class);
        if (skillsPluginProvider != null) {
            SkillsPlugin plugin = skillsPluginProvider.getProvider();
            final Arrow arrow = attacking.getPlayer().getPlayer().launchProjectile(Arrow.class);
            plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                @Override
                public void run() {
                    arrow.getLocation().getWorld().playEffect(arrow.getLocation(), Effect.ENDER_SIGNAL, 0);
                }
            }, 5L, 5L);
        }
        attacking.getPlayer().getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.POISON, 200, 0), true);
    }

    @Override
    public double getWeaponModifier(ItemStack weapon) {
        return getRangedWeaponModifier(weapon);
    }

    @Override
    public String getFightUseMessage(Character attacking, Character defending, double damage) {
        return attacking.getName() + " shot a poisoned arrow at " + defending.getName() + " dealing " + damage + " points of damage, and poisoning them.";
    }
}
