package net.wayward_realms.waywardmoderation.warning;

import net.wayward_realms.waywardlib.moderation.Warning;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WarningImpl implements Warning {

    private String reason;
    private String player;
    private String issuer;
    private Date time;

    private WarningImpl() {}

    public WarningImpl(String reason, OfflinePlayer player, OfflinePlayer issuer) {
        this(reason, player, issuer, new Date());
    }

    public WarningImpl(String reason, OfflinePlayer player, OfflinePlayer issuer, Date time) {
        this.reason = reason;
        this.player = player.getName();
        this.issuer = issuer.getName();
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
        serialised.put("player", player);
        serialised.put("issuer", issuer);
        serialised.put("time", time);
        return serialised;
    }

    public static WarningImpl deserialize(Map<String, Object> serialised) {
        WarningImpl deserialised = new WarningImpl();
        deserialised.reason = (String) serialised.get("reason");
        deserialised.player = (String) serialised.get("player");
        deserialised.issuer = (String) serialised.get("issuer");
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
