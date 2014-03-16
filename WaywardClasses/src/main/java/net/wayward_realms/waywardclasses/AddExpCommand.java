package net.wayward_realms.waywardclasses;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddExpCommand implements CommandExecutor {

    private WaywardClasses plugin;

    public AddExpCommand(WaywardClasses plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "The use of this command is discouraged, it will be removed in future. Use '/class addexp' instead");
        if (sender.hasPermission("wayward.classes.command.addexp")) {
            if (plugin.getServer().getPlayer(args[0]) != null) {
                Player player = plugin.getServer().getPlayer(args[0]);
                try {
                    plugin.giveExperience(player, Integer.parseInt(args[1]));
                    sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Gave " + player.getName() + " " + Integer.parseInt(args[1]) + " exp");
                } catch (NumberFormatException exception) {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Amount of experience must be an integer.");
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That player is not online.");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
        }
        return true;
    }

}