package net.wayward_realms.waywardessentials.command;

import net.wayward_realms.waywardessentials.WaywardEssentials;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CloneCommand implements CommandExecutor {

    private final WaywardEssentials plugin;

    public CloneCommand(WaywardEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("wayward.essentials.command.clone")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.getItemInHand() != null) {
                    player.getInventory().addItem(new ItemStack(player.getItemInHand()));
                    player.updateInventory();
                    sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Item cloned.");
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You are not holding any item.");
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to perform this command.");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
        }
        return true;
    }
}
