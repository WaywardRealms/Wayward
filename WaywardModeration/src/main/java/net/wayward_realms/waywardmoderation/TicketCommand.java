package net.wayward_realms.waywardmoderation;

import net.wayward_realms.waywardlib.moderation.Ticket;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TicketCommand implements CommandExecutor {

    private final WaywardModeration plugin;

    public TicketCommand(WaywardModeration plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("create")) {
                if (sender instanceof Player) {
                    if (args.length > 1) {
                        Player player = (Player) sender;
                        StringBuilder reason = new StringBuilder();
                        for (int i = 1; i < args.length; i++) {
                            reason.append(args[i]).append(" ");
                        }
                        Ticket ticket = new TicketImpl(reason.toString(), player);
                        plugin.addTicket(player, ticket);
                        sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Ticket filed: " + ChatColor.YELLOW + '#' + plugin.getTicketId(ticket) + ChatColor.GREEN + ' ' + ticket.getReason());
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify a reason for filing the ticket.");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to perform this command.");
                }
            } else if (args[0].equalsIgnoreCase("close")) {
                if (args.length > 1) {
                    try {
                        Ticket ticket = plugin.getTicket(Integer.parseInt(args[1]));
                        if (sender.hasPermission("wayward.moderation.command.ticket.close") || ticket.getIssuer().getName().equalsIgnoreCase(sender.getName())) {
                            ticket.close();
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Ticket closed.");
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
                        }
                    } catch (NumberFormatException exception) {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Could not parse ticket ID, make sure it's an integer!");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify the ID of the ticket you want to close.");
                }
            } else if (args[0].equalsIgnoreCase("teleport")) {
                if (sender.hasPermission("wayward.moderation.command.ticket.teleport")) {
                    if (args.length > 1) {
                        if (sender instanceof Player) {
                            Player player = (Player) sender;
                            try {
                                Ticket ticket = plugin.getTicket(Integer.parseInt(args[1]));
                                player.teleport(ticket.getLocation());
                                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Teleported to ticket filing location.");
                            } catch (NumberFormatException exception) {
                                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Could not parse ticket ID, make sure it's an integer!");
                            }
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to perform this command.");
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify the ID of the ticket you want to teleport to.");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /ticket [create|close|teleport]");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /ticket [create|close|teleport]");
        }
        return true;
    }
}
