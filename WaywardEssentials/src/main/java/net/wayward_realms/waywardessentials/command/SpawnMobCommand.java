package net.wayward_realms.waywardessentials.command;

import net.wayward_realms.waywardessentials.WaywardEssentials;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class SpawnMobCommand implements CommandExecutor {

    private WaywardEssentials plugin;

    public SpawnMobCommand(WaywardEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("wayward.essentials.command.spawnmob")) {
            if (args.length >= 2) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    try {
                        EntityType entityType = EntityType.valueOf(args[0].toUpperCase());
                        try {
                            int amount = Integer.parseInt(args[1]);
                            for (int i = 0; i < amount; i++) {
                                player.getLocation().getWorld().spawnEntity(player.getLocation(), entityType);
                            }
                            sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Mobs spawned.");
                        } catch (NumberFormatException exception) {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "The amount of mobs must be an integer.");
                        }
                    } catch (IllegalArgumentException exception) {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That entity type does not exist.");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to use this command.");
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify an entity type and an amount.");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
        }
        return true;
    }

}
