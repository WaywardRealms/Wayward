package net.wayward_realms.waywardmonsters;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EntityLevelCommand implements CommandExecutor {

    private WaywardMonsters plugin;

    public EntityLevelCommand(WaywardMonsters plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("set")) {
                if (sender.hasPermission("wayward.monsters.command.entitylevel.set")) {
                    if (sender instanceof Player) {
                        if (args.length > 2) {
                            try {
                                int level = Integer.parseInt(args[1]);
                                int radius = Integer.parseInt(args[2]);
                                plugin.setChunkEntityLevel(((Player) sender).getLocation().getChunk(), level, radius);
                                sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Chunk's entity level set to " + level + ", fading over " + radius + " chunks.");
                            } catch (NumberFormatException exception) {
                                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /" + label + " set [level] [radius]");
                            }
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /" + label + " set [level] [radius]");
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to perform this command.");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
                }
            } else if (args[0].equalsIgnoreCase("get")) {
                if (sender.hasPermission("wayward.monsters.command.entitylevel.get")) {
                    if (sender instanceof Player) {
                        Chunk chunk = ((Player) sender).getLocation().getChunk();
                        sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "This chunk's mob level is " + plugin.getChunkEntityLevel(chunk));
                        sender.sendMessage(plugin.getPrefix() + ChatColor.GRAY + "(You are in chunk " + chunk.getX() + ", " + chunk.getZ());
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to perform this command.");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /" + label + " [get|set]");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /" + label + " [get|set]");
        }
        return true;
    }

}
