package net.wayward_realms.waywardessentials.command;

import net.wayward_realms.waywardessentials.WaywardEssentials;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetWarpCommand implements CommandExecutor {

    private WaywardEssentials plugin;

    public SetWarpCommand(WaywardEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("wayward.essentials.command.setwarp")) {
            if (sender instanceof Player) {
                if (args.length >= 1) {
                    Player player = (Player) sender;
                    plugin.addWarp(args[0], player.getLocation());
                    sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Set warp " + args[0].toLowerCase() + " in " + player.getWorld().getName() + " at " + player.getLocation().getBlockX() + ", " + player.getLocation().getBlockY() + ", " + player.getLocation().getBlockZ());
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
