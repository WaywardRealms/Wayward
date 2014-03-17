package net.wayward_realms.vayleryn.vayleryndonatorperks;

import net.wayward_realms.waywardlib.donation.DonationRank;
import net.wayward_realms.waywardlib.economy.EconomyPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClaimMoneyCommand implements CommandExecutor {

    private WaywardDonations plugin;

    public ClaimMoneyCommand(WaywardDonations plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        int money = 0;
        for (DonationRank rank : plugin.getDonationRanks((Player) sender)) {
            money += rank.getMoney();
        }
        int moneyAlreadyClaimed = plugin.getConfig().getInt(sender.getName() + ".money-claimed", 0);
        money -= moneyAlreadyClaimed;
        if (money > 0) {
            EconomyPlugin economyPlugin = plugin.getServer().getServicesManager().getRegistration(EconomyPlugin.class).getProvider();
            economyPlugin.addMoney((Player) sender, money);
            plugin.getConfig().set(sender.getName() + ".money-claimed", money);
            plugin.saveConfig();
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You may not currently claim any more money.");
        }
        return true;
    }

}
