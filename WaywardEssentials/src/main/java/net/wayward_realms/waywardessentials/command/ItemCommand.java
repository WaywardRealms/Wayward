package net.wayward_realms.waywardessentials.command;

import net.wayward_realms.waywardessentials.WaywardEssentials;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemCommand implements CommandExecutor {

    private WaywardEssentials plugin;

    public ItemCommand(WaywardEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("wayward.essentials.command.item")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length >= 1) {
                    Material material = Material.matchMaterial(args[0]);
                    int amount = 1;
                    if (args.length >= 2) {
                        try {
                            amount = Integer.parseInt(args[1]);
                        } catch (NumberFormatException exception) {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "If you specify an amount, it must be an integer.");
                            return true;
                        }
                    }
                    if (material != null) {
                        ItemStack item = new ItemStack(material, amount);
                        player.getInventory().addItem(item);
                        if (amount > 1) {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Created " + amount + " " + material.toString() + "s");
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Created a " + material.toString() + ".");
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Could not find that material.");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify a material.");
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
