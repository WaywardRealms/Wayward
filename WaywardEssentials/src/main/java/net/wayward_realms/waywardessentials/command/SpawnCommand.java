package net.wayward_realms.waywardessentials.command;

import net.wayward_realms.waywardessentials.WaywardEssentials;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {

    private WaywardEssentials plugin;

    public SpawnCommand(WaywardEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("wayward.essentials.command.spawn")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.teleport(player.getWorld().getSpawnLocation());
                sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Teleported.");
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to send this command.");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You don't have permission.");
        }
        return true;
    }

}
