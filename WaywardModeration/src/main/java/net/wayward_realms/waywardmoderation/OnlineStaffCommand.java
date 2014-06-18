package net.wayward_realms.waywardmoderation;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OnlineStaffCommand implements CommandExecutor {

    private WaywardModeration plugin;

    public OnlineStaffCommand(WaywardModeration plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Online staff:");
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (player.hasPermission("wayward.moderation.staff")) {
                sender.sendMessage(ChatColor.GREEN + player.getName());
            }
        }
        return true;
    }

}
