package net.wayward_realms.waywardtravel.portal;

import net.wayward_realms.waywardlib.travel.Portal;
import net.wayward_realms.waywardtravel.WaywardTravel;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static net.wayward_realms.waywardlib.util.location.LocationUtils.parseLocation;

public class PortalCommand implements CommandExecutor {

    private WaywardTravel plugin;

    public PortalCommand(WaywardTravel plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("create")) {
                if (sender.hasPermission("wayward.travel.command.portal.create")) {
                    if (args.length > 3) {
                        Location location1 = parseLocation(args[1]);
                        Location location2 = parseLocation(args[2]);
                        Location teleportLocation = parseLocation(args[3]);
                        if (location1 != null && location2 != null && teleportLocation != null) {
                            Location minLocation = new Location(location1.getWorld(), Math.min(location1.getX(), location2.getX()) - 0.5, Math.min(location1.getY(), location2.getY()) - 0.5, Math.min(location1.getZ(), location2.getZ()) - 0.5);
                            Location maxLocation = new Location(location1.getWorld(), Math.max(location1.getX(), location2.getX()) + 0.5, Math.max(location1.getY(), location2.getY()) + 0.5, Math.max(location1.getZ(), location2.getZ()) + 0.5);
                            Portal portal = new PortalImpl(minLocation, maxLocation, teleportLocation);
                            plugin.addPortal(portal);
                            sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Portal created.");
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Could not parse locations.");
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Format: world,x,y,z");
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /portal create [location1] [location2] [teleportLocation]");
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /portal create [location1] [location2] [teleportLocation]");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /portal [create]");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /portal [create]");
        }
        return true;
    }

}
