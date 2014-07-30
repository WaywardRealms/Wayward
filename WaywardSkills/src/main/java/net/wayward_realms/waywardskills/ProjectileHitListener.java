package net.wayward_realms.waywardskills;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public class ProjectileHitListener implements Listener {

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getEntity();
            if (arrow.getShooter() instanceof Player) {
                if (arrow.getMetadata("isPoisonArrow") != null) {
                    if (!arrow.getMetadata("isPoisonArrow").isEmpty()) {
                        Potion potion = new Potion(PotionType.POISON, 2);
                        potion.setSplash(true);
                        ItemStack itemStack = new ItemStack(Material.POTION);
                        potion.apply(itemStack);
                        ThrownPotion thrownPotion = arrow.getShooter().launchProjectile(ThrownPotion.class);
                        thrownPotion.teleport(arrow);
                        thrownPotion.setVelocity(arrow.getVelocity());
                        thrownPotion.setItem(itemStack);
                    }
                } else if (arrow.getMetadata("isSnareShot") != null) {
                    if (!arrow.getMetadata("isSnareShot").isEmpty()) {
                        Potion potion = new Potion(PotionType.SLOWNESS, 7);
                        potion.setSplash(true);
                        ItemStack itemStack = new ItemStack(Material.POTION);
                        potion.apply(itemStack);
                        ThrownPotion thrownPotion = arrow.getShooter().launchProjectile(ThrownPotion.class);
                        thrownPotion.teleport(arrow);
                        thrownPotion.setVelocity(arrow.getVelocity());
                        thrownPotion.setItem(itemStack);
                    }
                }
            }
        }
    }

}
