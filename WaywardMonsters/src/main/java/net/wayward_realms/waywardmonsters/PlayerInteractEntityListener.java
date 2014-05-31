package net.wayward_realms.waywardmonsters;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerInteractEntityListener implements Listener {

    @EventHandler
    public void onPlayernteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof LivingEntity) {
            if (event.getPlayer().getItemInHand() != null) {
                if (event.getPlayer().getItemInHand().getType() == Material.SADDLE) {
                    event.getRightClicked().setPassenger(event.getPlayer());
                }
            }
        }
    }
}
