package net.wayward_realms.waywardmonsters;

import net.wayward_realms.wayward.waywardclasses.WaywardClasses;
import io.github.wayward.waywardlib.plugin.character.CharacterPlugin;
import io.github.wayward.waywardlib.plugin.classes.Stat;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EntityDamageByEntityListener implements Listener {

    private WaywardClasses plugin;

    public EntityDamageByEntityListener(WaywardClasses plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        LivingEntity attacker = null;
        LivingEntity defender = null;
        Stat attackStat = null;
        Stat defenceStat = null;
        int attack = 0;
        int defence = 0;
        if (event.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) event.getDamager();
            if (projectile.getShooter() instanceof LivingEntity) {
                attacker = (LivingEntity) projectile.getShooter();
                if (projectile instanceof Arrow) {
                    attackStat = Stat.RANGED_ATTACK;
                    defenceStat = Stat.RANGED_DEFENCE;
                } else {
                    attackStat = Stat.MAGIC_ATTACK;
                    defenceStat = Stat.MAGIC_DEFENCE;
                }
            }
        } else if (event.getDamager() instanceof LivingEntity) {
            attacker = (LivingEntity) event.getDamager();
            attackStat = Stat.MELEE_ATTACK;
            defenceStat = Stat.MELEE_DEFENCE;
        }
        if (event.getEntity() instanceof LivingEntity) {
            defender = (LivingEntity) event.getEntity();
        }
        if (attacker != null) {
            if (attacker instanceof Player) {
                RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                if (characterPluginProvider != null) {
                    CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                    attack = characterPlugin.getActiveCharacter((Player) attacker).getStatValue(attackStat);
                }
            } else if (attacker instanceof LivingEntity) {
                attack = plugin.getEntityLevelManager().getEntityStatValue(attacker);
            }
        }
        if (defender != null) {
            if (defender instanceof Player) {
                RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                if (characterPluginProvider != null) {
                    CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                    defence = characterPlugin.getActiveCharacter((Player) defender).getStatValue(defenceStat);
                }
            } else if (defender instanceof LivingEntity) {
                defence = plugin.getEntityLevelManager().getEntityStatValue(defender);
            }
        }
        if (attacker != null && defender != null && attackStat != null && defenceStat != null) {
            event.setDamage((Math.ceil((((attack + 1D) / 2D) + 5D) * event.getDamage())) / (defence + 1D) / 2D);
        }
        if (defender != null) {
            if (defender instanceof Monster) {
                defender.setCustomName("Lv" + plugin.getEntityLevelManager().getEntityLevel(defender) + " " + defender.getType().toString().toLowerCase().replace('_', ' ') + " - " + Math.round(defender.getHealth() * 100D) / 100D + "HP");
                defender.setCustomNameVisible(true);
            }
        }
    }

}
