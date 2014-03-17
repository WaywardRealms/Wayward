package net.wayward_realms.waywardlocks;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetKeyCommand implements CommandExecutor {

    private WaywardLocks plugin;

    public GetKeyCommand(WaywardLocks plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("wayward.locks.command.getkey")) {
            if (sender instanceof Player) {
                plugin.setGettingKey((Player) sender, true);
                sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Click the block you want to get the key for.");
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to get keys.");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
        }
        return true;
    }

}
