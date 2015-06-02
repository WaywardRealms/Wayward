package net.wayward_realms.waywardessentials.command;

import net.wayward_realms.waywardessentials.WaywardEssentials;
import net.wayward_realms.waywardlib.util.math.MathUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DistanceCommand implements CommandExecutor {

    private WaywardEssentials plugin;

    public DistanceCommand(WaywardEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("wayward.essentials.command.distance")) {
            if (sender instanceof Player) {
                if (args.length > 0) {
                    Player player = plugin.getServer().getPlayer(args[0]);
                    if (player != null) {
                        if (player.getWorld() == ((Player) sender).getWorld()) {
                            if (!plugin.isTrackable(player)) {
                                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That player is not currently trackable.");
                                player.sendMessage(plugin.getPrefix() + ChatColor.RED + sender.getName() + " attempted to check their distance to you. If you wish for them to be able to find you, re-enable tracking with /toggletracking");
                                return true;
                            }
                            ItemStack itemRequirement = plugin.getConfig().getItemStack("distance-command.item-requirement");
                            if (itemRequirement != null && !player.getInventory().containsAtLeast(itemRequirement, itemRequirement.getAmount())) {
                                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You require " + itemRequirement.getAmount() + " x " + itemRequirement.getType().toString().toLowerCase().replace('_', ' ') + " to use that command.");
                                return true;
                            }
                            int maximumDistance = plugin.getConfig().getInt("distance-command.maximum-distance");
                            double distance = MathUtils.fastsqrt(player.getLocation().distanceSquared(((Player) sender).getLocation()));
                            if (maximumDistance >= 0 && distance > maximumDistance) {
                                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You are too far away to get the distance to that player");
                                return true;
                            }
                            sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Distance to " + player.getName() + "/" + player.getDisplayName() + ": " + distance);
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That player is in a different world.");
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That player is not online");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify a player.");
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to perform that command.");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
        }
        return true;
    }

}
