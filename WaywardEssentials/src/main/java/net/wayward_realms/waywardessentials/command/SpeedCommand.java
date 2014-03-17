package net.wayward_realms.waywardessentials.command;

import net.wayward_realms.waywardessentials.WaywardEssentials;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpeedCommand implements CommandExecutor {

    private WaywardEssentials plugin;

    public SpeedCommand(WaywardEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("wayward.essentials.command.speed")) {
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            float speed = 0;
            if (args.length >= 2 && plugin.getServer().getPlayer(args[0]) != null) {
                player = plugin.getServer().getPlayer(args[0]);
                try {
                    speed = Float.parseFloat(args[1]);
                } catch (NumberFormatException exception) {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Speed must be a number.");
                }
            } else {
                try {
                    speed = Float.parseFloat(args[0]);
                } catch (NumberFormatException exception) {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Speed must be a number.");
                }
            }
            if (player != null) {
                if (speed >= 0 && speed <= 1) {
                    player.setFlySpeed(speed);
                    sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Set " + player.getName() + "'s fly speed to " + speed);
                    player.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Your flyspeed was set to " + speed + " by " + sender.getName());
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Speed must be between 0 and 1");
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify a player if you're using this command from console.");
            }
        }
        return true;
    }

}
