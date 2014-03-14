package net.wayward_realms.waywardlib.essentials;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a kit is claimed
 * @author Lucariatias
 *
 */
public class EssentialsKitClaimEvent extends EssentialsEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private Kit kit;
    private boolean cancel;

    public EssentialsKitClaimEvent(final Player player, Kit kit) {
        this.player = player;
        this.kit = kit;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Gets the player claiming the kit
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the kit being claimed
     *
     * @return the kit
     */
    public Kit getKit() {
        return kit;
    }

    /**
     * Sets the kit to claim
     *
     * @param kit the kit to set
     */
    public void setKit(Kit kit) {
        this.kit = kit;
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
