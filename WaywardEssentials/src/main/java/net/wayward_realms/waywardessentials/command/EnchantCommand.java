package net.wayward_realms.waywardessentials.command;

import net.wayward_realms.waywardessentials.WaywardEssentials;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

public class EnchantCommand implements CommandExecutor {

    private WaywardEssentials plugin;

    public EnchantCommand(WaywardEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("wayward.essentials.command.enchant")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInMainHand().getType() != Material.AIR) {
                    if (args.length >= 2) {
                        if (sender.hasPermission("wayward.essentials.command.enchant.unsafe")) {
                            if (Enchantment.getByName(args[0].toUpperCase()) != null) {
                                try {
                                    player.getInventory().getItemInMainHand().addUnsafeEnchantment(Enchantment.getByName(args[0].toUpperCase()), Integer.parseInt(args[1]));
                                    sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Successfully enchanted " + player.getInventory().getItemInMainHand().getAmount() + " x " + player.getInventory().getItemInMainHand().getType() + " with " + Enchantment.getByName(args[0].toUpperCase()).getName() + " " + Integer.parseInt(args[1]));
                                } catch (NumberFormatException exception) {
                                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "The enchantment level must be a number");
                                } catch (IllegalArgumentException exception) {
                                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Illegal enchantment!");
                                }
                            } else {
                                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That enchantment does not exist.");
                            }
                        } else {
                            if (Enchantment.getByName(args[0].toUpperCase()) != null) {
                                try {
                                    player.getInventory().getItemInMainHand().addEnchantment(Enchantment.getByName(args[0].toUpperCase()), Integer.parseInt(args[1]));
                                    sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Successfully enchanted " + player.getInventory().getItemInMainHand().getAmount() + " x " + player.getInventory().getItemInMainHand().getType() + " with " + Enchantment.getByName(args[0].toUpperCase()).getName() + " " + Integer.parseInt(args[1]));
                                } catch (NumberFormatException exception) {
                                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "The enchantment level must be a number");
                                } catch (IllegalArgumentException exception) {
                                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Illegal enchantment!");
                                }
                            } else {
                                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That enchantment does not exist.");
                            }
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /enchant [enchantment] [level]");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be holding something to enchant it.");
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "This command cannot be used from console.");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
        }
        return true;
    }

}
