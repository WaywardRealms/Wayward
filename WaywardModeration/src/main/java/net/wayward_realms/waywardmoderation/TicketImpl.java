package net.wayward_realms.waywardmoderation;

import net.wayward_realms.waywardlib.moderation.Ticket;
import net.wayward_realms.waywardlib.util.serialisation.SerialisableLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TicketImpl implements Ticket {

    private String reason;
    private String issuer;
    private String resolver;
    private Date openDate;
    private Date closeDate;
    private Location location;

    private TicketImpl() {}

    public TicketImpl(String reason, Player issuer) {
        this(reason, issuer, issuer.getLocation());
    }

    public TicketImpl(String reason, OfflinePlayer issuer, Location location) {
        this(reason, issuer, new Date(), location);
    }

    public TicketImpl(String reason, OfflinePlayer issuer, Date openDate, Location location) {
        this.reason = reason;
        this.issuer = issuer.getName();
        this.openDate = openDate;
        this.location = location;
    }

    @Override
    public String getReason() {
        return reason;
    }

    @Override
    public OfflinePlayer getIssuer() {
        return Bukkit.getOfflinePlayer(issuer);
    }

    @Override
    public OfflinePlayer getResolver() {
        return Bukkit.getOfflinePlayer(resolver);
    }

    @Override
    public Date getTime() {
        return getOpenDate();
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public Date getOpenDate() {
        return openDate;
    }

    @Override
    public Date getCloseDate() {
        return closeDate;
    }

    @Override
    public void close() {
        closeDate = new Date();
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialised = new HashMap<>();
        serialised.put("reason", reason);
        serialised.put("issuer", issuer);
        serialised.put("resolver", resolver);
        serialised.put("open-date", openDate);
        serialised.put("close-date", closeDate);
        serialised.put("location", new SerialisableLocation(location));
        return serialised;
    }

    public static TicketImpl deserialize(Map<String, Object> serialised) {
        TicketImpl deserialised = new TicketImpl();
        deserialised.reason = (String) serialised.get("reason");
        deserialised.issuer = (String) serialised.get("issuer");
        deserialised.resolver = (String) serialised.get("resolver");
        deserialised.openDate = (Date) serialised.get("open-date");
        deserialised.closeDate = (Date) serialised.get("close-date");
        deserialised.location = ((SerialisableLocation) serialised.get("location")).toLocation();
        return deserialised;
    }

}
