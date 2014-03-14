package net.wayward_realms.waywardlib.events;

import net.wayward_realms.waywardlib.character.Character;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a dungeon is joined
 * @author Lucariatias
 *
 */
public class DungeonJoinEvent extends DungeonEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Dungeon dungeon;
    private final net.wayward_realms.waywardlib.character.Character character;
    private boolean cancel;

    public DungeonJoinEvent(final Dungeon dungeon, final Character character) {
        this.dungeon = dungeon;
        this.character = character;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Gets the dungeon being joined
     *
     * @return the dungeon
     */
    public Dungeon getDungeon() {
        return dungeon;
    }

    /**
     * Gets the character joining the dungeon
     *
     * @return the character
     */
    public Character getCharacter() {
        return character;
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
