package net.wayward_realms.waywardclasses;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.classes.Class;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class ClassCommand implements CommandExecutor {

    private final WaywardClasses plugin;

    public ClassCommand(WaywardClasses plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("set")) {
                if (args.length >= 3 && (sender.hasPermission("wayward.classes.command.setclass") || sender.hasPermission("wayward.classes.command.class.set"))) {
                    if (plugin.getClass(args[2].toUpperCase()) != null) {
                        Class clazz = plugin.getClass(args[2]);
                        if (plugin.getServer().getPlayer(args[1]) != null) {
                            Player player = plugin.getServer().getPlayer(args[1]);
                            sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + player.getName() + "'s class set to " + clazz.getName());
                            plugin.setClass(player, clazz);
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That player is not online!");
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That's not a valid class!");
                    }
                } else if (args.length >= 2) {
                    if (plugin.getClass(args[1]) != null) {
                        if (sender instanceof Player) {
                            Player player = (Player) sender;
                            RegisteredServiceProvider<CharacterPlugin> characterProvider = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                            if (characterProvider != null) {
                                CharacterPlugin characterPlugin = characterProvider.getProvider();
                                Character character = characterPlugin.getActiveCharacter(player);
                                Class clazz = plugin.getClass(args[1]);
                                if (clazz.hasPrerequisites(character)) {
                                    sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Class set to " + clazz.getName());
                                    plugin.setClass((Player) sender, clazz);
                                } else {
                                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have the prerequisites for that class.");
                                }
                            } else {
                                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Could not find a character plugin.");
                            }
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to use this command.");
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That's not a valid class!");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You need to specify a class!");
                }
            } else if (args[0].equalsIgnoreCase("info")) {
                Player player = null;
                if (sender instanceof Player) {
                    player = (Player) sender;
                }
                if ((sender.hasPermission("wayward.classes.command.getclass") && sender.hasPermission("wayward.classes.command.getlevel")) || sender.hasPermission("wayward.classes.command.class.info")) {
                    if (args.length >= 2) {
                        if (plugin.getServer().getPlayer(args[1]) != null) {
                            player = plugin.getServer().getPlayer(args[1]);
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That player is not online!");
                            return true;
                        }
                    }
                }
                if (player != null) {
                    if (plugin.getClass(player) != null) {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + player.getDisplayName() + ChatColor.GREEN + " is currently a lv" + plugin.getLevel(player) + " " +  plugin.getClass(player).getName());
                        sender.sendMessage(ChatColor.GRAY + " (Progress towards level " + (plugin.getLevel(player) + 1) + ": " + plugin.getExperienceTowardsNextLevel(player) + "/" + plugin.getExpToNextLevel(plugin.getLevel(player)) + ")");
                        sender.sendMessage(ChatColor.GREEN + "Other class levels: ");
                        for (Class clazz : plugin.getClasses()) {
                            if (plugin.getTotalExperience(player, clazz) > 0) {
                                sender.sendMessage(ChatColor.GREEN + clazz.getName() + " - lv" + plugin.getLevel(player, clazz) + ChatColor.GRAY + "(" + plugin.getExperienceTowardsNextLevel(player) + "/" + plugin.getExpToNextLevel(plugin.getLevel(player)) + " exp)");
                            }
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + player.getDisplayName() + ChatColor.RED + " has not chosen a class!");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to perform this command.");
                }
            } else if (args[0].equalsIgnoreCase("setlevel")) {
                if (sender.hasPermission("wayward.classes.command.setlevel") || sender.hasPermission("wayward.classes.command.class.setlevel")) {
                    if (args.length >= 3) {
                        try {
                            if (plugin.getServer().getPlayer(args[1]) != null) {
                                plugin.setLevel(plugin.getServer().getPlayer(args[1]), Integer.parseInt(args[2]));
                            } else {
                                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That player is not online!");
                            }
                        } catch (NumberFormatException exception) {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Level must be an integer!");
                        }
                    } else if (args.length >= 2) {
                        try {
                            plugin.setLevel((Player) sender, Integer.parseInt(args[1]));
                            sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Level set to " + args[1]);
                        } catch (NumberFormatException exception) {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Level must be a number!");
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You need to specify a level!");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission!");
                }
            } else if (args[0].equalsIgnoreCase("list")) {
                sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Available classes:");
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    RegisteredServiceProvider<CharacterPlugin> characterProvider = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                    if (characterProvider.getProvider() != null) {
                        CharacterPlugin characterPlugin = characterProvider.getProvider();
                        Character character = characterPlugin.getActiveCharacter(player);
                        for (Class clazz : plugin.getClasses()) {
                            sender.sendMessage((clazz.hasPrerequisites(character) ? ChatColor.GREEN : ChatColor.RED) + clazz.getName() + (clazz.hasPrerequisites(character) ? ChatColor.GRAY + " (" + ChatColor.GREEN + "Unlocked" + ChatColor.GRAY + ")" : ChatColor.GRAY + " (" + ChatColor.RED + "Locked" + ChatColor.GRAY + ")"));
                        }
                    }
                } else {
                    for (Class clazz : plugin.getClasses()) {
                        sender.sendMessage(ChatColor.GREEN + clazz.getName());
                    }
                }
            } else if (args[0].equalsIgnoreCase("addexp")) {
                if (sender.hasPermission("wayward.classes.command.addexp") || sender.hasPermission("wayward.classes.command.class.addexp")) {
                    if (args.length >= 3) {
                        if (plugin.getServer().getPlayer(args[1]) != null) {
                            Player player = plugin.getServer().getPlayer(args[1]);
                            try {
                                plugin.giveExperience(player, Integer.parseInt(args[2]));
                                sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Gave " + player.getName() + " " + Integer.parseInt(args[2]) + " exp");
                            } catch (NumberFormatException exception) {
                                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Amount of experience must be an integer.");
                            }
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That player is not online.");
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /class addexp [player] [amount]");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /class [set|info|list|setlevel|addexp]");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /class [set|info|list|setlevel|addexp]");
        }
        return true;
    }
}
