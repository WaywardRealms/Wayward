package net.wayward_realms.waywardcharacters;

import org.bukkit.ChatColor;
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
            if ((((float) damaged.getHealth() / (float) damaged.getMaxHealth()) * 100.0D > 25.0D) && (((float) damaged.getHealth() / (float) damaged.getMaxHealth()) * 100.0D <= 50.0D)) {
                for (Player player : event.getEntity().getWorld().getPlayers()) {
                    if (event.getEntity().getLocation().distance(player.getLocation()) <= MAX_DISTANCE) {
                        player.sendMessage(plugin.getPrefix() + ChatColor.RED + ((Player) event.getEntity()).getDisplayName() + " appears wounded!");
                    }
                }
            }
            if (((float) damaged.getHealth() / (float) damaged.getMaxHealth()) * 100.0D <= 25.0D) {
                for (Player player : event.getEntity().getWorld().getPlayers()) {
                    if (event.getEntity().getLocation().distance(player.getLocation()) <= MAX_DISTANCE) {
                        player.sendMessage(plugin.getPrefix() + ChatColor.RED + ((Player) event.getEntity()).getDisplayName() + " appears gravely wounded!");
                    }
                }
            }
        }
    }

}

