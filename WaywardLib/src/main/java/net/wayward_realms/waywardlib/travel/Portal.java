package net.wayward_realms.waywardlib.travel;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

/**
 * Represents a portalWaywardPlugin
 *
 */
public interface Portal extends ConfigurationSerializable {

    /**
     * Gets the location the portal teleports to
     *
     * @return the location
     */
    public Location getTeleportLocation();

    /**
     * Sets the location the portal should teleport to
     *
     * @param teleportLocation the location to teleport to
     */
    public void setTeleportLocation(Location teleportLocation);

    /**
     * Gets the minimum location of the portal
     *
     * @return the minimum location of the portal
     */
    public Location getMinLocation();

    /**
     * Sets the minimum location of the portal
     *
     * @param minLocation the minimum location of the portal
     */
    public void setMinLocation(Location minLocation);

    /**
     * Gets the maximum location of the portal
     *
     * @return the maximum location of the portal
     */
    public Location getMaxLocation();

    /**
     * Sets the maximum location of the portal
     *
     * @param maxLocation the maximum location of the portal
     */
    public void setMaxLocation(Location maxLocation);

}
