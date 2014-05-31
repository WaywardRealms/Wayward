package net.wayward_realms.waywardmoderation.ticket;

import net.wayward_realms.waywardlib.moderation.Ticket;

public class OpenTicketFilter implements TicketFilter {

    private boolean open;

    public OpenTicketFilter(boolean open) {
        this.open = open;
    }

    @Override
    public boolean accept(Ticket ticket) {
        return open ? !ticket.isClosed() : ticket.isClosed();
    }
}
