package net.wayward_realms.waywardlib.donation;

import net.wayward_realms.waywardlib.WaywardEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * Represents a donator related event
 * @author Lucariatias
 *
 */
public abstract class DonatorEvent extends WaywardEvent {

    private static final HandlerList handlers = new HandlerList();

    private final Player donator;

    public DonatorEvent(final Player donator) {
        this.donator = donator;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Gets the donator involved in this event
     *
     * @return the donator involved in this event
     */
    public final Player getDonator() {
        return donator;
    }

}
