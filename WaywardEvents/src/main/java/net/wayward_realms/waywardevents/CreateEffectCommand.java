package net.wayward_realms.waywardevents;

import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateEffectCommand implements CommandExecutor {

    private net.wayward_realms.waywardevents.WaywardEvents plugin;

    public CreateEffectCommand(net.wayward_realms.waywardevents.WaywardEvents plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("wayward.events.command.createeffect")) {
            if (args.length >= 2) {
                if (plugin.getServer().getPlayer(args[0]) != null) {
                    Player target = plugin.getServer().getPlayer(args[0]);
                    try {
                        Effect effect = Effect.valueOf(args[1].toUpperCase());
                        target.playEffect(target.getLocation(), effect, null);
                    } catch (IllegalArgumentException exception) {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Could not find an effect by that name.");
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Available effects: ");
                        for (Effect effect : Effect.values()) {
                            sender.sendMessage(ChatColor.RED + effect.toString().toLowerCase());
                        }
                    }
                } else {
                    try {
                        Location target = parseLocation(args[0]);
                        Effect effect = Effect.valueOf(args[1]);
                        target.getWorld().playEffect(target, effect, 0);
                    } catch (IllegalArgumentException exception) {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Could not find that player or location!");
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "If you're targeting a player, make sure they're online and you spelt their name right.");
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "If you're targeting a location, use the format world,x,y,z or world,x,y,z,yaw,pitch");
                    }
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /createeffect [player|location] [effect]");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
        }
        return true;
    }

    private Location parseLocation(String locationString) {
        if (StringUtils.countMatches(locationString, ",") == 3) {
            World world = Bukkit.getWorld(locationString.split(",")[0]);
            try {
                double x = Double.parseDouble(locationString.split(",")[1]);
                double y = Double.parseDouble(locationString.split(",")[2]);
                double z = Double.parseDouble(locationString.split(",")[3]);
                if (world != null) {
                    return new Location(world, x, y, z);
                } else {
                    throw new IllegalArgumentException("Unparsable location!");
                }
            } catch (NumberFormatException exception) {
                throw new IllegalArgumentException("Unparsable location!", exception);
            }
        } else if (StringUtils.countMatches(locationString, ",") == 5) {
            World world = Bukkit.getWorld(locationString.split(",")[0]);
            try {
                double x = Double.parseDouble(locationString.split(",")[1]);
                double y = Double.parseDouble(locationString.split(",")[2]);
                double z = Double.parseDouble(locationString.split(",")[3]);
                float yaw = Float.parseFloat(locationString.split(",")[4]);
                float pitch = Float.parseFloat(locationString.split(",")[5]);
                if (world != null) {
                    return new Location(world, x, y, z, yaw, pitch);
                } else {
                    throw new IllegalArgumentException("Unparsable location!");
                }
            } catch (NumberFormatException exception) {
                throw new IllegalArgumentException("Unparsable location!", exception);
            }
        }
        throw new IllegalArgumentException("Unparsable location!");
    }

}
