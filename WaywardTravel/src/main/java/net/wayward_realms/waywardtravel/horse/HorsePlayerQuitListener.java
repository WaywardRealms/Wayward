package net.wayward_realms.waywardtravel.horse;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class HorsePlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (event.getPlayer().isInsideVehicle()) {
            event.getPlayer().getVehicle().eject();
        }
    }

}
