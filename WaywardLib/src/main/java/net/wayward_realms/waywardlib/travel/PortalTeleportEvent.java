package net.wayward_realms.waywardlib.travel;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a player teleports using a portalWaywardPlugin
 *
 */
public class PortalTeleportEvent extends PortalEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Portal from;
    private Portal to;
    private boolean cancel;

    public PortalTeleportEvent(final Player player, final Portal from, Portal to) {
        this.player = player;
        this.from = from;
        this.to = to;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Gets the player being teleported
     *
     * @return the player being teleported
     */
    public final Player getPlayer() {
        return player;
    }

    /**
     * Gets the portal the player is stepping into
     *
     * @return the portal the player is stepping into
     */
    public final Portal getFrom() {
        return from;
    }

    /**
     * Gets the portal the player is teleporting to
     *
     * @return the portal the player is being teleported to
     */
    public Portal getTo() {
        return to;
    }

    /**
     * Sets the portal for the player to be teleported to
     *
     * @param to the portal to teleport the player to
     */
    public void setTo(Portal to) {
        this.to = to;
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
