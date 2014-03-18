package net.wayward_realms.waywardunconsciousness;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

public class EntityTargetListener implements Listener {

    private WaywardUnconsciousness plugin;

    public EntityTargetListener(WaywardUnconsciousness plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        if (event.getTarget() instanceof Player) {
            if (plugin.isUnconscious((Player) event.getTarget())) {
                event.setCancelled(true);
            }
        }
    }

}
