package net.wayward_realms.waywardmoderation;

import net.wayward_realms.waywardlib.moderation.Ticket;
import org.bukkit.OfflinePlayer;

import java.util.*;

public class TicketManager {

    private List<Ticket> tickets = new ArrayList<>();

    public Collection<Ticket> getTickets() {
        return tickets;
    }

    public Collection<Ticket> getTickets(OfflinePlayer player) {
        Set<Ticket> tickets = new HashSet<>();
        for (Ticket ticket : this.tickets) {
            if (ticket.getIssuer().getName().equalsIgnoreCase(player.getName())) {
                tickets.add(ticket);
            }
        }
        return tickets;
    }

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
    }

    public void removeTicket(Ticket ticket) {
        tickets.remove(ticket);
    }

    public Ticket getTicket(int id) {
        return tickets.get(id);
    }

    public int getTicketId(Ticket ticket) {
        for (int i = 0; i < tickets.size() - 1; i++) {
            if (tickets.get(i) == ticket) {
                return i;
            }
        }
        return -1;
    }


}
