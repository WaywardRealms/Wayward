package net.wayward_realms.waywarddonations;

import net.wayward_realms.waywardlib.donation.DonationRank;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class ClaimItemsCommand implements CommandExecutor {

    private WaywardDonations plugin;

    public ClaimItemsCommand(WaywardDonations plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Set<ItemStack> items = new HashSet<>();
        for (DonationRank rank : plugin.getDonationRanks((Player) sender)) {
            items.addAll(rank.getKit().getItems());
        }
        for (ItemStack item : items) {
            int amountToGive = item.getAmount() - plugin.getItemsClaimed((Player) sender, item.getType());
            item.setAmount(amountToGive);
            if (item.getAmount() > 0) {
                ((Player) sender).getInventory().addItem(item);
                sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Claimed " + item.getAmount() + " x " + item.getType());
                plugin.setItemsClaimed((Player) sender, item.getType(), item.getAmount());
            }
        }
        return true;
    }
}
