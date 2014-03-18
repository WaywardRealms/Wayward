package net.wayward_realms.waywardunconsciousness;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WakeCommand implements CommandExecutor {

    private WaywardUnconsciousness plugin;

    public WakeCommand(WaywardUnconsciousness plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("wayward.unconsciousness.command.wake")) {
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (args.length > 0) {
                if (plugin.getServer().getPlayer(args[0]) != null) {
                    player = Bukkit.getServer().getPlayer(args[0]);
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Could not find a player by that name, defaulting to self.");
                }
            }
            if (player != null) {
                plugin.setUnconscious(player, false);
                sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Forcefully woke " + player.getDisplayName());
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify a player to wake");
            }
        }
        return true;
    }

}
