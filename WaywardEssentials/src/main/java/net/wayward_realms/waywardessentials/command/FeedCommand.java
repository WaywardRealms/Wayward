package net.wayward_realms.waywardessentials.command;

import net.wayward_realms.waywardessentials.WaywardEssentials;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class FeedCommand implements CommandExecutor {

    private WaywardEssentials plugin;

    public FeedCommand(WaywardEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("wayward.essentials.command.feed")) {
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
                player.setFoodLevel(20);
                player.setSaturation(10);
                RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                if (characterPluginProvider != null) {
                    CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                    characterPlugin.getActiveCharacter(player).setFoodLevel(20);
                }
                player.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Hunger refilled.");
                sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + player.getName() + "'s hunger was refilled.");
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "When using from console, you must also specify a player to feed.");
            }
        }
        return true;
    }

}
