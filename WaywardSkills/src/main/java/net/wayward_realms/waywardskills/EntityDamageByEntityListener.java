package net.wayward_realms.waywardskills;

import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntityListener implements Listener {

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Snowball) {
            Snowball snowball = (Snowball) event.getDamager();
            if (snowball.getShooter() instanceof Player) {
                if (snowball.getMetadata("isIcebolt") != null) {
                    if (!snowball.getMetadata("isIcebolt").isEmpty()) {
                        if (event.getEntity() instanceof LivingEntity) {
                            event.setDamage(4.0D);
                        }
                    }
                }
                if (snowball.getMetadata("isFreeze") != null) {
                    if (!snowball.getMetadata("isFreeze").isEmpty()) {
                        freeze(event.getEntity());
                    }
                }
                if (snowball.getMetadata("isMagicMissile") != null) {
                    if (!snowball.getMetadata("isMagicMissile").isEmpty()) {
                        if (event.getEntity() instanceof LivingEntity) {
                            event.setDamage(4.0D);
                        }
                    }
                }
            }
        }
    }

    private void freeze(Entity entity) {
        for (int x = entity.getLocation().getBlockX() - 1; x <= entity.getLocation().getBlockX() + 1; x++) {
            for (int y = entity.getLocation().getBlockY() - 1; y <= entity.getLocation().getBlockY() + 3; y++) {
                for (int z = entity.getLocation().getBlockZ() - 1; z <= entity.getLocation().getBlockZ() + 1; z++) {
                    if (!entity.getWorld().getBlockAt(x, y, z).getType().isSolid()) {
                        entity.getWorld().getBlockAt(x, y, z).setType(Material.ICE);
                    }
                }
            }
        }
    }

}
