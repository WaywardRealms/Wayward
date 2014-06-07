package net.wayward_realms.waywardskills;

import net.wayward_realms.waywardlib.professions.ToolType;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
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
                if (snowball.getMetadata("isIceBreath") != null) {
                    if (!snowball.getMetadata("isIceBreath").isEmpty()) {
                        event.setCancelled(true);
                        freeze(event.getEntity());
                    }
                }
            }
        } else if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (player.getItemInHand() != null) {
                if (ToolType.getToolType(player.getItemInHand().getType()) == ToolType.SWORD) {
                    if (player.getItemInHand().getItemMeta().hasLore()) {
                        for (String lore : player.getItemInHand().getItemMeta().getLore()) {
                            if (lore.startsWith("lightning:")) {
                                long endTime = Long.parseLong(lore.split(":")[1]);
                                if (endTime >= System.currentTimeMillis()) {
                                    event.getEntity().getWorld().strikeLightning(event.getEntity().getLocation());
                                }
                            } else if (lore.startsWith("blizzard:")) {
                                long endTime = Long.parseLong(lore.split(":")[1]);
                                if (endTime >= System.currentTimeMillis()) {
                                    event.setDamage(event.getDamage() + 6);
                                    freeze(event.getEntity());
                                }
                            }
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
