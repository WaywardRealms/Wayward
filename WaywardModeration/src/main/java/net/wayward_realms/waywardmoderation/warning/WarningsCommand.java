package net.wayward_realms.waywardmoderation.warning;

import net.wayward_realms.waywardlib.moderation.Warning;
import net.wayward_realms.waywardmoderation.WaywardModeration;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class WarningsCommand implements CommandExecutor {

    private WaywardModeration plugin;

    public WarningsCommand(WaywardModeration plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        if (args.length > 0) {
            if (sender.hasPermission("wayward.moderation.command.warnings")) {
                if (plugin.getServer().getPlayer(args[0]) != null) {
                    player = plugin.getServer().getPlayer(args[0]);
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission to view another player's warnings");
            }
        }
        if (player != null) {
            sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + player.getName() +"'s warnings: ");
            DateFormat format = new SimpleDateFormat("dd/MM/yy hh:mm");
            for (Warning warning : plugin.getWarnings(player)) {
                sender.sendMessage(ChatColor.GREEN + format.format(warning.getTime()) + " - " + warning.getReason());
                sender.sendMessage(ChatColor.GREEN + "(Issued by " + warning.getIssuer().getName() + ")");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify a player");
        }
        return true;
    }
}
