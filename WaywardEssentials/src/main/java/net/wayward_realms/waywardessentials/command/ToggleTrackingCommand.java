package net.wayward_realms.waywardessentials.command;

import net.wayward_realms.waywardessentials.WaywardEssentials;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleTrackingCommand implements CommandExecutor {

    private WaywardEssentials plugin;

    public ToggleTrackingCommand(WaywardEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (sender.hasPermission("wayward.essentials.command.toggletracking")) {
                Player player = (Player) sender;
                plugin.setTrackable(player, !plugin.isTrackable(player));
                if (plugin.isTrackable(player)) {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "You have allowed people to find you with /track and /distance");
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You have disallowed people to find you with /track and /distance");
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission to do that.");
            }
        }
        return true;
    }
}
