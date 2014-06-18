package net.wayward_realms.waywardmoderation.ticket;

import net.wayward_realms.waywardlib.moderation.Ticket;
import net.wayward_realms.waywardlib.util.file.filter.YamlFileFilter;
import net.wayward_realms.waywardmoderation.WaywardModeration;
import org.bukkit.ChatColor;

import java.io.File;
import java.util.*;

public class TicketManager {

    private WaywardModeration plugin;

    public TicketManager(WaywardModeration plugin) {
        this.plugin = plugin;
    }

    public Collection<Ticket> getTickets() {
        File ticketsDirectory = new File(plugin.getDataFolder(), "tickets");
        List<Ticket> tickets = new ArrayList<>();
        if (ticketsDirectory.exists()) {
            for (File file : ticketsDirectory.listFiles(new YamlFileFilter())) {
                tickets.add(new TicketImpl(file));
            }
        }
        return tickets;
    }

    public Collection<Ticket> getTickets(TicketFilter filter) {
        Set<Ticket> tickets = new HashSet<>();
        for (Ticket ticket : getTickets()) {
            if (filter.accept(ticket)) {
                tickets.add(ticket);
            }
        }
        return tickets;
    }

    public void addTicket(Ticket ticket) {
        plugin.getServer().broadcast(plugin.getPrefix() + ChatColor.GREEN + "New ticket filed: #" + ticket.getId() + " " + ticket.getReason() + " (by " + ticket.getIssuer().getName() + ")", "wayward.moderation.ticket.notification");
        if (!(ticket instanceof TicketImpl)) new TicketImpl(plugin, ticket);
    }

    public void removeTicket(Ticket ticket) {
        File ticketsDirectory = new File(plugin.getDataFolder(), "tickets");
        File ticketFile = new File(ticketsDirectory, ticket.getId() + ".yml");
        ticketFile.delete();
    }

    public Ticket getTicket(int id) {
        File ticketDirectory = new File(plugin.getDataFolder(), "tickets");
        File ticketFile = new File(ticketDirectory, id + ".yml");
        return new TicketImpl(ticketFile);
    }

    public int getTicketId(Ticket ticket) {
        return ticket.getId();
    }


}
