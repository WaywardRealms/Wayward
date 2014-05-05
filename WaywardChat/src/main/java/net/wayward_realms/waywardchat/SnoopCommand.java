package net.wayward_realms.waywardchat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SnoopCommand implements CommandExecutor {

    private WaywardChat plugin;

    public SnoopCommand(WaywardChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (sender.hasPermission("wayward.chat.command.snoop")) {
                plugin.setSnooping((Player) sender, !plugin.isSnooping((Player) sender));
                sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Toggled snooping.");
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission to do that.");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to use that command.");
        }
        return true;
    }
}
