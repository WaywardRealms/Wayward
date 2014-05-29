package net.wayward_realms.waywardunconsciousness;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    private WaywardUnconsciousness plugin;

    public PlayerDeathListener(WaywardUnconsciousness plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.setDeathMessage("");
        plugin.setUnconscious(event.getEntity(), true);
        plugin.setDeathLocation(event.getEntity(), event.getEntity().getLocation());
        plugin.setDeathTime(event.getEntity());
        event.getEntity().setBedSpawnLocation(null, true);
    }

}
