package net.wayward_realms.waywardlib.character;

import net.wayward_realms.waywardlib.WaywardEvent;
import org.bukkit.event.HandlerList;

/**
 * Represents an event involving a character
 *
 */
public abstract class CharacterEvent extends WaywardEvent {

    private static final HandlerList handlers = new HandlerList();

    private final net.wayward_realms.waywardlib.character.Character character;

    public CharacterEvent(Character character) {
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
     * Gets the character involved in this event
     *
     * @return the character who is involved in this event
     */
    public final Character getCharacter() {
        return character;
    }

}
