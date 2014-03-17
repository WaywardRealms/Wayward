package net.wayward_realms.waywardessentials;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerTeleportListener implements Listener {

    private final WaywardEssentials plugin;

    public PlayerTeleportListener(WaywardEssentials plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        plugin.setPreviousLocation(event.getPlayer(), event.getFrom());
    }

}
