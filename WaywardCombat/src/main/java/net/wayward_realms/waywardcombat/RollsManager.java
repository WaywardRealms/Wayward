package net.wayward_realms.waywardcombat;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.classes.ClassesPlugin;
import net.wayward_realms.waywardlib.classes.Stat;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

public class RollsManager {

    private WaywardCombat plugin;

    public RollsManager(WaywardCombat plugin) {
        this.plugin = plugin;
    }

    public String getRoll(Character character, Stat stat) {
        RegisteredServiceProvider<ClassesPlugin> classesPluginProvider = plugin.getServer().getServicesManager().getRegistration(ClassesPlugin.class);
        if (classesPluginProvider != null) {
            ClassesPlugin classesPlugin = classesPluginProvider.getProvider();
            return "1d" + (10 + classesPlugin.getLevel(character)) + "+" + (int) Math.round((character.getStatValue(stat) + getArmourBonus(character, stat) + getWeaponBonus(character, stat)));
        }
        return "1d20+0";
    }

    private double getArmourBonus(Character character, Stat stat) {
        Player player = character.getPlayer().getPlayer();
        double bonus = 0D;
        // Helmets
        if (player.getInventory().getHelmet() != null) {
            ItemStack helmet = player.getInventory().getHelmet();
            Material helmetType = helmet.getType();
            bonus += plugin.getConfig().getDouble("rolls.armour." + helmetType.toString() + "." + stat.toString());
            bonus += getEtBonus(helmet, stat);
            bonus += getEnchantmentBonus(helmet, stat);
        }
        // Chestplates
        if (player.getInventory().getChestplate() != null) {
            ItemStack chestplate = player.getInventory().getChestplate();
            Material chestplateType = chestplate.getType();
            bonus += plugin.getConfig().getDouble("rolls.armour." + chestplateType.toString() + "." + stat.toString());
            bonus += getEtBonus(chestplate, stat);
            bonus += getEnchantmentBonus(chestplate, stat);
        }
        // Leggings
        if (player.getInventory().getLeggings() != null) {
            ItemStack leggings = player.getInventory().getLeggings();
            Material leggingsType = leggings.getType();
            bonus += plugin.getConfig().getDouble("rolls.armour." + leggingsType.toString() + "." + stat.toString());
            bonus += getEtBonus(leggings, stat);
            bonus += getEnchantmentBonus(leggings, stat);
        }
        // Boots
        if (player.getInventory().getBoots() != null) {
            ItemStack boots = player.getInventory().getBoots();
            Material bootsType = boots.getType();
            bonus += plugin.getConfig().getDouble("rolls.armour." + bootsType.toString() + "." + stat.toString());
            bonus += getEtBonus(boots, stat);
            bonus += getEnchantmentBonus(boots, stat);
        }
        return bonus;
    }

    private double getWeaponBonus(Character character, Stat stat) {
        double bonus = 0D;
        Player player = character.getPlayer().getPlayer();
        Material weaponType = player.getItemInHand().getType();
        bonus += plugin.getConfig().getDouble("rolls.weapons." + weaponType.toString() + "." + stat.toString());
        bonus += getEtBonus(player.getItemInHand(), stat);
        bonus += getEnchantmentBonus(player.getItemInHand(), stat);
        return bonus;
    }

    private double getEtBonus(ItemStack item, Stat stat) {
        double bonus = 0D;
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta.hasLore()) {
                for (String lore : meta.getLore()) {
                    if (lore.startsWith("et:")) {
                        if (StringUtils.countMatches(lore, ":") == 2) {
                            Stat stat1 = Stat.valueOf(lore.split(":")[1].toUpperCase().replace(' ', '_'));
                            if (stat1 == stat) {
                                double etBonus = Double.parseDouble(lore.split(":")[2]);
                                bonus += etBonus;
                            }
                        }
                    }
                }
            }
        }
        return bonus;
    }

    private double getEnchantmentBonus(ItemStack item, Stat stat) {
        double bonus = 0D;
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta.hasEnchants()) {
                for (Enchantment enchantment : meta.getEnchants().keySet()) {
                    bonus += meta.getEnchants().get(enchantment) * plugin.getConfig().getDouble("rolls.enchants." + enchantment.getName() + "." + stat.toString());
                }
            }
        }
        return bonus;
    }

}
