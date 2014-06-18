package net.wayward_realms.waywardmoderation.ticket;

import net.wayward_realms.waywardlib.moderation.Ticket;
import net.wayward_realms.waywardlib.util.serialisation.SerialisableLocation;
import net.wayward_realms.waywardmoderation.WaywardModeration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TicketImpl implements Ticket {

    private File file;

    private static int nextId = 0;

    public TicketImpl(File file) {
        this.file = file;
    }

    public TicketImpl(WaywardModeration plugin, Ticket ticket) {
        int id = ticket.getId();
        File ticketDirectory = new File(plugin.getDataFolder(), "tickets");
        this.file = new File(ticketDirectory, id + ".yml");
        setId(id);
        setReason(ticket.getReason());
        setIssuer(ticket.getIssuer());
        setResolver(ticket.getResolver());
        setLocation(ticket.getLocation());
        setOpenDate(ticket.getOpenDate());
        setCloseDate(ticket.getCloseDate());

    }

    public static int nextAvailableId() {
        nextId++;
        return nextId;
    }

    public static int getNextId() {
        return nextId;
    }

    public static void setNextId(int id) {
        TicketImpl.nextId = id;
    }

    private TicketImpl() {}

    public TicketImpl(WaywardModeration plugin, String reason, Player issuer) {
        this(plugin, reason, issuer, issuer.getLocation());
    }

    public TicketImpl(WaywardModeration plugin, String reason, OfflinePlayer issuer, Location location) {
        this(plugin, reason, issuer, new Date(), location);
    }

    public TicketImpl(WaywardModeration plugin, String reason, OfflinePlayer issuer, Date openDate, Location location) {
        int id = nextAvailableId();
        File ticketDirectory = new File(plugin.getDataFolder(), "tickets");
        this.file = new File(ticketDirectory, id + ".yml");
        setId(id);
        setReason(reason);
        setIssuer(issuer);
        setOpenDate(openDate);
        setLocation(location);
    }

    private void setFieldValue(String field, Object value) {
        YamlConfiguration save = YamlConfiguration.loadConfiguration(file);
        save.set("ticket." + field, value);
        try {
            save.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private Object getFieldValue(String field) {
        YamlConfiguration save = YamlConfiguration.loadConfiguration(file);
        return save.get("ticket." + field);
    }

    private int getFieldIntValue(String field) {
        YamlConfiguration save = YamlConfiguration.loadConfiguration(file);
        return save.getInt("ticket." + field);
    }

    private long getFieldLongValue(String field) {
        YamlConfiguration save = YamlConfiguration.loadConfiguration(file);
        return save.getLong("ticket." + field);
    }

    private double getFieldDoubleValue(String field) {
        YamlConfiguration save = YamlConfiguration.loadConfiguration(file);
        return save.getDouble("ticket." + field);
    }

    private boolean getFieldBooleanValue(String field) {
        YamlConfiguration save = YamlConfiguration.loadConfiguration(file);
        return save.getBoolean("ticket." + field);
    }

    private String getFieldStringValue(String field) {
        YamlConfiguration save = YamlConfiguration.loadConfiguration(file);
        return save.getString("ticket." + field);
    }

    @Override
    public int getId() {
        return getFieldIntValue("id");
    }

    @Override
    public void setId(int id) {
        setFieldValue("id", id);
    }

    @Override
    public String getReason() {
        return getFieldStringValue("reason");
    }

    @Override
    public void setReason(String reason) {
        setFieldValue("reason", reason);
    }

    @Override
    public OfflinePlayer getIssuer() {
        if (getFieldValue("issuer-uuid") == null) {
            setFieldValue("issuer-uuid", Bukkit.getOfflinePlayer(getFieldStringValue("issuer")).getUniqueId().toString());
            setFieldValue("issuer", null);
        }
        return Bukkit.getOfflinePlayer(UUID.fromString(getFieldStringValue("issuer-uuid")));
    }

    @Override
    public void setIssuer(OfflinePlayer issuer) {
        setFieldValue("issuer-uuid", issuer.getUniqueId());
    }

    @Override
    public OfflinePlayer getResolver() {
        if (getFieldValue("resolver-uuid") == null) {
            setFieldValue("resolver-uuid", Bukkit.getOfflinePlayer(getFieldStringValue("resolver")).getUniqueId().toString());
            setFieldValue("resolver", null);
        }
        return Bukkit.getOfflinePlayer(UUID.fromString(getFieldStringValue("resolver-uuid")));
    }

    @Override
    public void setResolver(OfflinePlayer resolver) {
        setFieldValue("resolver-uuid", resolver.getUniqueId().toString());
    }

    @Override
    public Location getLocation() {
        return ((SerialisableLocation) getFieldValue("location")).toLocation();
    }

    @Override
    public void setLocation(Location location) {
        setFieldValue("location", new SerialisableLocation(location));
    }

    @Override
    public Date getOpenDate() {
        return (Date) getFieldValue("open-date");
    }

    @Override
    public void setOpenDate(Date date) {
        setFieldValue("open-date", date);
    }

    @Override
    public Date getCloseDate() {
        return (Date) getFieldValue("close-date");
    }

    @Override
    public void setCloseDate(Date date) {
        setFieldValue("close-date", date);
    }

    @Override
    public void close(OfflinePlayer resolver) {
        setFieldValue("closed", true);
        setResolver(resolver);
        setCloseDate(new Date());
    }

    @Override
    public void reopen() {
        setFieldValue("closed", false);
    }

    @Override
    public boolean isClosed() {
        return getFieldBooleanValue("closed");
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialised = new HashMap<>();
        serialised.put("reason", getReason());
        serialised.put("issuer", getIssuer().getUniqueId().toString());
        serialised.put("resolver", getResolver().getUniqueId().toString());
        serialised.put("open-date", getOpenDate());
        serialised.put("close-date", getCloseDate());
        serialised.put("location", new SerialisableLocation(getLocation()));
        return serialised;
    }

    public static TicketImpl deserialize(Map<String, Object> serialised) {
        TicketImpl deserialised = new TicketImpl();
        if (deserialised.getId() > nextId) {
            nextId = deserialised.getId();
        }
        deserialised.setReason((String) serialised.get("reason"));
        deserialised.setIssuer(Bukkit.getOfflinePlayer(UUID.fromString((String) serialised.get("issuer"))));
        deserialised.setResolver(Bukkit.getOfflinePlayer(UUID.fromString((String) serialised.get("resolver"))));
        deserialised.setOpenDate((Date) serialised.get("open-date"));
        deserialised.setCloseDate((Date) serialised.get("close-date"));
        deserialised.setLocation(((SerialisableLocation) serialised.get("location")).toLocation());
        return deserialised;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Ticket)) return false;
        Ticket ticket = (Ticket) obj;
        return ticket.getId() == getId();
    }

}
