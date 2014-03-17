package net.wayward_realms.waywardpermissions;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class SetGroupCommand implements CommandExecutor {

    private WaywardPermissions plugin;

    public SetGroupCommand(WaywardPermissions plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("wayward.permissions.command.setgroup")) {
            if (args.length >= 2) {
                if (plugin.getServer().getPlayer(args[0]) != null) {
                    if (plugin.getConfig().getConfigurationSection("groups").contains(args[1])) {
                        List<String> groups = new ArrayList<>();
                        groups.add(args[1]);
                        plugin.getConfig().set("users/" + plugin.getServer().getPlayer(args[0]).getName(), groups);
                        plugin.saveConfig();
                        plugin.assignPermissions(plugin.getServer().getPlayer(args[0]));
                        sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + plugin.getServer().getPlayer(args[0]).getName() + " was set to the group " + args[1]);
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That group does not exist.");
                    }
                } else {
                    if (plugin.getConfig().getConfigurationSection("groups").contains(args[1])) {
                        List<String> groups = new ArrayList<>();
                        groups.add(args[1]);
                        plugin.getConfig().set("users/" + plugin.getServer().getOfflinePlayer(args[0]).getName(), groups);
                        plugin.saveConfig();
                        sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + plugin.getServer().getOfflinePlayer(args[0]).getName() + " was set to the group " + args[1]);
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That group does not exist.");
                    }
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /setgroup [player] [group]");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
        }
        return true;
    }

}
