package net.wayward_realms.waywardunconsciousness;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    private WaywardUnconsciousness plugin;

    public PlayerMoveListener(WaywardUnconsciousness plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (plugin.isUnconscious(event.getPlayer())) {
            if (event.getFrom().getBlockX() != event.getTo().getBlockX() || event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
                event.getPlayer().teleport(new Location(event.getFrom().getWorld(), event.getFrom().getBlockX() + 0.5, event.getFrom().getBlockY() + 0.5, (double) event.getFrom().getBlockZ(), event.getFrom().getPitch(), event.getFrom().getYaw()));
                if (plugin.getServer().getPluginCommand("sleep") != null) plugin.getServer().dispatchCommand(event.getPlayer().getPlayer(), "sleep");
            }
        }
    }

}
