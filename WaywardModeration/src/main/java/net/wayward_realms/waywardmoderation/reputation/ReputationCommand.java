package net.wayward_realms.waywardmoderation.reputation;

import net.wayward_realms.waywardmoderation.WaywardModeration;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReputationCommand implements CommandExecutor {

    // Constants
    private static final int MAX_REPUTATION = ReputationManager.MAX_REPUTATION;
    private static final char BLOCK_CHARACTER = '|';

    private WaywardModeration plugin;

    public ReputationCommand(WaywardModeration plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("view")) {
                OfflinePlayer player = null;
                int reputation;
                if (sender instanceof Player) {
                    player = (Player) sender;
                }
                if (args.length >= 2) {
                    player = plugin.getServer().getOfflinePlayer(args[1]);
                }
                if (player != null) {
                    reputation = plugin.getReputation(player);
                    String reputationBar = "";
                    if (reputation < 0) {
                        reputationBar += ChatColor.DARK_RED;
                        int i;
                        for (i = 0; i < MAX_REPUTATION + reputation; i++) {
                            reputationBar += BLOCK_CHARACTER;
                        }
                        reputationBar += ChatColor.RED;
                        for (; i < MAX_REPUTATION; i++) {
                            reputationBar += BLOCK_CHARACTER;
                        }
                        reputationBar += ChatColor.WHITE + "" + BLOCK_CHARACTER + ChatColor.DARK_GREEN;
                        for (i  = 0; i < MAX_REPUTATION; i++) {
                            reputationBar += BLOCK_CHARACTER;
                        }
                    } else if (reputation == 0) {
                        reputationBar += ChatColor.DARK_RED;
                        int i;
                        for (i = 0; i < MAX_REPUTATION; i++) {
                            reputationBar += BLOCK_CHARACTER;
                        }
                        reputationBar += ChatColor.WHITE + "" + BLOCK_CHARACTER + ChatColor.DARK_GREEN;
                        for (i = 0; i < MAX_REPUTATION; i++) {
                            reputationBar += BLOCK_CHARACTER;
                        }
                    } else if (reputation > 0) {
                        reputationBar += ChatColor.DARK_RED;
                        int i;
                        for (i = 0; i < MAX_REPUTATION; i++) {
                            reputationBar += BLOCK_CHARACTER;
                        }
                        reputationBar += ChatColor.WHITE + "" + BLOCK_CHARACTER + ChatColor.GREEN;
                        for (i = 0; i < reputation; i++) {
                            reputationBar += BLOCK_CHARACTER;
                        }
                        reputationBar += ChatColor.DARK_GREEN;
                        for (; i < MAX_REPUTATION; i++) {
                            reputationBar += BLOCK_CHARACTER;
                        }
                    }
                    reputationBar += ChatColor.DARK_GRAY + " [" + ChatColor.WHITE + reputation + ChatColor.DARK_GRAY + "]";
                    sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Reputation: " + reputationBar);
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify a player when checking reputation from the console.");
                }
            } else if (args[0].equalsIgnoreCase("set")) {
                if (sender.hasPermission("wayward.moderation.command.reputation.set")) {
                    if (args.length >= 3) {
                        OfflinePlayer player = plugin.getServer().getOfflinePlayer(args[1]);
                        try {
                            int reputation = Integer.parseInt(args[2]);
                            plugin.setReputation(player, reputation);
                            sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + player.getName() + "'s reputation set to " + reputation + ".");
                        } catch (NumberFormatException exception) {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Reputation must be an integer.");
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /reputation set [player] [reputation]");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
                }
            } else if (args[0].equalsIgnoreCase("plus")) {
                if (sender.hasPermission("wayward.moderation.command.reputation.plus")) {
                    if (args.length >= 2) {
                        Player player = plugin.getServer().getPlayer(args[1]);
                        if (sender instanceof Player) {
                            if (!plugin.hasSetReputation((Player) sender, player)) {
                                plugin.setGivenReputation((Player) sender, player, 1);
                                sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Gave " + player.getName() + " +1 rep.");
                            } else {
                                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You have already set reputation for that player.");
                            }
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to perform this command. Perhaps you should be using '/reputation set' instead?");
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /reputation plus [player]");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
                }
            } else if (args[0].equalsIgnoreCase("minus")) {
                if (sender.hasPermission("wayward.moderation.command.reputation.minus")) {
                    if (args.length >= 2) {
                        Player player = plugin.getServer().getPlayer(args[1]);
                        if (sender instanceof Player) {
                            if (!plugin.hasSetReputation((Player) sender, player)) {
                                plugin.setGivenReputation((Player) sender, player, -1);
                                sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Gave " + player.getName() + " -1 rep.");
                            } else {
                                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You have already set reputation for that player.");
                            }
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to perform this command. Perhaps you should be using '/reputation set' instead?");
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /reputation plus [player]");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /reputation [view|set|plus|minus]");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /reputation [view|set|plus|minus]");
        }
        return true;
    }

}
