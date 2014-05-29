package net.wayward_realms.waywardevents;

import net.wayward_realms.waywardlib.events.Dungeon;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DungeonCommand implements CommandExecutor {

    private WaywardEvents plugin;

    public DungeonCommand(WaywardEvents plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("create")) {
                if (sender.hasPermission("wayward.events.command.event.create")) {
                    if (args.length >= 3) {
                        Dungeon dungeon = new DungeonImpl();
                        dungeon.addDungeonMaster((Player) sender);
                        plugin.getDungeonManager().addDungeon(args[1], dungeon);
                        sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Dungeon " + args[1] + " successfully created");
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /dungeon create [name]");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
                }
            } else if (args[0].equalsIgnoreCase("modify")) {
                if (sender.hasPermission("wayward.events.command.dungeon.modify")) {
                    if (args.length >= 4) {
                        if (plugin.getDungeon(args[1]) != null) {
                            Dungeon dungeon = plugin.getDungeon(args[1]);
                            if (dungeon.getDungeonMasters().contains(sender) || sender.hasPermission("wayward.events.command.dungeon.modify.other")) {
                                if (args[2].equalsIgnoreCase("name")) {
                                    plugin.getDungeonManager().getDungeons().remove(args[1]);
                                    plugin.getDungeonManager().getDungeons().put(args[3], dungeon);
                                    sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Dungeon " + args[1] + " successfully renamed to " + args[3]);
                                } else if (args[2].equalsIgnoreCase("dungeonmasters")) {
                                    if (args.length >= 5) {
                                        if (args[3].equalsIgnoreCase("add")) {
                                            dungeon.addDungeonMaster(plugin.getServer().getOfflinePlayer(args[4]));
                                            sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Dungeon master " + args[4] + " successfully added to " + args[1]);
                                        } else if (args[3].equalsIgnoreCase("remove")) {
                                            dungeon.removeDungeonMaster(plugin.getServer().getOfflinePlayer(args[4]));
                                            sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Dungeon master " + args[4] + " successfully removed from " + args[1]);
                                        } else {
                                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /dungeon modify [dungeon] dungeonmasters [add|remove] [name]");
                                        }
                                    } else {
                                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /dungeon modify [dungeon] dungeonmasters [add|remove] [name]");
                                    }
                                } else if (args[2].equalsIgnoreCase("players")) {
                                    if (args.length >= 5) {
                                        if (args[3].equalsIgnoreCase("add")) {
                                            dungeon.addPlayer(plugin.getServer().getOfflinePlayer(args[4]));
                                            sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Player " + args[4] + " successfully added to " + args[1]);
                                        } else if (args[3].equalsIgnoreCase("remove")) {
                                            dungeon.removeDungeonMaster(plugin.getServer().getOfflinePlayer(args[4]));
                                            sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Player " + args[4] + " successfully removed from " + args[1]);
                                        } else {
                                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /dungeon modify [dungeon] players [add|remove] [name]");
                                        }
                                    } else {
                                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /dungeon modify [dungeon] players [add|remove] [name]");
                                    }
                                } else if (args[2].equalsIgnoreCase("active")) {
                                    if (args.length >= 4) {
                                        if (args[3].equalsIgnoreCase("true") || args[3].equalsIgnoreCase("t") || args[3].equalsIgnoreCase("yes") || args[3].equalsIgnoreCase("y")) {
                                            dungeon.setActive(true);
                                            sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Dungeon " + args[1] + " is now active!");
                                        } else if (args[3].equalsIgnoreCase("false") || args[3].equalsIgnoreCase("f") || args[3].equalsIgnoreCase("no") || args[3].equalsIgnoreCase("n")) {
                                            dungeon.setActive(false);
                                            sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Dungeon " + args[1] + " is now inactive.");
                                        } else {
                                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify a true/false value! (though some synonyms are accepted, e.g. yes/no)");
                                        }
                                    } else {
                                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /dungeon modify [dungeon] active [true|false]");
                                    }
                                } else {
                                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /dungeon modify [name|dungeonmasters|players|active|region]");
                                }
                            } else {
                                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
                            }
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Could not find that dungeon.");
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /dungeon modify [dungeon]");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /dungeon [create|modify]");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /dungeon [create|modify]");
        }
        return true;
    }

}
