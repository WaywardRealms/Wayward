package net.wayward_realms.waywardcharacters;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {

    private static final int MAX_DISTANCE = 16;

    private WaywardCharacters plugin;

    public EntityDamageListener(WaywardCharacters plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if ((event.getEntity() instanceof Player)) {
            Player damaged = (Player) event.getEntity();
            plugin.getActiveCharacter(damaged).setHealth(Math.max(damaged.getHealth() - event.getDamage(), 0));
        }
    }

}

