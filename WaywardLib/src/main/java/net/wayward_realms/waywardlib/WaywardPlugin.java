package net.wayward_realms.waywardlib;

import org.bukkit.plugin.Plugin;

/**
 * Represents a plugin
 *
 */
public interface WaywardPlugin extends Plugin {

    /**
     * Gets this plugin's prefix in chat
     *
     * @return the prefix
     */
    public String getPrefix();

    /**
     * Loads the plugin's state
     */
    public void loadState();

    /**
     * Saves the plugin's state
     */
    public void saveState();

}
