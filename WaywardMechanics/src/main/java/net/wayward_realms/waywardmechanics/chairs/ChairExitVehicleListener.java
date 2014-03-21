package net.wayward_realms.waywardmechanics.chairs;

import net.wayward_realms.waywardmechanics.WaywardMechanics;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleExitEvent;

public class ChairExitVehicleListener implements Listener {

    private WaywardMechanics plugin;

    public ChairExitVehicleListener(WaywardMechanics plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority= EventPriority.LOWEST)
    public void onExitVehicle(VehicleExitEvent event) {
        if (event.getVehicle().getPassenger() instanceof Player) {
            final Player player = (Player) event.getVehicle().getPassenger();
            if (plugin.getChairManager().isSitting(player)) {
                plugin.getChairManager().unsitPlayerNormal(player);
            }
        }
    }

}
