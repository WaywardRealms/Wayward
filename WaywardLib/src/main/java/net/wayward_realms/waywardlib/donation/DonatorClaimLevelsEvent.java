package net.wayward_realms.waywardlib.donation;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a donator claims levels
 * @author Lucariatias
 *
 */
public class DonatorClaimLevelsEvent extends DonatorEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private int levels;
    private boolean cancel;

    public DonatorClaimLevelsEvent(final Player donator, int levels) {
        super(donator);
        this.levels = levels;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Gets the levels being given to the donator
     *
     * @return the levels being given to the donator
     */
    public int getLevels() {
        return levels;
    }

    /**
     * Sets the amount of levels being given to the donator
     *
     * @param levels the levels to set
     */
    public void setLevels(int levels) {
        this.levels = levels;
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
