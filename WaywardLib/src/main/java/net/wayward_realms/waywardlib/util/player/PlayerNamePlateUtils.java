package net.wayward_realms.waywardlib.util.player;

import net.wayward_realms.waywardlib.Wayward;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Utility class for changing player name plates
 */
public class PlayerNamePlateUtils {

    private static PlayerDisplayModifier factory;

    /**
     * Initialises the {@link net.wayward_realms.waywardlib.util.player.PlayerDisplayModifier} factory
     *
     * @param plugin the plugin instance
     */
    public static void init(Wayward plugin) {
        factory = new PlayerDisplayModifier(plugin);
    }

    /**
     * Refreshes a player's name plate by firing a {@link net.wayward_realms.waywardlib.util.player.PlayerNamePlateChangeEvent} and changing display name and skin accordingly
     *
     * @param player the player to refresh
     */
    public static void refreshPlayer(Player player) {
        final PlayerNamePlateChangeEvent event = new PlayerNamePlateChangeEvent(player, player.getName(), player.getName());
        Bukkit.getPluginManager().callEvent(event);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getServer().getPluginManager().getPlugin("WaywardLib"), new Runnable() {
            @Override
            public void run() {
                factory.changeDisplay(event.getPlayer(), event.getSkin(), event.getName());
            }
        });
    }

    /**
     * Invalidates a player's cached profile
     *
     * @param player the player to invalidate
     */
    public static void invalidate(Player player) {

    }

}
