package net.wayward_realms.waywardessentials.chairs;

import net.wayward_realms.waywardessentials.WaywardEssentials;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class ChairPlayerTeleportListener implements Listener {

    private WaywardEssentials plugin;

    public ChairPlayerTeleportListener(WaywardEssentials plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority= EventPriority.LOWEST)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        final Player player = event.getPlayer();
        if (plugin.getChairManager().isSitting(player)) {
            event.setCancelled(true);
        }
    }

}
