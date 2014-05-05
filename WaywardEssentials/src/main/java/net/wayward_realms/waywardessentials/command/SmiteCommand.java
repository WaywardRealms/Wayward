package net.wayward_realms.waywardessentials.command;

import net.wayward_realms.waywardessentials.WaywardEssentials;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SmiteCommand implements CommandExecutor {

    private WaywardEssentials plugin;

    public SmiteCommand(WaywardEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.hasPermission("wayward.essentials.command.smite")) {
			if (args.length == 0) {
				sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Incorrect usage: /smite [Player]");
			} else {
				if (plugin.getServer().getPlayer(args[0]) == null) {
					sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Could not find a player by that name");
				} else {
					Player player = plugin.getServer().getPlayer(args[0]);
					World world = player.getWorld();
					Location location = player.getLocation();
					world.strikeLightning(location);
					player.setFireTicks(100);
					sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Opening the heavens on " + player.getName());
				}
			}
		} else {
			sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission");
		}
		return true;
	}
}