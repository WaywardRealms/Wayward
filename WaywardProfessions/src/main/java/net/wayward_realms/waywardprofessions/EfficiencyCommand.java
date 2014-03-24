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

public class EfficiencyCommand implements CommandExecutor {

    private final WaywardProfessions plugin;

    public EfficiencyCommand(WaywardProfessions plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = null;
        net.wayward_realms.waywardlib.character.Character character = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        if (player != null) {
            RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
            if (characterPluginProvider != null) {
                CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                character = characterPlugin.getActiveCharacter(player);
            }
            if (character != null) {
                if (args.length > 0) {
                    if (args.length > 1 && !args[0].equalsIgnoreCase("brew")) {
                        if (Material.matchMaterial(args[1]) != null) {
                            Material material = Material.matchMaterial(args[0]);
                            if (args[0].equalsIgnoreCase("craft")) {
                                sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Crafting efficiency for " + material.toString().toLowerCase().replace('_', ' ') + ": " + plugin.getCraftEfficiency(character, material) + "%");
                                if (ToolType.getToolType(material) != null) {
                                    ToolType toolType = ToolType.getToolType(material);
                                    sender.sendMessage(ChatColor.GRAY + "(Maximum durability for " + toolType.toString().toLowerCase().replace('_', ' ') + "s: " + plugin.getMaxToolDurability(character, toolType) + ")");
                                }
                            } else if (args[0].equalsIgnoreCase("mine")) {
                                sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Mining efficiency for " + material.toString().toLowerCase().replace('_', ' ') + ": " + plugin.getMiningEfficiency(character, material) + "%");
                            }
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Could not find that material!");
                        }
                    } else if (args[0].equalsIgnoreCase("brew")) {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Brewing efficiency: " + plugin.getBrewingEfficiency(character) + "%");
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify a material for efficiency unless you are getting brewing efficiency!");
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Possible arguments include \"craft\", \"mine\", or \"brew\".");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify a skill to check efficiency of! Possible arguments include \"craft\", \"mine\" or \"brew\"");
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "No character plugin detected!");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to perform this command.");
        }
        return true;
    }
}
