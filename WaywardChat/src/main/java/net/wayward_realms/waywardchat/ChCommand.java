package net.wayward_realms.waywardchat;

import net.wayward_realms.waywardlib.chat.Channel;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChCommand implements CommandExecutor {

    private WaywardChat plugin;

    public ChCommand(WaywardChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ch")) {
            if (args.length >= 2) {
                if (args[0].equalsIgnoreCase("talkin")) {
                    if (plugin.getChannel(args[1]) != null) {
                        if (sender.hasPermission("wayward.chat.ch.talkin." + args[1].toLowerCase())) {
                            plugin.setPlayerChannel((Player) sender, plugin.getChannel(args[1].toLowerCase()));
                            sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "You are now talking in " + args[1].toLowerCase() + "!");
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You don't have permission to talk in that channel!");
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "No channel by that name exists! Use /ch list for channels.");
                    }
                } else if (args[0].equalsIgnoreCase("listen")) {
                    if (plugin.getChannel(args[1]) != null) {
                        if (sender.hasPermission("wayward.chat.ch.listen." + args[1].toLowerCase())) {
                            if (!plugin.getChannel(args[1]).getListeners().contains(sender)) {
                                plugin.getChannel(args[1]).addListener((Player) sender);
                                sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "You are now listening to " + args[1].toLowerCase() + "!");
                            } else {
                                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You are already listening to that channel!");
                            }
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission to listen to that channel!");
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "No channel by that name exists! Use /ch list for channels.");
                    }
                } else if (args[0].equalsIgnoreCase("mute")) {
                    if (plugin.getChannel(args[1]) != null) {
                        if (sender.hasPermission("wayward.chat.ch.mute." + args[1].toLowerCase())) {
                            if (plugin.getChannel(args[1]).getListeners().contains(sender)) {
                                plugin.getChannel(args[1]).removeListener((Player) sender);
                                sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "You are no longer listening to " + args[1].toLowerCase() + "!");
                            } else {
                                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You are not listening to that channel!");
                            }
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission to mute that channel!");
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "No channel by that name exists! Use /ch list for channels.");
                    }
                }
            } else {
                if (args.length >= 1) {
                    if (args[0].equalsIgnoreCase("list")) {
                        if (sender.hasPermission("wayward.chat.ch.list")) {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.BLUE + "Channel list: ");
                            for (Channel channel : plugin.getChannels()) {
                                sender.sendMessage(channel.getColour() + channel.getName());
                                sender.sendMessage(channel.getColour() + " - Format: " + channel.getFormat());
                                sender.sendMessage(channel.getColour() + " - Radius: " + channel.getRadius());
                            }
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission to list channels!");
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Incorrect usage! Use /chathelp for help.");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Incorrect usage! Use /chathelp for help.");
                }
            }
            return true;
        }
        return false;
    }

}
