package net.wayward_realms.waywardmoderation;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VanishCommand implements CommandExecutor {

    private WaywardModeration plugin;

    public VanishCommand(WaywardModeration plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("wayward.moderation.command.vanish")) {
            if (sender instanceof Player) {
                if (!plugin.isVanished((Player) sender)) {
                    plugin.setVanished((Player) sender, true);
                    sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Vanished.");
                } else {
                    plugin.setVanished((Player) sender, false);
                    sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Unvanished.");
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player in order to vanish.");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
        }
        return true;
    }

}
