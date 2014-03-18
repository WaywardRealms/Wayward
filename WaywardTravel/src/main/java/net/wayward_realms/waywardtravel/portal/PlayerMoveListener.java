package net.wayward_realms.waywardtravel.portal;

import net.wayward_realms.waywardlib.travel.Portal;
import net.wayward_realms.waywardtravel.WaywardTravel;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    private WaywardTravel plugin;

    public PlayerMoveListener(WaywardTravel plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        for (Portal portal : plugin.getPortals()) {
            Location minLocation = portal.getMinLocation();
            Location maxLocation = portal.getMaxLocation();
            Location location = event.getTo();
            if (minLocation.getX() < location.getX() &&
                minLocation.getY() < location.getY() &&
                minLocation.getZ() < location.getZ() &&
                maxLocation.getX() > location.getX() &&
                maxLocation.getY() > location.getY() &&
                maxLocation.getZ() > location.getZ()) {
                event.getPlayer().teleport(portal.getTeleportLocation());
            }
        }
    }

}
