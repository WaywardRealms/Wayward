package net.wayward_realms.waywardessentials.command;

import net.wayward_realms.waywardessentials.WaywardEssentials;
import net.wayward_realms.waywardlib.util.math.MathUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TrackCommand implements CommandExecutor {

    private WaywardEssentials plugin;

    public TrackCommand(WaywardEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("wayward.essentials.command.track")) {
            if (sender instanceof Player) {
                if (args.length > 0) {
                    Player player = plugin.getServer().getPlayer(args[0]);
                    if (player != null) {
                        if (!plugin.isTrackable(player)) {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That player is not currently trackable.");
                            player.sendMessage(plugin.getPrefix() + ChatColor.RED + sender.getName() + " attempted to track you. If you wish for them to be able to find you, re-enable tracking with /toggletracking");
                            return true;
                        }
                        ItemStack itemRequirement = plugin.getConfig().getItemStack("track-command.item-requirement");
                        if (itemRequirement != null && !player.getInventory().containsAtLeast(itemRequirement, itemRequirement.getAmount())) {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You require " + itemRequirement.getAmount() + " x " + itemRequirement.getType().toString().toLowerCase().replace('_', ' ') + " to use that command.");
                            return true;
                        }
                        int maximumDistance = plugin.getConfig().getInt("track-command.maximum-distance");
                        double distance = MathUtils.fastsqrt(player.getLocation().distanceSquared(((Player) sender).getLocation()));
                        if (maximumDistance >= 0 && distance > maximumDistance) {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You are too far away to track that player");
                            return true;
                        }
                        ((Player) sender).setCompassTarget(player.getLocation());
                        sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Now tracking " + player.getName() + "/" + player.getDisplayName());
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "No player by that name is online.");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify a player to track.");
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to perform this command.");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
        }
        return true;
    }

}
