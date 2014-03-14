package net.wayward_realms.waywardlib.portals;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a player destroys a portal
 * @author Lucariatias
 *
 */
public class PortalDestroyEvent extends PortalEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Portal portal;
    private boolean cancel;

    public PortalDestroyEvent(final Player player, final Portal portal) {
        this.player = player;
        this.portal = portal;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Gets the player destroying the portal
     *
     * @return the player destroying the portal
     */
    public final Player getPlayer() {
        return player;
    }

    /**
     * Gets the portal being destroyed
     *
     * @return the portal to be destroyed
     */
    public final Portal getPortal() {
        return portal;
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
