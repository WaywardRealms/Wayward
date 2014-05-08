package net.wayward_realms.waywardchat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatGroupCommand implements CommandExecutor {

    private WaywardChat plugin;

    public ChatGroupCommand(WaywardChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("create")) {
                if (args.length >= 2) {
                    if (sender instanceof Player) {
                        if (plugin.getChatGroup(args[1]) == null) {
                            if (!args[1].startsWith("_pm_")) {
                                ChatGroup chatGroup = new ChatGroup(plugin, args[1], (Player) sender);
                                plugin.addChatGroup(chatGroup);
                                sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Chat group " + chatGroup.getName() + " created. Use /chatgroup invite " + chatGroup.getName() + " [player] to invite players.");
                            } else {
                                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Chat groups with the _pm_ prefix are reserved. Please use a different name.");
                            }
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "A chat group by that name already exists, try a different one.");
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to create a chat group.");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify the name of the chat group.");
                }
            } else if (args[0].equalsIgnoreCase("disband")) {
                if (args.length >= 2) {
                    if (sender instanceof Player) {
                        if (plugin.getChatGroup(args[1]) != null) {
                            for (String player : plugin.getChatGroup(args[1]).getPlayers()) {
                                plugin.getServer().getPlayerExact(player).sendMessage(plugin.getPrefix() + ChatColor.RED + "");
                            }
                            plugin.removeChatGroup(args[1]);
                            sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + args[1].toLowerCase() + " disbanded.");
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That chat group does not exist.");
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to perform that command.");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify the name of the chat group.");
                }
            } else if (args[0].equalsIgnoreCase("invite")) {
                if (args.length >= 3) {
                    if (plugin.getChatGroup(args[1]) != null) {
                        if (plugin.getChatGroup(args[1]).getPlayers().contains(sender.getName())) {
                            if (plugin.getServer().getPlayer(args[2]) != null) {
                                Player player = plugin.getServer().getPlayer(args[2]);
                                plugin.getChatGroup(args[1]).invitePlayer(player);
                                player.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "You have been invited to join the chat group " + args[1].toLowerCase() + ", use /chatgroup join " + args[1].toLowerCase() + " to join.");
                                sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Invited " + player.getName() + " to join " + args[1].toLowerCase());
                            } else {
                                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Could not find a player by that name.");
                            }
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You are not a member of that chat group");
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That chat group does not exist.");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify a chat group and a player to invite");
                }
            } else if (args[0].equalsIgnoreCase("join")) {
                if (args.length >= 2) {
                    if (plugin.getChatGroup(args[1]) != null) {
                        if (sender instanceof Player) {
                            if (plugin.getChatGroup(args[1]).isInvited((Player) sender)) {
                                plugin.getChatGroup(args[1]).addPlayer((Player) sender);
                                sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Joined " + args[1].toLowerCase());
                            } else {
                                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You have not been invited to that chat group.");
                            }
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to perform that command.");
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That chat group does not exist.");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify a chat group to join.");
                }
            } else if (args[0].equalsIgnoreCase("leave")) {
                if (args.length >= 2) {
                    if (plugin.getChatGroup(args[1]) != null) {
                        if (sender instanceof Player) {
                            if (plugin.getChatGroup(args[1]).getPlayers().contains(sender.getName())) {
                                plugin.getChatGroup(args[1]).removePlayer((Player) sender);
                                sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Left " + args[1].toLowerCase());
                            } else {
                                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You are not a member of that chat group.");
                            }
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to perform that command.");
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That chat group does not exist.");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify a chat group to leave.");
                }
            } else if (args[0].equalsIgnoreCase("message")) {
                if (args.length >= 3) {
                    if (plugin.getChatGroup(args[1]) != null) {
                        if (sender instanceof Player) {
                            if (plugin.getChatGroup(args[1]).getPlayers().contains(sender.getName())) {
                                StringBuilder message = new StringBuilder();
                                for (int i = 2; i < args.length; i++) {
                                    message.append(args[i]).append(" ");
                                }
                                plugin.sendPrivateMessage((Player) sender, plugin.getChatGroup(args[1]), message.toString());
                            } else {
                                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You are not a member of that chat group.");
                            }
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to perform that command.");
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That chat group does not exist.");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify a chat group and a message to send");
                }
            } else if (args[0].equalsIgnoreCase("players")) {
                if (args.length >= 2) {
                    if (plugin.getChatGroup(args[1]) != null) {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Members of " + args[1].toLowerCase() + ": ");
                        for (String player : plugin.getChatGroup(args[1]).getPlayers()) {
                            sender.sendMessage(ChatColor.GREEN + player);
                        }
                        sender.sendMessage(ChatColor.GREEN + "Pending invitations: ");
                        for (String player : plugin.getChatGroup(args[1]).getInvited()) {
                            sender.sendMessage(ChatColor.GREEN + player);
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That chat group does not exist.");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify a chat group");
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /chatgroup [create|disband|invite|join|leave|message|players]");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /chatgroup [create|disband|invite|join|leave|message|players]");
        }
        return true;
    }

}
