package net.wayward_realms.waywardclasses;

import net.wayward_realms.waywardlib.classes.Class;
import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class SetClassCommand implements CommandExecutor {

    private WaywardClasses plugin;

    public SetClassCommand(WaywardClasses plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "The use of this command is discouraged, it will be removed in future. Use '/class set' instead");
        if (args.length >= 2 && sender.hasPermission("wayward.classes.command.setclass")) {
            if (plugin.getClass(args[1].toUpperCase()) != null) {
                Class clazz = plugin.getClass(args[1]);
                if (plugin.getServer().getPlayer(args[0]) != null) {
                    Player player = plugin.getServer().getPlayer(args[0]);
                    sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + player.getName() + "'s class set to " + clazz.getName());
                    plugin.setClass(player, clazz);
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That player is not online!");
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That's not a valid class!");
            }
        } else if (args.length >= 1) {
            if (plugin.getClass(args[0]) != null) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    RegisteredServiceProvider<CharacterPlugin> characterProvider = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                    if (characterProvider != null) {
                        CharacterPlugin characterPlugin = characterProvider.getProvider();
                        Character character = characterPlugin.getActiveCharacter(player);
                        Class clazz = plugin.getClass(args[0]);
                        if (clazz.hasPrerequisites(character)) {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Class set to " + clazz.getName());
                            plugin.setClass((Player) sender, clazz);
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have the prerequisites for that class.");
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Could not find a character plugin.");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to use this command.");
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That's not a valid class!");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You need to specify a class!");
        }
        return true;
    }

}
