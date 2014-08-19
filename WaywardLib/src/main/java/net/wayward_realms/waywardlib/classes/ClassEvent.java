package net.wayward_realms.waywardlib.classes;

import net.wayward_realms.waywardlib.WaywardEvent;
import org.bukkit.event.HandlerList;

/**
 * Represents a class related event
 *
 * @deprecated classes are going to be removed
 */
@Deprecated
public abstract class ClassEvent extends WaywardEvent {

    private static final HandlerList handlers = new HandlerList();

    private final Class clazz;

    /**
     * Default constructor
     *
     * @param clazz the class
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public ClassEvent(final Class clazz) {
        this.clazz = clazz;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Gets the class involved in this event
     *
     * @return the class involved in this event
     * @deprecated classes are going to be removed
     */
    @Deprecated
    public final Class getClazz() {
        return clazz;
    }

}
