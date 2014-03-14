package net.wayward_realms.waywardlib.death;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.HandlerList;

/**
 * Called when a character is revived from a PvE death
 * @author Lucariatias
 *
 */
public class DeathReviveEvent extends DeathEvent {

    private static final HandlerList handlers = new HandlerList();

    private final OfflinePlayer player;

    public DeathReviveEvent(final OfflinePlayer player) {
        this.player = player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Gets the player being revived
     *
     * @return the player
     */
    public OfflinePlayer getPlayer() {
        return player;
    }

}
