package net.wayward_realms.waywardclasses;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetLevelCommand implements CommandExecutor {

    private WaywardClasses plugin;

    public GetLevelCommand(WaywardClasses plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "The use of this command is discouraged, it will be removed in future. Use '/class info' instead");
        Player player = (Player) sender;
        if (sender.hasPermission("wayward.classes.command.getlevel")) {
            if (args.length >= 1) {
                if (plugin.getServer().getPlayer(args[0]) != null) {
                    player = plugin.getServer().getPlayer(args[0]);
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That player is not online!");
                    return true;
                }
            }
        }
        sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + player.getDisplayName() + "'s level is " + plugin.getLevel(player));
        if (plugin.getLevel(player) < plugin.getClass(player).getMaxLevel()) {
            sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Progress towards next level: " + plugin.getExperienceTowardsNextLevel(player) + "/" + plugin.getExpToNextLevel(plugin.getLevel(player)));
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.YELLOW + "Maximum level!");
        }
        return true;
    }

}
