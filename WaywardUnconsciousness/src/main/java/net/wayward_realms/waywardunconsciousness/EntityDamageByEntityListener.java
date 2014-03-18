package net.wayward_realms.waywardunconsciousness;

import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntityListener implements Listener {

    private WaywardUnconsciousness plugin;

    public EntityDamageByEntityListener(WaywardUnconsciousness plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (plugin.isUnconscious(player)) {
                event.setCancelled(true);
            }
        }
        if (event.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) event.getDamager();
            if (projectile.getShooter() instanceof Player) {
                if (event.getEntity() instanceof Creature) {
                    ((Creature) event.getEntity()).setTarget((Player) projectile.getShooter());
                }
            }
        }
    }

}
