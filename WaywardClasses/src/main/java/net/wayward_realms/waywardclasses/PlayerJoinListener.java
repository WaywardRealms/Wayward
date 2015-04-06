package net.wayward_realms.waywardclasses;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private WaywardClasses plugin;

    public PlayerJoinListener(WaywardClasses plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.updateExp(event.getPlayer());
    }

}
