package net.wayward_realms.waywardevents;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RadiusEmoteCommand implements CommandExecutor {

    private net.wayward_realms.waywardevents.WaywardEvents plugin;

    public RadiusEmoteCommand(net.wayward_realms.waywardevents.WaywardEvents plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("wayward.events.command.radiusemote")) {
            if (args.length >= 2) {
                String message = "";

                for (int i = 1; i < args.length; ++i) {
                    message += args[i] + " ";
                }

                message = ChatColor.YELLOW + "" + ChatColor.ITALIC + ChatColor.BOLD + message.replaceAll("&", ChatColor.COLOR_CHAR + "");
                int radius = Integer.parseInt(args[0]);
                int radiusSquared = radius * radius;

                for (Player player : ((Player) sender).getLocation().getWorld().getPlayers()) {
                    if (((Player) sender).getLocation().distanceSquared(player.getLocation()) <= radiusSquared) {
                        player.sendMessage(message);
                    }
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /" + cmd.getName() + " [radius] [message]");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission!");
        }
        return true;
    }

}
