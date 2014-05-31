package net.wayward_realms.waywardmoderation.ticket;

import net.wayward_realms.waywardlib.moderation.Ticket;
import org.bukkit.OfflinePlayer;

public class IssuerTicketFilter implements TicketFilter {

    private String playerName;

    public IssuerTicketFilter(OfflinePlayer player) {
        this.playerName = player.getName();
    }

    @Override
    public boolean accept(Ticket ticket) {
        return ticket.getIssuer().getName().equalsIgnoreCase(playerName);
    }
}
