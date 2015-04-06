package net.wayward_realms.waywarddonations;

import net.wayward_realms.waywardlib.classes.ClassesPlugin;
import net.wayward_realms.waywardlib.donation.DonationRank;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClaimLevelsCommand implements CommandExecutor {

    private WaywardDonations plugin;

    public ClaimLevelsCommand(WaywardDonations plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        int maxLevels = 0;
        for (DonationRank donationRank : plugin.getDonationRanks((Player) sender)) {
            if (donationRank.getLevels() > maxLevels) {
                maxLevels = donationRank.getLevels();
            }
        }
        ClassesPlugin classesPlugin = plugin.getServer().getServicesManager().getRegistration(ClassesPlugin.class).getProvider();
        classesPlugin.setLevel((Player) sender, maxLevels);
        sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Level set to " + maxLevels);
        return true;
    }

}
