package net.wayward_realms.waywardpermissions;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private WaywardPermissions plugin;

    public PlayerJoinListener(WaywardPermissions plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!event.getPlayer().isBanned()) {
            plugin.assignPermissions(event.getPlayer());
        }
    }

}
