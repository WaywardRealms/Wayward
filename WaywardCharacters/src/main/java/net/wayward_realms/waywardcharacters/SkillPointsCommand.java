package net.wayward_realms.waywardcharacters;

import net.wayward_realms.waywardlib.classes.ClassesPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import static net.wayward_realms.waywardlib.skills.SkillType.*;

public class SkillPointsCommand implements CommandExecutor {

    private WaywardCharacters plugin;

    public SkillPointsCommand(WaywardCharacters plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            net.wayward_realms.waywardlib.character.Character character = plugin.getActiveCharacter(player);
            RegisteredServiceProvider<ClassesPlugin> classesPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(ClassesPlugin.class);
            if (classesPluginProvider != null) {
                ClassesPlugin classesPlugin = classesPluginProvider.getProvider();
                sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + character.getName() + "'s skill points");
                sender.sendMessage(ChatColor.GREEN + "Melee offence: " + character.getSkillPoints(MELEE_OFFENCE) + (classesPlugin.getLevel(character) < classesPlugin.getClass(character).getMaxLevel() ? ChatColor.GRAY + " (Next level: +" + classesPlugin.getClass(character).getSkillPointBonus(MELEE_OFFENCE) + ")" : ""));
                sender.sendMessage(ChatColor.GREEN + "Melee defence: " + character.getSkillPoints(MELEE_DEFENCE) + (classesPlugin.getLevel(character) < classesPlugin.getClass(character).getMaxLevel() ? ChatColor.GRAY + " (Next level: +" + classesPlugin.getClass(character).getSkillPointBonus(MELEE_DEFENCE) + ")" : ""));
                sender.sendMessage(ChatColor.GREEN + "Ranged offence: " + character.getSkillPoints(RANGED_OFFENCE) + (classesPlugin.getLevel(character) < classesPlugin.getClass(character).getMaxLevel() ? ChatColor.GRAY + " (Next level: +" + classesPlugin.getClass(character).getSkillPointBonus(RANGED_OFFENCE) + ")" : ""));
                sender.sendMessage(ChatColor.GREEN + "Ranged defence: " + character.getSkillPoints(RANGED_DEFENCE) + (classesPlugin.getLevel(character) < classesPlugin.getClass(character).getMaxLevel() ? ChatColor.GRAY + " (Next level: +" + classesPlugin.getClass(character).getSkillPointBonus(RANGED_DEFENCE) + ")" : ""));
                sender.sendMessage(ChatColor.GREEN + "Magic offence: " + character.getSkillPoints(MAGIC_OFFENCE) + (classesPlugin.getLevel(character) < classesPlugin.getClass(character).getMaxLevel() ? ChatColor.GRAY + " (Next level: +" + classesPlugin.getClass(character).getSkillPointBonus(MAGIC_OFFENCE) + ")" : ""));
                sender.sendMessage(ChatColor.GREEN + "Magic defence: " + character.getSkillPoints(MAGIC_DEFENCE) + (classesPlugin.getLevel(character) < classesPlugin.getClass(character).getMaxLevel() ? ChatColor.GRAY + " (Next level: +" + classesPlugin.getClass(character).getSkillPointBonus(MAGIC_DEFENCE) + ")" : ""));
                sender.sendMessage(ChatColor.GREEN + "Magic healing: " + character.getSkillPoints(MAGIC_HEALING) + (classesPlugin.getLevel(character) < classesPlugin.getClass(character).getMaxLevel() ? ChatColor.GRAY + " (Next level: +" + classesPlugin.getClass(character).getSkillPointBonus(MAGIC_HEALING) + ")" : ""));
                sender.sendMessage(ChatColor.GREEN + "Magic nature: " + character.getSkillPoints(MAGIC_NATURE) + (classesPlugin.getLevel(character) < classesPlugin.getClass(character).getMaxLevel() ? ChatColor.GRAY + " (Next level: +" + classesPlugin.getClass(character).getSkillPointBonus(MAGIC_NATURE) + ")" : ""));
                sender.sendMessage(ChatColor.GREEN + "Magic illusion: " + character.getSkillPoints(MAGIC_ILLUSION) + (classesPlugin.getLevel(character) < classesPlugin.getClass(character).getMaxLevel() ? ChatColor.GRAY + " (Next level: +" + classesPlugin.getClass(character).getSkillPointBonus(MAGIC_ILLUSION) + ")" : ""));
                sender.sendMessage(ChatColor.GREEN + "Magic summoning: " + character.getSkillPoints(MAGIC_SUMMONING) + (classesPlugin.getLevel(character) < classesPlugin.getClass(character).getMaxLevel() ? ChatColor.GRAY + " (Next level: +" + classesPlugin.getClass(character).getSkillPointBonus(MAGIC_SUMMONING) + ")" : ""));
                sender.sendMessage(ChatColor.GREEN + "Magic sword: " + character.getSkillPoints(MAGIC_SWORD) + (classesPlugin.getLevel(character) < classesPlugin.getClass(character).getMaxLevel() ? ChatColor.GRAY + " (Next level: +" + classesPlugin.getClass(character).getSkillPointBonus(MAGIC_SWORD) + ")" : ""));
                sender.sendMessage(ChatColor.GREEN + "Speed nimble: " + character.getSkillPoints(SPEED_NIMBLE) + (classesPlugin.getLevel(character) < classesPlugin.getClass(character).getMaxLevel() ? ChatColor.GRAY + " (Next level: +" + classesPlugin.getClass(character).getSkillPointBonus(SPEED_NIMBLE) + ")" : ""));
                sender.sendMessage(ChatColor.GREEN + "Support perform: " + character.getSkillPoints(SUPPORT_PERFORM) + (classesPlugin.getLevel(character) < classesPlugin.getClass(character).getMaxLevel() ? ChatColor.GRAY + " (Next level: +" + classesPlugin.getClass(character).getSkillPointBonus(SUPPORT_PERFORM) + ")" : ""));
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to perform this command.");
        }
        return true;
    }
}
