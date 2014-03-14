package net.wayward_realms.waywardlib.moderation;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.Date;

/**
 * Represents a warning
 * WaywardPlugin
 *
 */
public interface Warning extends ConfigurationSerializable {

    /**
     * Gets the reason the warning was issued
     *
     * @return the reason the warning was issued
     */
    public String getReason();

    /**
     * Gets the player the warning was issued to
     *
     * @return the player
     */
    public OfflinePlayer getPlayer();

    /**
     * Gets the issuer of the warning
     *
     * @return the issuer
     */
    public OfflinePlayer getIssuer();

    /**
     * Gets the time the warning was issued
     *
     * @return the time the warning was issued
     */
    public Date getTime();

}
