package net.wayward_realms.waywardprofessions;

import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.professions.ToolType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class ProfessionCommand implements CommandExecutor {
    private final WaywardProfessions plugin;

    public ProfessionCommand(WaywardProfessions plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("wayward.professions.command.profession")) {
            Player player = null;
            net.wayward_realms.waywardlib.character.Character character = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            int argsOffset = 0;
            if (args.length > 0) {
                if (plugin.getServer().getPlayer(args[argsOffset]) != null) {
                    player = plugin.getServer().getPlayer(args[argsOffset]);
                    argsOffset += 1;
                }
            }
            if (player != null) {
                RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                if (characterPluginProvider != null) {
                    CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                    character = characterPlugin.getActiveCharacter(player);
                }
                if (character != null) {
                    if (args.length > 1) {
                        if (args.length > 2 && !args[argsOffset].equalsIgnoreCase("brewefficiency")) {
                            if (Material.matchMaterial(args[argsOffset + 1]) != null) {
                                Material material = Material.matchMaterial(args[argsOffset + 1]);
                                if (args[argsOffset].equalsIgnoreCase("craftefficiency")) {
                                    try {
                                        plugin.setCraftEfficiency(character, material, plugin.getCraftEfficiency(character, material) + Integer.parseInt(args[argsOffset + 2]));
                                        sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + (character.isNameHidden() ? ChatColor.MAGIC + character.getName() + ChatColor.RESET : character.getName()) + ChatColor.GREEN + "'s crafting efficiency for " + material.toString().toLowerCase().replace('_', ' ') + " increased by " + args[argsOffset + 2] + "%" + ChatColor.GRAY + " (Total: " + plugin.getCraftEfficiency(character, material) + "%)");
                                    } catch (NumberFormatException exception) {
                                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify a number for efficiency to add.");
                                    }
                                } else if (args[argsOffset].equalsIgnoreCase("maxdurability")) {
                                    if (ToolType.getToolType(material) != null) {
                                        ToolType toolType = ToolType.getToolType(material);
                                        try {
                                            plugin.setMaxToolDurability(character, toolType, plugin.getMaxToolDurability(character, toolType) + Integer.parseInt(args[argsOffset + 2]));
                                            sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + (character.isNameHidden() ? ChatColor.MAGIC + character.getName() + ChatColor.RESET : character.getName()) + ChatColor.GREEN + "'s maximum durability for " + toolType.toString().toLowerCase().replace('_', ' ') + "s increased by " + args[argsOffset + 2] + ChatColor.GRAY + " (Total: " + plugin.getMaxToolDurability(character, toolType) + ")");
                                        } catch (NumberFormatException exception) {
                                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify a number for durability to add.");
                                        }
                                    } else {
                                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That material does not have durability!");
                                    }
                                } else if (args[argsOffset].equalsIgnoreCase("mineefficiency")) {
                                    try {
                                        plugin.setMiningEfficiency(character, material, plugin.getMiningEfficiency(character, material) + Integer.parseInt(args[argsOffset + 2]));
                                        sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + (character.isNameHidden() ? ChatColor.MAGIC + character.getName() + ChatColor.RESET : character.getName()) + ChatColor.GREEN + "'s mining efficiency for " + material.toString().toLowerCase().replace('_', ' ') + " increased by " + args[argsOffset + 2] + "%" + ChatColor.GRAY + "(Total: " + plugin.getMiningEfficiency(character, material) + "%)");
                                    } catch (NumberFormatException exception) {
                                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify a number for efficiency to add.");
                                    }
                                }
                            } else {
                                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Could not find that material!");
                            }
                        } else if (args[argsOffset].equalsIgnoreCase("brewefficiency")) {
                            try {
                                plugin.setBrewingEfficiency(character, plugin.getBrewingEfficiency(character) + Integer.parseInt(args[argsOffset + 1]));
                                sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Brewing efficiency: " + plugin.getBrewingEfficiency(character) + "%");
                            } catch (NumberFormatException exception) {
                                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify a number for efficiency to add.");
                            }
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify a material for efficiency unless you are getting brewing efficiency!");
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Possible arguments include \"craftefficiency\", \"maxdurability\", \"mineefficiency\", or \"brewefficiency\".");
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify a skill to check efficiency of! Possible arguments include \"craftefficiency\", \"mineefficiency\" or \"brewefficiency\"");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "No character plugin detected!");
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
