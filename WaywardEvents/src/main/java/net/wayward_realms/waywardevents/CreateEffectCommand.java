package net.wayward_realms.waywardevents;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static net.wayward_realms.waywardlib.util.location.LocationUtils.parseLocation;

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
                        target.getWorld().playEffect(target.getLocation(), effect, null);
                    } catch (IllegalArgumentException exception) {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Could not find an effect by that name.");
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Available effects: ");
                        for (Effect effect : Effect.values()) {
                            sender.sendMessage(ChatColor.RED + effect.toString().toLowerCase());
                        }
                    }
                } else {
                    Location target = null;
                    Effect effect = null;
                    try {
                        target = parseLocation(args[0]);
                    } catch (IllegalArgumentException exception) {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Could not find that player or location!");
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "If you're targeting a player, make sure they're online and you spelt their name right.");
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "If you're targeting a location, use the format world,x,y,z or world,x,y,z,yaw,pitch");
                    }
                    try {
                        effect = Effect.valueOf(args[1].toUpperCase());
                    } catch (IllegalArgumentException exception) {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Could not find an effect by that name.");
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Available effects: ");
                        for (Effect effect1 : Effect.values()) {
                            sender.sendMessage(ChatColor.RED + effect1.toString().toLowerCase());
                        }
                    }
                    if (target != null && effect != null) target.getWorld().playEffect(target, effect, 0);
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /createeffect [player|location] [effect]");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
        }
        return true;
    }

}
