package net.wayward_realms.waywardmonsters;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class CreatureSpawnListener implements Listener {

    private WaywardMonsters plugin;

    public CreatureSpawnListener(WaywardMonsters plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntitySpawn(CreatureSpawnEvent event) {
        if (event.getEntity() != null) {
            if (plugin.getEntityLevelManager().getEntityLevel(event.getEntity()) == 0) {
                event.setCancelled(true);
            }
        }
    }

}
