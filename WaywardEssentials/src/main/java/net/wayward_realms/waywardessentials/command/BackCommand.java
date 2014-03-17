package net.wayward_realms.waywardessentials.command;

import net.wayward_realms.waywardessentials.WaywardEssentials;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BackCommand implements CommandExecutor {

    private WaywardEssentials plugin;

    public BackCommand(WaywardEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (sender.hasPermission("wayward.essentials.command.back")) {
                Player player = (Player) sender;
                if (plugin.getPreviousLocation(player) != null) {
                    player.teleport(plugin.getPreviousLocation(player));
                    sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Teleported to previous location.");
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You have not teleported within the last session.");
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to perform this command.");
        }
        return true;
    }
}
