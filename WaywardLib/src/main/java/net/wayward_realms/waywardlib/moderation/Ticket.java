package net.wayward_realms.waywardlib.moderation;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.Date;

/**
 * Represents a ticket
 * 
 * @author Lucariatias
 *
 */
public interface Ticket extends ConfigurationSerializable {

    /**
     * Gets the reason the ticket was issued
     *
     * @return the reason the ticket was issued
     */
    public String getReason();

    /**
     * Gets the issuer of the warning
     *
     * @return the issuer
     */
    public OfflinePlayer getIssuer();

    /**
     * Gets the person that resolved the ticket
     *
     * @return the resolver
     */
    public OfflinePlayer getResolver();

    /**
     * Gets the time the ticket was filed
     *
     * @return the time the ticket was filed
     * @deprecated inconsistent
     * @see Ticket#getOpenDate
     */
    @Deprecated
    public Date getTime();

    /**
     * Gets the location the ticket was filed at
     *
     * @return the location where the ticket was filed
     */
    public Location getLocation();

    /**
     * Gets the date the ticket was opened
     *
     * @return the date the ticket was opened
     */
    public Date getOpenDate();

    /**
     * Gets the date the ticket was closed
     *
     * @return the date the ticket was closed
     */
    public Date getCloseDate();

    /**
     * Closes the ticket
     */
    public void close();

}
