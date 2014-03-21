package net.wayward_realms.waywardmechanics.chairs;

import net.wayward_realms.waywardmechanics.WaywardMechanics;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class ChairPlayerTeleportListener implements Listener {

    private WaywardMechanics plugin;

    public ChairPlayerTeleportListener(WaywardMechanics plugin) {
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
