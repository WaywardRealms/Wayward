package net.wayward_realms.waywardessentials.command;

import net.wayward_realms.waywardessentials.WaywardEssentials;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemMetaCommand implements CommandExecutor {

    private WaywardEssentials plugin;

    public ItemMetaCommand(WaywardEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("wayward.essentials.command.itemmeta")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInMainHand().getType() != Material.AIR) {
                    if (args.length >= 2) {
                        ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
                        if (args[0].equalsIgnoreCase("setname")) {
                            StringBuilder nameBuilder = new StringBuilder();
                            for (int i = 1; i < args.length; i++) {
                                nameBuilder.append(args[i]);
                                if (i < args.length - 1) {
                                    nameBuilder.append(" ");
                                }
                            }
                            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', nameBuilder.toString()));
                        } else if (args[0].equalsIgnoreCase("addlore")) {
                            List<String> lore;
                            if (meta.hasLore()) {
                                lore = meta.getLore();
                            } else {
                                lore = new ArrayList<>();
                            }
                            StringBuilder loreBuilder = new StringBuilder();
                            for (int i = 1; i < args.length; i++) {
                                loreBuilder.append(args[i]);
                                if (i < args.length - 1) {
                                    loreBuilder.append(" ");
                                }
                            }
                            lore.add(ChatColor.translateAlternateColorCodes('&', loreBuilder.toString()));
                            sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Lore added: \"" + ChatColor.translateAlternateColorCodes('&', loreBuilder.toString()) + ChatColor.GREEN + "\"");
                            meta.setLore(lore);
                        } else if (args[0].equalsIgnoreCase("removelore")) {
                            if (meta.hasLore()) {
                                List<String> lore = meta.getLore();
                                StringBuilder loreBuilder = new StringBuilder();
                                for (int i = 1; i < args.length; i++) {
                                    loreBuilder.append(args[i]);
                                    if (i < args.length - 1) {
                                        loreBuilder.append(" ");
                                    }
                                }
                                if (lore.contains(ChatColor.translateAlternateColorCodes('&', loreBuilder.toString()))) {
                                    lore.remove(ChatColor.translateAlternateColorCodes('&', loreBuilder.toString()));
                                    sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Lore removed: \"" + ChatColor.translateAlternateColorCodes('&', loreBuilder.toString()) + ChatColor.GREEN + "\"");
                                } else {
                                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That item does not have that lore.");
                                }
                                meta.setLore(lore);
                            } else {
                                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That item does not have any lore.");
                            }
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /itemmeta [setname|addlore|removelore] [name|lore]");
                        }
                        player.getInventory().getItemInMainHand().setItemMeta(meta);
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /itemmeta [setname|addlore|removelore] [name|lore]");
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
