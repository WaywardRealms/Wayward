package net.wayward_realms.waywardlib.util.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Here is where the magic is made.
 * <p>
 * Catch this event in order to have an effect on the player's name tag
 */
public final class PlayerNamePlateChangeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    /**
     * This is a Bukkit method. Don't touch me.
     *
     * @return registered handlers to Bukkit
     */
    public static HandlerList getHandlerList() {
        return PlayerNamePlateChangeEvent.handlers;
    }

    private Player player;
    private String skin;
    private String name;

    PlayerNamePlateChangeEvent(Player player, String skin, String name) {
        this.player = player;
        this.skin = skin;
        this.name = name;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerNamePlateChangeEvent.handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}