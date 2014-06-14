package net.wayward_realms.waywardlib.travel;

import net.wayward_realms.waywardlib.WaywardPlugin;

import java.util.Collection;

/**
 * Represents a travel plugin
 *
 */
public interface TravelPlugin extends WaywardPlugin {

    /**
     * Gets a collection of the travel
     *
     * @return a collection of the travel
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
