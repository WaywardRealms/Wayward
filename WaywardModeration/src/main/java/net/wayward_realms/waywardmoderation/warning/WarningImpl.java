package net.wayward_realms.waywardmoderation.warning;

import net.wayward_realms.waywardlib.moderation.Warning;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WarningImpl implements Warning {

    private String reason;
    private UUID player;
    private UUID issuer;
    private Date time;

    private WarningImpl() {}

    public WarningImpl(String reason, OfflinePlayer player, OfflinePlayer issuer) {
        this(reason, player, issuer, new Date());
    }

    public WarningImpl(String reason, OfflinePlayer player, OfflinePlayer issuer, Date time) {
        this.reason = reason;
        this.player = player.getUniqueId();
        this.issuer = issuer.getUniqueId();
        this.time = time;
    }

    @Override
    public String getReason() {
        return reason;
    }

    @Override
    public OfflinePlayer getPlayer() {
        return Bukkit.getOfflinePlayer(player);
    }

    @Override
    public OfflinePlayer getIssuer() {
        return Bukkit.getOfflinePlayer(issuer);
    }

    @Override
    public Date getTime() {
        return time;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialised = new HashMap<>();
        serialised.put("reason", reason);
        serialised.put("player-uuid", player.toString());
        serialised.put("issuer-uuid", issuer.toString());
        serialised.put("time", time);
        return serialised;
    }

    public static WarningImpl deserialize(Map<String, Object> serialised) {
        WarningImpl deserialised = new WarningImpl();
        deserialised.reason = (String) serialised.get("reason");
        deserialised.player = serialised.containsKey("player-uuid") ? Bukkit.getOfflinePlayer((String) serialised.get("player")).getUniqueId() : UUID.fromString((String) serialised.get("player-uuid"));
        deserialised.issuer = serialised.containsKey("issuer-uuid") ? Bukkit.getOfflinePlayer((String) serialised.get("issuer")).getUniqueId() : UUID.fromString((String) serialised.get("issuer-uuid"));
        deserialised.time = (Date) serialised.get("time");
        return deserialised;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Warning)) return false;
        Warning warning = (Warning) obj;
        return warning.getPlayer().getName().equals(getPlayer().getName())
                && warning.getReason().equals(getReason())
                && warning.getIssuer().getName().equals(getIssuer().getName())
                && warning.getTime().equals(getTime());
    }

}
