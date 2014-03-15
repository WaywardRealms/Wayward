package net.wayward_realms.waywardcharacters;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class EntityRegainHealthListener implements Listener {

    private WaywardCharacters plugin;

    public EntityRegainHealthListener(WaywardCharacters plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityRegainHealth(EntityRegainHealthEvent event) {
        if (event.getEntity() instanceof Player) {
            if (event.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED) {
                event.setCancelled(true);
            } else {
                plugin.getActiveCharacter((Player) event.getEntity()).setHealth(((Player) event.getEntity()).getHealth());
            }
        }
    }

}
