package net.wayward_realms.waywardlib.travel;

import net.wayward_realms.waywardlib.WaywardEvent;
import org.bukkit.event.HandlerList;

/**
 * Represents a portal related eventWaywardPlugin
 *
 */
public abstract class PortalEvent extends WaywardEvent {

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
