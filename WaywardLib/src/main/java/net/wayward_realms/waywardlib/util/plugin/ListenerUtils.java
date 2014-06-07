package net.wayward_realms.waywardlib.util.plugin;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

/**
 * Utility class for methods for easing the use of listeners
 */
public class ListenerUtils {

    /**
     * Registers any amount of listeners to a plugin
     *
     * @param plugin the plugin
     * @param listeners the listeners to register
     */
    public static void registerListeners(Plugin plugin, Listener... listeners) {
        PluginManager pluginManager = plugin.getServer().getPluginManager();
        for (Listener listener : listeners) {
            pluginManager.registerEvents(listener, plugin);
        }
    }

}
