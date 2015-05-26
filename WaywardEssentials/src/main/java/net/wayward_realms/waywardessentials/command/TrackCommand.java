package net.wayward_realms.waywardessentials.command;

import net.wayward_realms.waywardessentials.WaywardEssentials;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TrackCommand implements CommandExecutor {

    private WaywardEssentials plugin;

    public TrackCommand(WaywardEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("wayward.essentials.command.track")) {
            if (sender instanceof Player) {
                if (args.length > 0) {
                    Player player = plugin.getServer().getPlayer(args[0]);
                    if (player != null) {
                        ((Player) sender).setCompassTarget(player.getLocation());
                        sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Now tracking " + player.getName() + "/" + player.getDisplayName());
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "No player by that name is online.");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify a player to track.");
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to perform this command.");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
        }
        return true;
    }

}
