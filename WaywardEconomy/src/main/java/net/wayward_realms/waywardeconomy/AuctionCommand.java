package net.wayward_realms.waywardeconomy;

import net.wayward_realms.waywardeconomy.auction.AuctionImpl;
import net.wayward_realms.waywardeconomy.auction.BidImpl;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.economy.Auction;
import net.wayward_realms.waywardlib.economy.Bid;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AuctionCommand implements CommandExecutor {

    private WaywardEconomy plugin;

    public AuctionCommand(WaywardEconomy plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("create")) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    CharacterPlugin characterPlugin = plugin.getCharacterPlugin();
                    Auction auction = new AuctionImpl();
                    auction.setCharacter(characterPlugin.getActiveCharacter(player));
                    auction.setItem(player.getItemInHand());
                    auction.setCurrency(plugin.getPrimaryCurrency());
                    player.setItemInHand(null);
                    auction.setLocation(player.getLocation());
                    plugin.addAuction(auction);
                    sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Created an auction for your " + auction.getItem().getType().toString().toLowerCase().replace('_', ' '));
                    sender.sendMessage(ChatColor.GREEN + "Set the minimum bid increment with /auction set minimumBidIncrement [amount]");
                    sender.sendMessage(ChatColor.GREEN + "Set the starting bid with /auction set startingBid [amount]");
                    sender.sendMessage(ChatColor.GREEN + "Once done, use /auction openBidding to start the bidding.");
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to perform this command.");
                }
            } else if (args[0].equalsIgnoreCase("set")) {
                if (args.length >= 2) {
                    if (args[1].equalsIgnoreCase("minimumBidIncrement")) {
                        if (args.length >= 3) {
                            if (sender instanceof Player) {
                                Player player = (Player) sender;
                                CharacterPlugin characterPlugin = plugin.getCharacterPlugin();
                                Auction auction = plugin.getAuction(characterPlugin.getActiveCharacter(player));
                                if (auction != null) {
                                    try {
                                        auction.setMinimumBidIncrement(Integer.parseInt(args[2]));
                                        sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Set minimum bid increment to " + args[2]);
                                    } catch (NumberFormatException exception) {
                                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify an integer for minimum bid increment.");
                                    }
                                } else {
                                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "YOu must first create an auction with /auction create");
                                }
                            } else {
                                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to perform this command.");
                            }
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify a minimum bid increment.");
                        }
                    } else if (args[1].equalsIgnoreCase("startingBid")) {
                        if (args.length >= 3) {
                            if (sender instanceof Player) {
                                Player player = (Player) sender;
                                CharacterPlugin characterPlugin = plugin.getCharacterPlugin();
                                Character character = characterPlugin.getActiveCharacter(player);
                                Auction auction = plugin.getAuction(character);
                                if (auction != null) {
                                    try {
                                        Bid bid = new BidImpl(character, Integer.parseInt(args[2]));
                                        auction.addBid(bid);
                                        sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Set starting bid to " + bid.getAmount());
                                    } catch (NumberFormatException exception) {
                                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify an integer for starting bid.");
                                    }
                                } else {
                                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must first create an auction with /auction create");
                                }
                            } else {
                                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to perform this command.");
                            }
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify a starting bid amount.");
                        }
                    }
                }
            } else if (args[0].equalsIgnoreCase("openBidding")) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    CharacterPlugin characterPlugin = plugin.getCharacterPlugin();
                    final Auction auction = plugin.getAuction(characterPlugin.getActiveCharacter(player));
                    if (auction != null) {
                        if (auction.getBids().size() == 0) {
                            auction.addBid(new BidImpl(characterPlugin.getActiveCharacter(player), 0));
                        }
                        auction.openBidding();
                        sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Bidding opened!");
                        for (Player bystander : plugin.getServer().getOnlinePlayers()) {
                            if (bystander.getLocation().distanceSquared(auction.getLocation()) <= 1024) {
                                bystander.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Bidding opened for " + (auction.getCharacter().isNameHidden() ? ChatColor.MAGIC + auction.getCharacter().getName() + ChatColor.RESET : auction.getCharacter().getName()) + ChatColor.GREEN + "'s " + auction.getItem().getAmount() + "x" + auction.getItem().getType().toString().toLowerCase().replace('_', ' ') + " at " + auction.getHighestBid().getAmount() + " " + (auction.getHighestBid().getAmount() == 1 ? auction.getCurrency().getNameSingular() : auction.getCurrency().getNamePlural()));
                                bystander.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Use /bid " + auction.getCharacter().getPlayer().getName() + " [amount] to bid");
                            }
                        }
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                auction.closeBidding();
                            }
                        }, 6000L);
                        int[] intervals = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 20, 30, 60, 120, 180, 240};
                        for (final int interval : intervals) {
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                @Override
                                public void run() {
                                    Set<Player> players = new HashSet<>();
                                    for (Bid bid : auction.getBids()) {
                                        if (bid.getCharacter().getPlayer().isOnline()) {
                                            players.add(bid.getCharacter().getPlayer().getPlayer());
                                        }
                                    }
                                    int minutes = (int) Math.floor(interval / 60);
                                    int seconds = interval % 60;
                                    String[] messages = new String[]{plugin.getPrefix() + ChatColor.GREEN + (minutes > 0 ? minutes + " minutes, " : "") + seconds + " seconds till " + (auction.getCharacter().isNameHidden() ? ChatColor.MAGIC + auction.getCharacter().getName() + ChatColor.RESET : auction.getCharacter().getName()) + ChatColor.GREEN + "'s auction for " + auction.getItem().getAmount() + "x" + auction.getItem().getType().toString().toLowerCase().replace('_', ' ') + " ends.",
                                            ChatColor.GREEN + "Highest bid: " + auction.getHighestBid().getAmount() + " " + (auction.getHighestBid().getAmount() == 1 ? auction.getCurrency().getNameSingular() : auction.getCurrency().getNamePlural()) + " by " + (auction.getHighestBid().getCharacter().isNameHidden() ? ChatColor.MAGIC + auction.getHighestBid().getCharacter().getName() + ChatColor.RESET : auction.getHighestBid().getCharacter().getName())};
                                    for (Player player : players) {
                                        player.sendMessage(messages);
                                    }
                                }
                            }, (240 - interval) * 20);
                        }
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                auction.closeBidding();
                            }
                        }, 4800);
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must first create an auction with /auction create");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to perform this command.");
                }
            } else if (args[0].equalsIgnoreCase("closeBidding")) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    CharacterPlugin characterPlugin = plugin.getCharacterPlugin();
                    Auction auction = plugin.getAuction(characterPlugin.getActiveCharacter(player));
                    if (auction != null) {
                        auction.closeBidding();
                        sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Bidding closed.");
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must first create an auction with /auction create");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to perform this command.");
                }
            } else if (args[0].equalsIgnoreCase("status")) {
                Player player = null;
                if (sender instanceof Player) {
                    player = (Player) sender;
                }
                if (args.length >= 2 && plugin.getServer().getPlayer(args[1]) != null) {
                    player = plugin.getServer().getPlayer(args[1]);
                }
                if (player != null) {
                    CharacterPlugin characterPlugin = plugin.getCharacterPlugin();
                    Auction auction = plugin.getAuction(characterPlugin.getActiveCharacter(player));
                    sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Auction status: ");
                    sender.sendMessage(ChatColor.GREEN + "Hosted by: " + (auction.getCharacter().isNameHidden() ? ChatColor.MAGIC + auction.getCharacter().getName() + ChatColor.RESET : auction.getCharacter().getName()));
                    sender.sendMessage(ChatColor.GREEN + "Item: " + auction.getItem().getAmount() + " x " + auction.getItem().getType().toString().toLowerCase().replace('_', ' '));
                    sender.sendMessage(ChatColor.GREEN + "Bidding is " + (auction.isBiddingOpen() ? (ChatColor.GREEN + "OPEN") : (ChatColor.RED + "CLOSED")));
                    sender.sendMessage(ChatColor.GREEN + "Located at: " + auction.getLocation().getBlockX() + ", " + auction.getLocation().getBlockY() + ", " + auction.getLocation().getBlockZ());
                    if (!auction.getBids().isEmpty()) sender.sendMessage(ChatColor.GREEN + "Starting at: " + ((List<Bid>) auction.getBids()).get(0).getAmount());
                    sender.sendMessage(ChatColor.GREEN + "Minimum bid increment: " + auction.getMinimumBidIncrement());
                    if (auction.getHighestBid() != null)sender.sendMessage(ChatColor.GREEN + "Highest bid: " + auction.getHighestBid().getAmount() + " " + (auction.getHighestBid().getAmount() == 1 ? auction.getCurrency().getNameSingular() : auction.getCurrency().getNamePlural()) + " by " + (auction.getHighestBid().getCharacter().isNameHidden() ? ChatColor.MAGIC + auction.getHighestBid().getCharacter().getName() + ChatColor.RESET : auction.getHighestBid().getCharacter().getName()));
                    sender.sendMessage(ChatColor.GREEN + "Currency: " + auction.getCurrency().getNamePlural());
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify a player");
                }
            } else if (args[0].equalsIgnoreCase("list")) {
                for (Auction auction : plugin.getAuctions()) {
                    sender.sendMessage(ChatColor.DARK_GRAY + "=== " + ChatColor.YELLOW + auction.getCharacter().getName() + ChatColor.DARK_GRAY + "/" + ChatColor.YELLOW + auction.getCharacter().getPlayer().getName() + ChatColor.DARK_GRAY + " ===");
                    sender.sendMessage(ChatColor.GREEN + "Item: " + auction.getItem().getAmount() + " x " + auction.getItem().getType().toString().toLowerCase().replace('_', ' '));
                    sender.sendMessage(ChatColor.GREEN + "Bidding is " + (auction.isBiddingOpen() ? (ChatColor.GREEN + "OPEN") : (ChatColor.RED + "CLOSED")));
                    sender.sendMessage(ChatColor.GREEN + "Located at: " + auction.getLocation().getBlockX() + ", " + auction.getLocation().getBlockY() + ", " + auction.getLocation().getBlockZ());
                    if (!auction.getBids().isEmpty()) sender.sendMessage(ChatColor.GREEN + "Starting at: " + ((List<Bid>) auction.getBids()).get(0).getAmount());
                    sender.sendMessage(ChatColor.GREEN + "Minimum bid increment: " + auction.getMinimumBidIncrement());
                    if (auction.getHighestBid() != null) sender.sendMessage(ChatColor.GREEN + "Highest bid: " + auction.getHighestBid().getAmount() + " " + (auction.getHighestBid().getAmount() == 1 ? auction.getCurrency().getNameSingular() : auction.getCurrency().getNamePlural()) + " by " + (auction.getHighestBid().getCharacter().isNameHidden() ? ChatColor.MAGIC + auction.getHighestBid().getCharacter().getName() + ChatColor.RESET : auction.getHighestBid().getCharacter().getName()));
                    sender.sendMessage(ChatColor.GREEN + "Currency: " + auction.getCurrency().getNamePlural());
                }
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /" + label + " [create|set|openBidding|closeBidding|status|list]");
        }
        return true;
    }

}
