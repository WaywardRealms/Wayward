package net.wayward_realms.waywardskills;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ProjectileHitListener implements Listener {

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getEntity();
            if (arrow.getShooter() instanceof Player) {
                if (arrow.getMetadata("isPoisonArrow") != null) {
                    if (!arrow.getMetadata("isPoisonArrow").isEmpty()) {
                        ItemStack potion = new ItemStack(Material.POTION);
                        PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
                        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.POISON, 120, 2), false);
                        potion.setItemMeta(potionMeta);
                        ThrownPotion thrownPotion = arrow.getShooter().launchProjectile(ThrownPotion.class);
                        thrownPotion.teleport(arrow);
                        thrownPotion.setVelocity(arrow.getVelocity());
                        thrownPotion.setItem(potion);
                    }
                }
                if (arrow.getMetadata("isSnareShot") != null) {
                    if (!arrow.getMetadata("isSnareShot").isEmpty()) {
                        ItemStack potion = new ItemStack(Material.POTION);
                        PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
                        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW, 300, 2), false);
                        potion.setItemMeta(potionMeta);
                        ThrownPotion thrownPotion = arrow.getShooter().launchProjectile(ThrownPotion.class);
                        thrownPotion.teleport(arrow);
                        thrownPotion.setVelocity(arrow.getVelocity());
                        thrownPotion.setItem(potion);
                    }
                }
            }
        }
    }

}
