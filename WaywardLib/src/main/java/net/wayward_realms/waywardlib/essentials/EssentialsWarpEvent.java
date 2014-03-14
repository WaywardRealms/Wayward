package net.wayward_realms.waywardlib.essentials;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a player warpsWaywardPlugin
 *
 */
public class EssentialsWarpEvent extends EssentialsEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Location warpFrom;
    private Location warpTo;
    private boolean cancel;

    public EssentialsWarpEvent(final Player player, final Location warpFrom, final Location warpTo) {
        this.player = player;
        this.warpFrom = warpFrom;
        this.warpTo = warpTo;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Gets the player warping
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the location the player is warping from
     *
     * @return the location the player is warping from
     */
    public Location getWarpFrom() {
        return warpFrom;
    }

    /**
     * Gets the location the player is warping to
     *
     * @return the location the player is warping to
     */
    public Location getWarpTo() {
        return warpTo;
    }

    /**
     * Sets the location the player should warp to
     *
     * @param warpTo the place the player should warp to
     */
    public void setWarpTo(Location warpTo) {
        this.warpTo = warpTo;
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
