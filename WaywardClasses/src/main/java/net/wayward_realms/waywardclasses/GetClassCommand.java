package net.wayward_realms.waywardclasses;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetClassCommand implements CommandExecutor {

    private WaywardClasses plugin;

    public GetClassCommand(WaywardClasses plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "The use of this command is discouraged, it will be removed in future. Use '/class info' instead");
        Player player = (Player) sender;
        if (sender.hasPermission("wayward.classes.command.getclass")) {
            if (args.length >= 1) {
                if (plugin.getServer().getPlayer(args[0]) != null) {
                    player = plugin.getServer().getPlayer(args[0]);
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That player is not online!");
                    return true;
                }
            }
        }

        if (plugin.getClass(player) != null) {
            sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + player.getDisplayName() + "'s class is " + plugin.getClass(player).getName());
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + player.getDisplayName() + " has not chosen a class!");
        }
        return true;
    }

}
