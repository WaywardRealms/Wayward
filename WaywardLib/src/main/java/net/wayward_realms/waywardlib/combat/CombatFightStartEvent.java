package net.wayward_realms.waywardlib.combat;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a fight startsWaywardPlugin
 *
 */
public class CombatFightStartEvent extends CombatEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Fight fight;
    private boolean cancel;

    public CombatFightStartEvent(final Fight fight) {
        super();
        this.fight = fight;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Gets the fight about to start
     *
     * @return the fight
     */
    public Fight getFight() {
        return fight;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

}
