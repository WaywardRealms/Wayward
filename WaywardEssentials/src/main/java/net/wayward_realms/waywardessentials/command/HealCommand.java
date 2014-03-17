package net.wayward_realms.waywardessentials.command;

import net.wayward_realms.waywardessentials.WaywardEssentials;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.character.Character;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class HealCommand implements CommandExecutor {
    
    private WaywardEssentials plugin;

    public HealCommand(WaywardEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("wayward.essentials.command.heal")) {
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (args.length >= 1) {
                if (plugin.getServer().getPlayer(args[0]) != null) {
                    player = plugin.getServer().getPlayer(args[0]);
                }
            }
            if (player != null) {
                player.setHealth(player.getMaxHealth());
                player.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Healed.");
                sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + player.getName() + " was healed.");
                RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                if (characterPluginProvider != null) {
                    CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                    Character character = characterPlugin.getActiveCharacter(player);
                    character.setHealth(character.getMaxHealth());
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "When using from console, you must also specify a player to heal.");
            }
        }
        return true;
    }
    
}
