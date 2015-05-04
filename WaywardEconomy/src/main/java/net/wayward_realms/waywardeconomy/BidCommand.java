package net.wayward_realms.waywardeconomy;

import net.wayward_realms.waywardeconomy.auction.BidImpl;
import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.economy.Auction;
import net.wayward_realms.waywardlib.economy.Bid;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

import static net.wayward_realms.waywardlib.util.math.MathUtils.fastsqrt;

public class BidCommand implements CommandExecutor {

    private final WaywardEconomy plugin;

    public BidCommand(WaywardEconomy plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 2) {
            if (sender instanceof Player) {
                Player bidPlayer = (Player) sender;
                if (plugin.getServer().getPlayer(args[0]) != null) {
                    Player player = plugin.getServer().getPlayer(args[0]);
                    Character character = plugin.getCharacterPlugin().getActiveCharacter(player);
                    if (plugin.getAuction(character) != null) {
                        Auction auction = plugin.getAuction(character);
                        if (auction.getLocation().distanceSquared(bidPlayer.getLocation()) <= 1024) {
                            Character bidder = plugin.getCharacterPlugin().getActiveCharacter(bidPlayer);
                            try {
                                int amount = Integer.parseInt(args[1]);
                                if (amount >= auction.getHighestBid().getAmount() + auction.getMinimumBidIncrement()) {
                                    Bid bid = new BidImpl(bidder, amount);
                                    auction.addBid(bid);
                                    sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Bid " + bid.getAmount() + " " + (bid.getAmount() == 1 ? auction.getCurrency().getNameSingular() : auction.getCurrency().getNamePlural()) + " for " + (auction.getCharacter().isNameHidden() ? ChatColor.MAGIC + auction.getCharacter().getName() + ChatColor.RESET : auction.getCharacter().getName()) + "'s " + auction.getItem().getType().toString().toLowerCase().replace('_', ' '));
                                    Set<Player> players = new HashSet<>();
                                    for (Bid bid1 : auction.getBids()) {
                                        players.add(bid1.getCharacter().getPlayer().getPlayer());
                                    }
                                    for (Player player1 : players) {
                                        player1.sendMessage(plugin.getPrefix() + ChatColor.GREEN + (bidder.isNameHidden() ? ChatColor.MAGIC + bidder.getName() + ChatColor.RESET : bidder.getName()) + ChatColor.GREEN + " bid " + bid.getAmount() + " " + (bid.getAmount() == 1 ? auction.getCurrency().getNameSingular() : auction.getCurrency().getNamePlural()) + " for " + (auction.getCharacter().isNameHidden() ? ChatColor.MAGIC + auction.getCharacter().getName() + ChatColor.RESET : auction.getCharacter().getName()) + ChatColor.GREEN + "'s " + auction.getItem().getType().toString().toLowerCase().replace('_', ' '));
                                    }
                                } else {
                                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "The minimum bid increment is " + auction.getMinimumBidIncrement() + " " + (auction.getMinimumBidIncrement() == 1 ? auction.getCurrency().getNameSingular() : auction.getCurrency().getNamePlural()) + ", so you may not bid less than " + (auction.getHighestBid().getAmount() + auction.getMinimumBidIncrement()) + " " + ((auction.getHighestBid().getAmount() + auction.getMinimumBidIncrement()) == 1 ? auction.getCurrency().getNameSingular() : auction.getCurrency().getNamePlural()));
                                }
                            } catch (NumberFormatException exception) {
                                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify an integer for the amount to bid.");
                            }
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must stand " + (fastsqrt(auction.getLocation().distanceSquared(bidPlayer.getLocation())) - 32) + " blocks closer to that auction in order to be able to bid");
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That player's active character is not running an auction.");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "There is no player by that name online.");
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to use that command.");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /bid [player] [amount]");
        }
        return true;
    }

}
