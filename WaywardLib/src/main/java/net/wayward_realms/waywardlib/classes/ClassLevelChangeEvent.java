package net.wayward_realms.waywardlib.classes;

import net.wayward_realms.waywardlib.character.Character;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a character's level changes
 * @author Lucariatias
 *
 */
public class ClassLevelChangeEvent extends ClassEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Character character;
    private final int oldLevel;
    private final int newLevel;
    private boolean cancel;

    public ClassLevelChangeEvent(final Character character, final Class clazz, final int oldLevel, final int newLevel) {
        super(clazz);
        this.character = character;
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * Gets the character involved in this event
     *
     * @return the character who is involved in this event
     */
    public final Character getCharacter() {
        return character;
    }

    /**
     * Gets the old level of the character
     *
     * @return the old level of the character
     */
    public final int getOldLevel() {
        return oldLevel;
    }

    /**
     * Gets the new level of the character
     *
     * @return the new (current) level of the character
     */
    public final int getNewLevel() {
        return newLevel;
    }

}
