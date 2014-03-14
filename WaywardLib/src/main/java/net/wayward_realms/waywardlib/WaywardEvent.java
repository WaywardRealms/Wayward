package net.wayward_realms.waywardlib;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Represents a Vayleryn-specific eventWaywardPlugin
 *
 */
public abstract class WaywardEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
