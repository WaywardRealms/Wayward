package net.wayward_realms.waywardmonsters.bleed;

import net.wayward_realms.waywardmonsters.WaywardMonsters;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class BleedEntityDamageListener implements Listener {

    private WaywardMonsters plugin;

    public BleedEntityDamageListener(WaywardMonsters plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof LivingEntity)) return;
        LivingEntity entity = (LivingEntity) event.getEntity();
        if (entity.getHealth() <= (0.25D * entity.getMaxHealth())) {
            plugin.bleed(entity);
        }
    }

}
