package net.wayward_realms.waywardessentials.command;

import net.wayward_realms.waywardessentials.WaywardEssentials;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InventoryCommand implements CommandExecutor {

    private WaywardEssentials plugin;

    public InventoryCommand(WaywardEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("wayward.essentials.command.inventory")) {
            if (args.length >= 1) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (plugin.getServer().getPlayer(args[0]) != null) {
                        Player target = plugin.getServer().getPlayer(args[0]);
                        player.openInventory(target.getInventory());
                        sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Viewing " + target.getName() + "'s inventory.");
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That player is not online.");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to use this command.");
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify a player.");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
        }
        return true;
    }

}
