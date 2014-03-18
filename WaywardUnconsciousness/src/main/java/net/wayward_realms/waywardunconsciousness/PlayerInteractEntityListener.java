package net.wayward_realms.waywardunconsciousness;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerInteractEntityListener implements Listener {

    private WaywardUnconsciousness plugin;

    public PlayerInteractEntityListener(WaywardUnconsciousness plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Player) {
            if (plugin.isUnconscious((Player) event.getRightClicked())) {
                plugin.setUnconscious((Player) event.getRightClicked(), false);
            }
        }
    }

}
