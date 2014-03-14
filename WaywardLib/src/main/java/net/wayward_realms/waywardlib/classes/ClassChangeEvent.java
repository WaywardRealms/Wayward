package net.wayward_realms.waywardlib.classes;

import net.wayward_realms.waywardlib.character.Character;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a character's combat class is changed
 * @author Lucariatias
 *
 */
public class ClassChangeEvent extends ClassEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final net.wayward_realms.waywardlib.character.Character character;
    private final Class oldClass;
    private boolean cancel;

    public ClassChangeEvent(final Character character, final Class oldClass, final Class clazz) {
        super(clazz);
        this.character = character;
        this.oldClass = oldClass;
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
     * @return the character involved in this event
     */
    public Character getCharacter() {
        return character;
    }

    /**
     * Gets the old class of the player involved in this event
     *
     * @return the old class of the player
     */
    public Class getOldClass() {
        return oldClass;
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
