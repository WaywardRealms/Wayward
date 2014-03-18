package net.wayward_realms.waywardunconsciousness;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    private WaywardUnconsciousness plugin;

    public PlayerInteractListener(WaywardUnconsciousness plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (plugin.isUnconscious(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

}
