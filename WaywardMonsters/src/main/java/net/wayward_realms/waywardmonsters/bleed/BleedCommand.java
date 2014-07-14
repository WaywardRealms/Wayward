package net.wayward_realms.waywardmonsters.bleed;

import net.wayward_realms.waywardmonsters.WaywardMonsters;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BleedCommand implements CommandExecutor {

    private WaywardMonsters plugin;

    public BleedCommand(WaywardMonsters plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("wayward.monsters.command.bleed")) {
            if (args.length > 0) {
                Player player = plugin.getServer().getPlayer(args[0]);
                if (player != null) {
                    plugin.bleed(player);
                    sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Made " + player.getName() + "/" + player.getDisplayName() + " bleed.");
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That player is not online.");
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify a player.");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
        }
        return true;
    }
}
