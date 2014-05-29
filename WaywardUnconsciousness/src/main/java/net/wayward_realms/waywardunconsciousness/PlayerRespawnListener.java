package net.wayward_realms.waywardunconsciousness;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerRespawnListener implements Listener {

    private WaywardUnconsciousness plugin;

    public PlayerRespawnListener(WaywardUnconsciousness plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(final PlayerRespawnEvent event) {
        event.setRespawnLocation(plugin.getDeathLocation(event.getPlayer()));
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 0), true);

            }
        }, 20L);
    }

}
