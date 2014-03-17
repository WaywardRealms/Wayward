package net.wayward_realms.waywardessentials.command;

import net.wayward_realms.waywardessentials.WaywardEssentials;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RunAsCommand implements CommandExecutor {

    private WaywardEssentials plugin;

    public RunAsCommand(WaywardEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("wayward.essentials.command.runas")) {
            if (args.length >= 2) {
                if (plugin.getServer().getPlayer(args[0]) != null) {
                    Player player = plugin.getServer().getPlayer(args[0]);
                    StringBuilder commandToRun = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        commandToRun.append(args[i]).append(" ");
                    }
                    plugin.getServer().dispatchCommand(player, commandToRun.toString());
                    sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Dispatched command.");
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That player is not online.");
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify a player and a command.");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission");
        }
        return true;
    }

}
