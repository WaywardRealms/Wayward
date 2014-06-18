package net.wayward_realms.waywardmoderation;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TempBanCommand implements CommandExecutor {

    private WaywardModeration plugin;

    public TempBanCommand(WaywardModeration plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("wayward.moderation.command.tempban")) {
            if (args.length >= 2) {
                Player player = plugin.getServer().getPlayer(args[0]);
                if (player != null) {
                    try {
                        int hours = Integer.parseInt(args[1]);
                        long millis = hours * 3600000;
                        plugin.tempBan(player, millis);
                        player.kickPlayer(ChatColor.RED + "You have been temporarily banned for " + hours + " hours.");
                        sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Temporarily banned " + player.getName() + " for " + hours + " hours.");
                    } catch (NumberFormatException exception) {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /tempban [player] [hours]");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That player is not online.");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Usage: /tempban [player] [hours]");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You do not have permission.");
        }
        return true;
    }
}
