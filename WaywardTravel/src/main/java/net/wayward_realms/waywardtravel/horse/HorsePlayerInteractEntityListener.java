package net.wayward_realms.waywardtravel.horse;

import net.wayward_realms.waywardtravel.WaywardTravel;
import org.bukkit.ChatColor;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class HorsePlayerInteractEntityListener implements Listener {

    private WaywardTravel plugin;

    public HorsePlayerInteractEntityListener(WaywardTravel plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Horse) {
            Horse horse = (Horse) event.getRightClicked();
            if (horse.isTamed()) {
                if (horse.getOwner() != event.getPlayer()) {
                    event.setCancelled(true);
                }
            }
        }
        if (plugin.isUntaming(event.getPlayer())) {
            if (event.getRightClicked() instanceof Tameable) {
                Tameable tameable = (Tameable) event.getRightClicked();
                if (tameable.getOwner() == event.getPlayer()) {
                    tameable.setTamed(false);
                    event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Your tameable creature was untamed.");
                } else {
                    event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "That tameable creature is not yours!");
                }
            } else {
                event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "That entity is not tameable!");
            }
            plugin.setUntaming(event.getPlayer(), false);
        }
    }

}
