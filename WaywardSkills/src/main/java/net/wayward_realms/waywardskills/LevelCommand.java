package net.wayward_realms.waywardskills;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class LevelCommand implements CommandExecutor {

    private WaywardSkills plugin;

    public LevelCommand(WaywardSkills plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("info")) {
                Player player = null;
                if (sender instanceof Player) {
                    player = (Player) sender;
                }
                if (sender.hasPermission("wayward.skills.command.level.info")) {
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
                    RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                    if (characterPluginProvider != null) {
                        CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                        Character character = characterPlugin.getActiveCharacter(player);
                        sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + player.getDisplayName() + ChatColor.GREEN + " is currently lv" + plugin.getLevel(character));
                        sender.sendMessage(ChatColor.GRAY + " (Progress towards level " + (plugin.getLevel(character) + 1) + ": " + plugin.getExperience(character) + "/" + plugin.getExperienceForLevel(plugin.getLevel(character) + 1) + ")");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to perform this command.");
                }
            } else if (args[0].equalsIgnoreCase("setlevel")) {
                if (sender.hasPermission("wayward.skills.command.level.setlevel")) {
                    if (args.length >= 3) {
                        try {
                            Player player = plugin.getServer().getPlayer(args[1]);
                            if (player != null) {
                                RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                                if (characterPluginProvider != null) {
                                    CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                                    Character character = characterPlugin.getActiveCharacter(player);
                                    plugin.setLevel(character, Integer.parseInt(args[2]));
                                    sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + plugin.getServer().getPlayer(args[1]).getName() + "'s level was set to " + Integer.parseInt(args[2]));
                                }
                            } else {
                                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That player is not online!");
                            }
                        } catch (NumberFormatException exception) {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Level must be an integer!");
                        }
                    } else if (args.length >= 2) {
                        try {
                            RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                            if (characterPluginProvider != null) {
                                CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                                Character character = characterPlugin.getActiveCharacter((Player) sender);
                                plugin.setLevel(character, Integer.parseInt(args[1]));
                            }
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
            } else if (args[0].equalsIgnoreCase("addexp")) {
                if (sender.hasPermission("wayward.skills.command.level.addexp")) {
                    if (args.length >= 3) {
                        if (plugin.getServer().getPlayer(args[1]) != null) {
                            Player player = plugin.getServer().getPlayer(args[1]);
                            try {
                                RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                                if (characterPluginProvider != null) {
                                    CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                                    Character character = characterPlugin.getActiveCharacter(player);
                                    plugin.giveExperience(character, Integer.parseInt(args[2]));
                                }
                                sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Gave " + player.getName() + " " + Integer.parseInt(args[2]) + " exp");
                            } catch (NumberFormatException exception) {
                                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Amount of experience must be an integer.");
                            }
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That player is not online.");
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /" + label + " addexp [player] [amount]");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /" + label + " [set|info|list|setlevel|addexp]");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /" + label + " [set|info|list|setlevel|addexp]");
        }
        return true;
    }

}
