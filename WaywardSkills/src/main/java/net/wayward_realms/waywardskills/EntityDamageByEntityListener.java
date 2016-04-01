package net.wayward_realms.waywardskills;

import net.wayward_realms.waywardlib.professions.ToolType;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EntityDamageByEntityListener implements Listener {

    private WaywardSkills plugin;

    public EntityDamageByEntityListener(WaywardSkills plugin) {
        this.plugin = plugin;
    }

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
        } else if (event.getDamager() instanceof SmallFireball) {
            SmallFireball fireball = (SmallFireball) event.getDamager();
            if (fireball.getShooter() instanceof Player) {
                if (fireball.getMetadata("isFireBreath") != null) {
                    if (!fireball.getMetadata("isFireBreath").isEmpty()) {
                        event.getEntity().setFireTicks(400);
                        event.setCancelled(true);
                        fireball.remove();
                    }
                }
            }
        } else if (event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();
            if (arrow.getMetadata("isRazorShot") != null) {
                if (!arrow.getMetadata("isRazorShot").isEmpty()) {
                    if (event.getEntity() instanceof LivingEntity) {
                        event.setDamage(9999D);
                        Arrow newArrow = ((LivingEntity) event.getEntity()).launchProjectile(Arrow.class, arrow.getVelocity());
                        newArrow.setMetadata("isRazorShot", new FixedMetadataValue(plugin, true));
                    }
                }
            }
            if (arrow.getMetadata("isPowerShot") != null) {
                if (!arrow.getMetadata("isPowerShot").isEmpty()) {
                    event.setDamage(9999D);
                }
            }
            if (arrow.getMetadata("isPoisonArrow") != null) {
                if (!arrow.getMetadata("isPoisonArrow").isEmpty()) {
                    if (event.getEntity() instanceof LivingEntity) {
                        ((LivingEntity) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 2, 300));
                    }
                }
            }
            if (arrow.getMetadata("isSnareShot") != null) {
                if (!arrow.getMetadata("isSnareShot").isEmpty()) {
                    if (event.getEntity() instanceof LivingEntity) {
                        ((LivingEntity) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 7, 300));
                    }
                }
            }
        } else if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (player.getInventory().getItemInMainHand() != null) {
                if (ToolType.getToolType(player.getInventory().getItemInMainHand().getType()) == ToolType.SWORD) {
                    if (player.getInventory().getItemInMainHand().getItemMeta().hasLore()) {
                        for (String lore : player.getInventory().getItemInMainHand().getItemMeta().getLore()) {
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
