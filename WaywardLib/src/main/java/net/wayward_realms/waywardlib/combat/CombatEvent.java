package net.wayward_realms.waywardlib.combat;

import net.wayward_realms.waywardlib.WaywardEvent;
import org.bukkit.event.HandlerList;

/**
 * Represents a combat related eventWaywardPlugin
 *
 */
public abstract class CombatEvent extends WaywardEvent {

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
