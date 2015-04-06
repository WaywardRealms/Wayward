package net.wayward_realms.waywardclasses;

import net.wayward_realms.waywardlib.character.CharacterPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class EntityDamageListener implements Listener {

    private WaywardClasses plugin;

    public EntityDamageListener(WaywardClasses plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            if (event.getCause() == DamageCause.ENTITY_ATTACK || event.getCause() == DamageCause.PROJECTILE) {
                if (((Player) event.getEntity()).isBlocking()) {
                    event.setCancelled(true);
                } else {
                    CharacterPlugin characterPlugin = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class).getProvider();
                    characterPlugin.getActiveCharacter((Player) event.getEntity()).setHealth(((Player) event.getEntity()).getHealth());
                }
            }
        }
    }

}
