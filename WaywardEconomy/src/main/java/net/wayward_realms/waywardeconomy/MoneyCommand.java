package net.wayward_realms.waywardeconomy;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.economy.Currency;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MoneyCommand implements CommandExecutor {

    private WaywardEconomy plugin;

    public MoneyCommand(WaywardEconomy plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("pay")) {
                if (args.length >= 3) {
                    if (plugin.getServer().getPlayer(args[1]) != null) {
                        Player player = plugin.getServer().getPlayer(args[1]);
                        if (((Player) sender).getWorld().equals(player.getWorld())) {
                            if (((Player) sender).getLocation().distanceSquared(player.getLocation()) <= 64) {
                                Currency currency = plugin.getPrimaryCurrency();
                                if (args.length >= 4) {
                                    if (plugin.getCurrency(args[3]) != null) {
                                        currency = plugin.getCurrency(args[3]);
                                    }
                                }
                                try {
                                    if (Integer.parseInt(args[2]) > 0) {
                                        if (plugin.getMoney(player) + Integer.parseInt(args[2]) <= plugin.getMaximumMoney()) {
                                            plugin.transferMoney((Player) sender, player, currency, Integer.parseInt(args[2]));
                                            sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + ((Player) sender).getDisplayName() + ChatColor.GREEN + " gave " + args[2] + " " + (Integer.parseInt(args[2]) == 1 ? currency.getNameSingular() : currency.getNamePlural()) + " to " + player.getDisplayName());
                                            player.sendMessage(plugin.getPrefix() + ChatColor.GREEN + ((Player) sender).getDisplayName() + ChatColor.GREEN + " gave " + args[2] + " " + (Integer.parseInt(args[2]) == 1 ? currency.getNameSingular() : currency.getNamePlural()) + " to " + player.getDisplayName());
                                        } else {
                                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + player.getDisplayName() + " does not have enough room in their wallet to fit this much money.");
                                        }
                                    } else {
                                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must pay at least 1 " + currency.getNameSingular() + "!");
                                    }
                                } catch (NumberFormatException exception) {
                                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify a number for the amount of money to send.");
                                }
                            } else {
                                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + ((Player) sender).getDisplayName() + ChatColor.RED + " must be standing a little closer to " + player.getDisplayName() + ChatColor.RED + " in order to give them any money.");
                            }
                        }

                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /money pay [player] [amount] (currency)");
                    }
                }
            } else if (args[0].equalsIgnoreCase("set")) {
                if (sender.hasPermission("wayward.economy.command.money.set")) {
                    if (args.length >= 3) {
                        if (plugin.getServer().getPlayer(args[1]) != null) {
                            Player player = plugin.getServer().getPlayer(args[1]);
                            Currency currency = plugin.getPrimaryCurrency();
                            if (args.length >= 4) {
                                if (plugin.getCurrency(args[3]) != null) {
                                    currency = plugin.getCurrency(args[3]);
                                }
                            }
                            try {
                                plugin.setMoney(player, currency, Math.min(Integer.parseInt(args[2]), plugin.getMaximumMoney()));
                                sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Set " + player.getDisplayName() + ChatColor.GREEN + "'s " + currency.getNameSingular() + " balance to " + args[2]);
                                player.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Your " + currency.getNameSingular() + " balance was set to " + args[2]);
                            } catch (NumberFormatException exception) {
                                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify a number for the amount of money to set.");
                            }
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That player is not online.");
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /money set [player] [amount] (currency)");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
                }
            } else if (args[0].equalsIgnoreCase("add")) {
                if (sender.hasPermission("wayward.economy.command.money.set")) {
                    if (args.length >= 3) {
                        if (plugin.getServer().getPlayer(args[1]) != null) {
                            Player player = plugin.getServer().getPlayer(args[1]);
                            Currency currency = plugin.getPrimaryCurrency();
                            if (args.length >= 4) {
                                if (plugin.getCurrency(args[3]) != null) {
                                    currency = plugin.getCurrency(args[3]);
                                }
                            }
                            try {
                                plugin.addMoney(player, currency, Integer.parseInt(args[2]));
                                plugin.setMoney(player, currency, Math.min(plugin.getMoney(player), plugin.getMaximumMoney()));
                                sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Set " + player.getDisplayName() + ChatColor.GREEN + "'s " + currency.getNameSingular() + " balance to " + plugin.getMoney(player));
                                player.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Your " + currency.getNameSingular() + " balance was set to " + plugin.getMoney(player));
                            } catch (NumberFormatException exception) {
                                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify a number for the amount of money to add.");
                            }
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That player is not online.");
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /money add [player] [amount] (currency)");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
                }
            } else if (args[0].equalsIgnoreCase("top")) {
                sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Top balances: ");
                int i = 1;
                for (Character character : plugin.getRichestCharacters()) {
                    if (character != null) {
                        sender.sendMessage(ChatColor.GRAY + "" + i + ". " + (character.isDead() ? ChatColor.RED : ChatColor.GREEN) + (character.isNameHidden() ? ChatColor.MAGIC + character.getName() + ChatColor.RESET : character.getName()) + ChatColor.GRAY + " (" + (character.isDead() ? ChatColor.RED + "Dead" : ChatColor.GREEN + "Alive") + ChatColor.GRAY + "): " + (plugin.getMoney(character) + plugin.getBankBalance(character)) + " " + (plugin.getMoney(character) == 1 ? plugin.getPrimaryCurrency().getNameSingular() : plugin.getPrimaryCurrency().getNamePlural()));
                    } else {
                        break;
                    }
                    i++;
                }
            } else if (plugin.getCurrency(args[0]) != null) {
                Currency currency = plugin.getCurrency(args[0]);
                sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + currency.getNameSingular() + " balance: " + plugin.getMoney((Player) sender, currency));
            } else if (plugin.getServer().getPlayer(args[0]) != null) {
                Player player = plugin.getServer().getPlayer(args[0]);
                sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Balance: ");
                for (Currency currency : plugin.getCurrencies()) {
                    sender.sendMessage(ChatColor.GREEN + "" + plugin.getMoney(player, currency) + " " + (plugin.getMoney(player, currency) == 1 ? currency.getNameSingular() : currency.getNamePlural()));
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Unknown command!");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Balance: ");
            for (Currency currency : plugin.getCurrencies()) {
                sender.sendMessage(ChatColor.GREEN + "" + plugin.getMoney((Player) sender, currency) + " " + (plugin.getMoney((Player) sender, currency) == 1 ? currency.getNameSingular() : currency.getNamePlural()));
            }
        }
        return true;
    }
}
