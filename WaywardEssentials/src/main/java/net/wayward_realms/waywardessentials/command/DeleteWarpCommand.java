package net.wayward_realms.waywardessentials.command;

import net.wayward_realms.waywardessentials.WaywardEssentials;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeleteWarpCommand implements CommandExecutor {

    private WaywardEssentials plugin;

    public DeleteWarpCommand(WaywardEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("wayward.essentials.command.deletewarp")) {
            if (sender instanceof Player) {
                if (args.length >= 1) {
                    if (plugin.getWarps().containsKey(args[0].toLowerCase())) {
                        plugin.removeWarp(args[0].toLowerCase());
                        sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Deleted warp " + args[0].toLowerCase());
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify the name of the warp.");
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to use this command.");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
        }
        return true;
    }
}
