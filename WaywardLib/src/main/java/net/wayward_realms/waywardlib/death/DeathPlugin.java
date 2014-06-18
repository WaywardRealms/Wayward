package net.wayward_realms.waywardlib.death;

import net.wayward_realms.waywardlib.WaywardPlugin;
import net.wayward_realms.waywardlib.character.Character;
import org.bukkit.OfflinePlayer;

/**
 * Represents a death management plugin
 *
 */
public interface DeathPlugin extends WaywardPlugin {

    /**
     * Restores the items a player lost on death back to the player and returns them to the point of death
     *
     * @param player the player
     */
    public void wake(OfflinePlayer player);

    /**
     * Checks whether a player's active character is unconscious
     *
     * @param player the player
     * @return whether the player's active character is unconscious
     */
    public boolean isUnconscious(OfflinePlayer player);

    /**
     * Restores the items a character lost on death back to the character, and if active, returns them to the point of death
     *
     * @param character the character
     */
    public void wake(Character character);

    /**
     * Checks whether a character is unconscious
     *
     * @param character the character
     * @return whether the character is unconscious
     */
    public boolean isUnconscious(Character character);

}
