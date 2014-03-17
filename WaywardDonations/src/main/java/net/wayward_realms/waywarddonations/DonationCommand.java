package net.wayward_realms.waywarddonations;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class DonationCommand implements CommandExecutor {

    private WaywardDonations plugin;

    public DonationCommand(WaywardDonations plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            if (args.length >= 2) {
                if (plugin.getDonationRank(args[1]) != null) {
                    plugin.addDonationRank(plugin.getServer().getOfflinePlayer(args[0]), plugin.getDonationRank(args[1]));
                    sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Donation rank " + args[1] + " successfully added to " + args[0]);
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Donation rank " + args[1] + " does not exist");
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /donation [player] [rank]");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "This command may only be executed from console.");
        }
        return true;
    }

}
