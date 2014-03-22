package net.wayward_realms.waywardessentials.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SmiteCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.hasPermission("wayward.essentials.command.smite")) {
			if (args.length == 0) {
				sender.sendMessage(ChatColor.RED + "Incorrect usage: /smite [Player]");
			} else {
				if (Bukkit.getPlayer(args[0]) == null) {
					sender.sendMessage(ChatColor.RED + "Player not online");
				} else {
					Player player = Bukkit.getPlayer(args[0]);
					World w = player.getWorld();
					Location l = player.getLocation();
					w.strikeLightning(l);
					player.setFireTicks(100);
					sender.sendMessage(ChatColor.GOLD + "Opening the heavens on " + ChatColor.RED + args[0]);
				}
			}
		} else {
			sender.sendMessage(ChatColor.RED + "No permission");
		}
		return false;
	}
}