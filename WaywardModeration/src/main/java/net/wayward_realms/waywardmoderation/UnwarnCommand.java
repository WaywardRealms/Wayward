package net.wayward_realms.waywardmoderation;

import net.wayward_realms.waywardlib.moderation.Warning;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class UnwarnCommand implements CommandExecutor {

    private WaywardModeration plugin;

    public UnwarnCommand(WaywardModeration plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("wayward.moderation.command.warn")) {
            if (args.length > 1) {
                OfflinePlayer player = plugin.getServer().getOfflinePlayer(args[0]);
                StringBuilder warningReasonBuilder = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    warningReasonBuilder.append(args[i]).append(" ");
                }
                String warningReason = warningReasonBuilder.toString().replace(" ", "");
                boolean warningFound = false;
                for (Warning warning : plugin.getWarnings(player)) {
                    if (warning.getReason().replace(" ", "").equalsIgnoreCase(warningReason)) {
                        plugin.removeWarning(player, warning);
                        warningFound = true;
                    }
                }
                if (warningFound) {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Warning removed.");
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Warning not found, did you spell it exactly as it appears?");
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /unwarn [player] [reason]");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
        }
        return true;
    }

}
