package net.wayward_realms.waywardlib.portals;

import net.wayward_realms.waywardlib.WaywardPlugin;

import java.util.Collection;

/**
 * Represents a portals pluginWaywardPlugin
 *
 */
public interface PortalsPlugin extends WaywardPlugin {

    /**
     * Gets a collection of the portals
     *
     * @return a collection of the portals
     */
    public Collection<? extends Portal> getPortals();

    /**
     * Removes a portal
     *
     * @param portal the portal to remove
     */
    public void removePortal(Portal portal);

    /**
     * Adds a portal
     *
     * @param portal the portal to add
     */
    public void addPortal(Portal portal);

}
