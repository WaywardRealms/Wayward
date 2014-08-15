package net.wayward_realms.waywardcharacters;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.classes.Class;
import net.wayward_realms.waywardlib.classes.ClassesPlugin;
import net.wayward_realms.waywardlib.classes.Stat;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class StatsCommand implements CommandExecutor {

    private WaywardCharacters plugin;

    public StatsCommand(WaywardCharacters plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            net.wayward_realms.waywardlib.character.Character character = plugin.getActiveCharacter(player);
            RegisteredServiceProvider<ClassesPlugin> classesPluginProvider = plugin.getServer().getServicesManager().getRegistration(ClassesPlugin.class);
            if (classesPluginProvider != null) {
                ClassesPlugin classesPlugin = classesPluginProvider.getProvider();
                sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + character.getName() + "'s stats");
                sender.sendMessage(ChatColor.GREEN + "Health: " + character.getHealth() + "/" + character.getMaxHealth() + (classesPlugin.getLevel(character) < classesPlugin.getClass(character).getMaxLevel() ? ChatColor.GRAY + " (Next level: +" + getHpIncrease(character, classesPlugin.getLevel(character)) + ")" : ""));
                sender.sendMessage(ChatColor.GREEN + "Mana: " + character.getMana() + "/" + character.getMaxMana() + (classesPlugin.getLevel(character) < classesPlugin.getClass(character).getMaxLevel() ? ChatColor.GRAY + " (Next level: +" + getManaIncrease(character, classesPlugin.getLevel(character)) + ")" : ""));
                sender.sendMessage(ChatColor.GREEN + "Thirst: " + character.getThirst() + "/" + 20);
                sender.sendMessage(ChatColor.GREEN + "Melee attack: " + character.getStatValue(Stat.MELEE_ATTACK) + (classesPlugin.getLevel(character) < classesPlugin.getClass(character).getMaxLevel() ? ChatColor.GRAY + " (Next level: +" + getStatIncrease(character, Stat.MELEE_ATTACK, classesPlugin.getLevel(character)) + ")" : ""));
                sender.sendMessage(ChatColor.GREEN + "Melee defence: " + character.getStatValue(Stat.MELEE_DEFENCE) + (classesPlugin.getLevel(character) < classesPlugin.getClass(character).getMaxLevel() ? ChatColor.GRAY + " (Next level: +" + getStatIncrease(character, Stat.MELEE_DEFENCE, classesPlugin.getLevel(character)) + ")" : ""));
                sender.sendMessage(ChatColor.GREEN + "Ranged attack: " + character.getStatValue(Stat.RANGED_ATTACK) + (classesPlugin.getLevel(character) < classesPlugin.getClass(character).getMaxLevel() ? ChatColor.GRAY + " (Next level: +" + getStatIncrease(character, Stat.RANGED_ATTACK, classesPlugin.getLevel(character)) + ")" : ""));
                sender.sendMessage(ChatColor.GREEN + "Ranged defence: " + character.getStatValue(Stat.RANGED_DEFENCE) + (classesPlugin.getLevel(character) < classesPlugin.getClass(character).getMaxLevel() ? ChatColor.GRAY + " (Next level: +" + getStatIncrease(character, Stat.RANGED_DEFENCE, classesPlugin.getLevel(character)) + ")" : ""));
                sender.sendMessage(ChatColor.GREEN + "Magic attack: " + character.getStatValue(Stat.MAGIC_ATTACK) + (classesPlugin.getLevel(character) < classesPlugin.getClass(character).getMaxLevel() ? ChatColor.GRAY + " (Next level: +" + getStatIncrease(character, Stat.MAGIC_ATTACK, classesPlugin.getLevel(character)) + ")" : ""));
                sender.sendMessage(ChatColor.GREEN + "Magic defence: " + character.getStatValue(Stat.MAGIC_DEFENCE) + (classesPlugin.getLevel(character) < classesPlugin.getClass(character).getMaxLevel() ? ChatColor.GRAY + " (Next level: +" + getStatIncrease(character, Stat.MAGIC_DEFENCE, classesPlugin.getLevel(character)) + ")" : ""));
                sender.sendMessage(ChatColor.GREEN + "Speed: " + character.getStatValue(Stat.SPEED) + (classesPlugin.getLevel(character) < classesPlugin.getClass(character).getMaxLevel() ? ChatColor.GRAY + " (Next level: +" + getStatIncrease(character, Stat.SPEED, classesPlugin.getLevel(character)) + ")" : ""));
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to perform this command.");
        }
        return true;
    }

    private int getStatIncrease(Character character, Stat stat, int level) {
        return getStatIncrease(character, stat, level, level + 1);
    }

    private int getStatIncrease(Character character, Stat stat, int startLevel, int endLevel) {
        return getStatAtLevel(character, stat, endLevel) - getStatAtLevel(character, stat, startLevel);
    }

    private int getStatAtLevel(Character character, Stat stat, int level) {
        return 0;
    }

    private int getHpIncrease(Character character, int level) {
        return getHpIncrease(character, level, level + 1);
    }

    private int getHpIncrease(Character character, int startLevel, int endLevel) {
        return getHpAtLevel(character, endLevel) - getHpAtLevel(character, startLevel);
    }

    private int getHpAtLevel(Character character, int level) {
        RegisteredServiceProvider<ClassesPlugin> classesPluginProvider = plugin.getServer().getServicesManager().getRegistration(ClassesPlugin.class);
        if (classesPluginProvider != null) {
            ClassesPlugin classesPlugin = classesPluginProvider.getProvider();
            Class clazz = classesPlugin.getClass(character);
            return (int) Math.round((((250D + (20D * clazz.getHpBonus())) * (double) level) / 100D) + 10D);
        }
        return 0;
    }

    private int getManaIncrease(Character character, int level) {
        return getManaIncrease(character, level, level + 1);
    }

    private int getManaIncrease(Character character, int startLevel, int endLevel) {
        return getManaAtLevel(character, endLevel) - getManaAtLevel(character, startLevel);
    }

    private int getManaAtLevel(Character character, int level) {
        RegisteredServiceProvider<ClassesPlugin> classesPluginProvider = plugin.getServer().getServicesManager().getRegistration(ClassesPlugin.class);
        if (classesPluginProvider != null) {
            ClassesPlugin classesPlugin = classesPluginProvider.getProvider();
            Class clazz = classesPlugin.getClass(character);
            return (int) Math.round((((250D + (20D * clazz.getManaBonus())) * (double) level) / 100D) + 20D);
        }
        return 0;
    }

}
