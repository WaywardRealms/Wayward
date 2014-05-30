package net.wayward_realms.waywardcharacters;

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
                sender.sendMessage(ChatColor.GREEN + "Health: " + character.getHealth() + "/" + character.getMaxHealth() + (classesPlugin.getLevel(character) < classesPlugin.getClass(character).getMaxLevel() ? ChatColor.GRAY + " (Next level: +" + getHpIncrease(classesPlugin.getClass(character), classesPlugin.getLevel(character)) + ")" : ""));
                sender.sendMessage(ChatColor.GREEN + "Mana: " + character.getMana() + "/" + character.getMaxMana() + (classesPlugin.getLevel(character) < classesPlugin.getClass(character).getMaxLevel() ? ChatColor.GRAY + " (Next level: +" + getManaIncrease(classesPlugin.getClass(character), classesPlugin.getLevel(character)) + ")" : ""));
                sender.sendMessage(ChatColor.GREEN + "Thirst: " + character.getThirst() + "/" + 20);
                sender.sendMessage(ChatColor.GREEN + "Melee attack: " + character.getStatValue(Stat.MELEE_ATTACK) + (classesPlugin.getLevel(character) < classesPlugin.getClass(character).getMaxLevel() ? ChatColor.GRAY + " (Next level: +" + getStatIncrease(classesPlugin.getClass(character), Stat.MELEE_ATTACK, classesPlugin.getLevel(character)) + ")" : "") + (character instanceof CharacterImpl ? ChatColor.GRAY + " (Stat points: " + ((CharacterImpl) character).getStatPoints(Stat.MELEE_ATTACK) + ")" : ""));
                sender.sendMessage(ChatColor.GREEN + "Melee defence: " + character.getStatValue(Stat.MELEE_DEFENCE) + (classesPlugin.getLevel(character) < classesPlugin.getClass(character).getMaxLevel() ? ChatColor.GRAY + " (Next level: +" + getStatIncrease(classesPlugin.getClass(character), Stat.MELEE_DEFENCE, classesPlugin.getLevel(character)) + ")" : "") + (character instanceof CharacterImpl ? ChatColor.GRAY + " (Stat points: " + ((CharacterImpl) character).getStatPoints(Stat.MELEE_DEFENCE) + ")" : ""));
                sender.sendMessage(ChatColor.GREEN + "Ranged attack: " + character.getStatValue(Stat.RANGED_ATTACK) + (classesPlugin.getLevel(character) < classesPlugin.getClass(character).getMaxLevel() ? ChatColor.GRAY + " (Next level: +" + getStatIncrease(classesPlugin.getClass(character), Stat.RANGED_ATTACK, classesPlugin.getLevel(character)) + ")" : "") + (character instanceof CharacterImpl ? ChatColor.GRAY + " (Stat points: " + ((CharacterImpl) character).getStatPoints(Stat.RANGED_ATTACK) + ")" : ""));
                sender.sendMessage(ChatColor.GREEN + "Ranged defence: " + character.getStatValue(Stat.RANGED_DEFENCE) + (classesPlugin.getLevel(character) < classesPlugin.getClass(character).getMaxLevel() ? ChatColor.GRAY + " (Next level: +" + getStatIncrease(classesPlugin.getClass(character), Stat.RANGED_DEFENCE, classesPlugin.getLevel(character)) + ")" : "") + (character instanceof CharacterImpl ? ChatColor.GRAY + " (Stat points: " + ((CharacterImpl) character).getStatPoints(Stat.RANGED_DEFENCE) + ")" : ""));
                sender.sendMessage(ChatColor.GREEN + "Magic attack: " + character.getStatValue(Stat.MAGIC_ATTACK) + (classesPlugin.getLevel(character) < classesPlugin.getClass(character).getMaxLevel() ? ChatColor.GRAY + " (Next level: +" + getStatIncrease(classesPlugin.getClass(character), Stat.MAGIC_ATTACK, classesPlugin.getLevel(character)) + ")" : "") + (character instanceof CharacterImpl ? ChatColor.GRAY + " (Stat points: " + ((CharacterImpl) character).getStatPoints(Stat.MAGIC_ATTACK) + ")" : ""));
                sender.sendMessage(ChatColor.GREEN + "Magic defence: " + character.getStatValue(Stat.MAGIC_DEFENCE) + (classesPlugin.getLevel(character) < classesPlugin.getClass(character).getMaxLevel() ? ChatColor.GRAY + " (Next level: +" + getStatIncrease(classesPlugin.getClass(character), Stat.MAGIC_DEFENCE, classesPlugin.getLevel(character)) + ")" : "") + (character instanceof CharacterImpl ? ChatColor.GRAY + " (Stat points: " + ((CharacterImpl) character).getStatPoints(Stat.MAGIC_DEFENCE) + ")" : ""));
                sender.sendMessage(ChatColor.GREEN + "Speed: " + character.getStatValue(Stat.SPEED) + (classesPlugin.getLevel(character) < classesPlugin.getClass(character).getMaxLevel() ? ChatColor.GRAY + " (Next level: +" + getStatIncrease(classesPlugin.getClass(character), Stat.SPEED, classesPlugin.getLevel(character)) + ")" : "") + (character instanceof CharacterImpl ? ChatColor.GRAY + " (Stat points: " + ((CharacterImpl) character).getStatPoints(Stat.SPEED) + ")" : ""));
                if (character instanceof CharacterImpl) sender.sendMessage(ChatColor.GRAY + "Unassigned stat points: " + ((CharacterImpl) character).getUnassignedStatPoints());
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to perform this command.");
        }
        return true;
    }

    private int getStatIncrease(Class clazz, Stat stat, int level) {
        return getStatIncrease(clazz, stat, level, level + 1);
    }

    private int getStatIncrease(Class clazz, Stat stat, int startLevel, int endLevel) {
        return getStatAtLevel(clazz, stat, endLevel) - getStatAtLevel(clazz, stat, startLevel);
    }

    private int getStatAtLevel(Class clazz, Stat stat, int level) {
        return (int) Math.round((((150D + (20D * (double) clazz.getStatBonus(stat))) * (double) level) / 100D) + 5D);
    }

    private int getHpIncrease(Class clazz, int level) {
        return getHpIncrease(clazz, level, level + 1);
    }

    private int getHpIncrease(Class clazz, int startLevel, int endLevel) {
        return getHpAtLevel(clazz, endLevel) - getHpAtLevel(clazz, startLevel);
    }

    private int getHpAtLevel(Class clazz, int level) {
        return (int) Math.round((((250D + (20D * clazz.getHpBonus())) * (double) level) / 100D) + 10D);
    }

    private int getManaIncrease(Class clazz, int level) {
        return getManaIncrease(clazz, level, level + 1);
    }

    private int getManaIncrease(Class clazz, int startLevel, int endLevel) {
        return getManaAtLevel(clazz, endLevel) - getManaAtLevel(clazz, startLevel);
    }

    private int getManaAtLevel(Class clazz, int level) {
        return (int) Math.round((((250D + (20D * clazz.getManaBonus())) * (double) level) / 100D) + 10D);
    }

}
