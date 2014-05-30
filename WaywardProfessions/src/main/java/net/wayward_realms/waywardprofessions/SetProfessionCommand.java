package net.wayward_realms.waywardprofessions;

import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.professions.ToolType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class SetProfessionCommand implements CommandExecutor {

    private final WaywardProfessions plugin;

    public SetProfessionCommand(WaywardProfessions plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length > 0) {
                RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                if (characterPluginProvider != null) {
                    CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                    ConfigurationSection professionSection = plugin.getConfig().getConfigurationSection("professions." + args[0].toLowerCase());
                    if (professionSection != null) {
                        plugin.reset(characterPlugin.getActiveCharacter((Player) sender));
                        ConfigurationSection mineSection = professionSection.getConfigurationSection("mine");
                        if (mineSection != null) {
                            for (String key : mineSection.getKeys(false)) {
                                plugin.setMiningEfficiency(characterPlugin.getActiveCharacter((Player) sender), Material.matchMaterial(key), mineSection.getInt(key));
                            }
                        }
                        ConfigurationSection craftSection = professionSection.getConfigurationSection("craft");
                        if (craftSection != null) {
                            for (String key : craftSection.getKeys(false)) {
                                plugin.setCraftEfficiency(characterPlugin.getActiveCharacter((Player) sender), Material.matchMaterial(key), craftSection.getInt(key));
                            }
                        }
                        if (professionSection.get("brew") != null) {
                            plugin.setBrewingEfficiency(characterPlugin.getActiveCharacter((Player) sender), professionSection.getInt("brew"));
                        }
                        ConfigurationSection maxDurabilitySection = professionSection.getConfigurationSection("max-durability");
                        if (maxDurabilitySection != null) {
                            for (String key : maxDurabilitySection.getKeys(false)) {
                                plugin.setMaxToolDurability(characterPlugin.getActiveCharacter((Player) sender), ToolType.valueOf(key.toUpperCase().replace(" ", "_")), maxDurabilitySection.getInt(key));
                            }
                        }
                        sender.sendMessage(ChatColor.GREEN + "Assigned profession preset " + args[0].toLowerCase());
                    } else {
                        sender.sendMessage(ChatColor.RED + "That profession does not exist.");
                    }
                }
            } else {
                sender.sendMessage(ChatColor.RED + "You must specify a profession to set.");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You must be a player to perform this command.");
        }
        return true;
    }

}
