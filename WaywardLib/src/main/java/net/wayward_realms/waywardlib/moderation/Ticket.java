package net.wayward_realms.waywardlib.moderation;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.Date;

/**
 * Represents a ticket
 *
 */
public interface Ticket extends ConfigurationSerializable {

    /**
     * Gets the ID
     *
     * @return the id
     */
    public int getId();

    /**
     * Sets the ID
     *
     * @param id the id to set
     */
    public void setId(int id);

    /**
     * Gets the reason the ticket was issued
     *
     * @return the reason the ticket was issued
     */
    public String getReason();

    /**
     * Sets the reason the ticket was issued
     *
     * @param reason the reason the ticket was issued
     */
    public void setReason(String reason);

    /**
     * Gets the issuer of the ticket
     *
     * @return the issuer
     */
    public OfflinePlayer getIssuer();

    /**
     * Sets the issuer of the ticket
     *
     * @param issuer the issuer
     */
    public void setIssuer(OfflinePlayer issuer);

    /**
     * Gets the person that resolved the ticket
     *
     * @return the resolver
     */
    public OfflinePlayer getResolver();

    /**
     * Sets the person that resolved the ticket
     *
     * @param resolver the player who resolved the ticket
     */
    public void setResolver(OfflinePlayer resolver);

    /**
     * Gets the location the ticket was filed at
     *
     * @return the location where the ticket was filed
     */
    public Location getLocation();

    /**
     * Sets the location the ticket was filed at
     *
     * @param location the location where the ticket was filed
     */
    public void setLocation(Location location);

    /**
     * Gets the date the ticket was opened
     *
     * @return the date the ticket was opened
     */
    public Date getOpenDate();

    /**
     * Sets the date the ticket was opened
     *
     * @param date the date the ticket was opened
     */
    public void setOpenDate(Date date);

    /**
     * Gets the date the ticket was closed
     *
     * @return the date the ticket was closed
     */
    public Date getCloseDate();

    /**
     * Sets the date the issue was closed
     *
     * @param date the date the ticket was closed
     */
    public void setCloseDate(Date date);

    /**
     * Closes the ticket
     *
     * @param resolver the player who resolved the ticket
     */
    public void close(OfflinePlayer resolver);

    /**
     * Reopens the ticket after being closed
     */
    public void reopen();

    public boolean isClosed();

}
