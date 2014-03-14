package net.wayward_realms.waywardlib.death;

import net.wayward_realms.waywardlib.WaywardEvent;
import org.bukkit.event.HandlerList;

/**
 * Represents a death related eventWaywardPlugin
 *
 */
public abstract class DeathEvent extends WaywardEvent {

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
