package net.wayward_realms.waywardmonsters;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ZeroPointCommand implements CommandExecutor {

    private WaywardMonsters plugin;

    public ZeroPointCommand(WaywardMonsters plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (sender.hasPermission("wayward.monsters.command.zeropoint")) {
                plugin.getEntityLevelManager().addZeroPoint(((Player) sender).getLocation());
                sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Zero point set.");
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission to perform that command.");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to perform this command.");
        }
        return true;
    }
}
