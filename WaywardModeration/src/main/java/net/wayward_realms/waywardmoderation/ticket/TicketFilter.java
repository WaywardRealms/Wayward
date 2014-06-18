package net.wayward_realms.waywardmoderation.ticket;

import net.wayward_realms.waywardlib.moderation.Ticket;

public interface TicketFilter {

    public boolean accept(Ticket ticket);

}
