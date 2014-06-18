package net.wayward_realms.waywardessentials.command;

import net.wayward_realms.waywardessentials.WaywardEssentials;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WarpCommand implements CommandExecutor {

    private WaywardEssentials plugin;

    public WarpCommand(WaywardEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("wayward.essentials.command.warp")) {
            if (sender instanceof Player) {
                if (args.length >= 1) {
                    Player player = (Player) sender;
                    Location warp = plugin.getWarp(args[0].toLowerCase());
                    if (warp != null) {
                        player.teleport(warp);
                        sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Warped to " + args[0].toLowerCase());
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That warp does not exist.");
                    }
                } else {
                    if (plugin.getWarps().size() > 0) {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Warps:");
                        Object[] warps = plugin.getWarps().keySet().toArray();
                        List<String> warpMessages = new ArrayList<>();
                        StringBuilder warpsBuilder = new StringBuilder();
                        for (int i = 0; i < warps.length; i++) {
                            warpsBuilder.append(warps[i]).append(", ");
                            if ((i + 1) % 10 == 0) {
                                if (i == warps.length - 1) {
                                    warpsBuilder.delete(warpsBuilder.length() - 2, warpsBuilder.length());
                                }
                                warpMessages.add(warpsBuilder.toString());
                                warpsBuilder = new StringBuilder();
                            }
                        }
                        if (warpsBuilder.length() != 0) {
                            warpMessages.add(warpsBuilder.delete(warpsBuilder.length() - 2, warpsBuilder.length()).toString());
                        }
                        for (String message : warpMessages) {
                            sender.sendMessage(ChatColor.GREEN + message);
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "No warps are currently set. Set one using /setwarp [name]");
                    }
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to use this command.");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission to use this command.");
        }
        return true;
    }

}
