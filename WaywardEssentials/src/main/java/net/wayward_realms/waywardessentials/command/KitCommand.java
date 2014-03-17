package net.wayward_realms.waywardessentials.command;

import net.wayward_realms.waywardessentials.WaywardEssentials;
import net.wayward_realms.waywardlib.essentials.Kit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitCommand implements CommandExecutor {

    private WaywardEssentials plugin;

    public KitCommand(WaywardEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("wayward.essentials.command.kit")) {
            if (sender instanceof Player) {
                if (args.length >= 1) {
                    Player player = (Player) sender;
                    Kit kit = plugin.getKit(args[0]);
                    if (kit != null) {
                        kit.give(player);
                        sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Claimed a kit.");
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That kit does not exist.");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Kit list:");
                    for (String kitName : plugin.getKits().keySet()) {
                        sender.sendMessage(ChatColor.GREEN + kitName);
                    }
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to claim a kit.");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
        }
        return true;
    }

}
