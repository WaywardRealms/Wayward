package net.wayward_realms.waywardessentials.command;

import net.wayward_realms.waywardessentials.WaywardEssentials;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SeenCommand implements CommandExecutor {

    private WaywardEssentials plugin;

    public SeenCommand(WaywardEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            OfflinePlayer player = plugin.getServer().getOfflinePlayer(args[0]);
            if (player.isOnline()) {
                sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + player.getName() + " is online now!");
            } else {
                if (player.getLastPlayed() != 0) {
                    Date lastPlayed = new Date(player.getLastPlayed());
                    sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + player.getName() + " was last seen on " + new SimpleDateFormat("dd/MM/yyyy").format(lastPlayed) + " at " + new SimpleDateFormat("HH:mm:ss").format(lastPlayed));
                    long millis = System.currentTimeMillis() - player.getLastPlayed();
                    long second = (millis / 1000) % 60;
                    long minute = (millis / (1000 * 60)) % 60;
                    long hour = (millis / (1000 * 60 * 60)) % 24;
                    long day = (millis / (1000 * 60 * 60 * 24));
                    sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "(That's " + (day > 0 ? day + " days, " : "") + (hour > 0 ? hour + " hours, " : "") + (minute > 0 ? minute + " minutes, " : "") + (second > 0 ? second + " seconds, " : "") + " ago.)");
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That player has never played on the server.");
                }
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify a player.");
        }
        return true;
    }
}
