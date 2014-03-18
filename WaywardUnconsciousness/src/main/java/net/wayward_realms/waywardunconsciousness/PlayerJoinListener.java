package net.wayward_realms.waywardunconsciousness;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerJoinListener implements Listener {

    private WaywardUnconsciousness plugin;

    public PlayerJoinListener(WaywardUnconsciousness plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (plugin.isUnconscious(event.getPlayer())) {
            if (System.currentTimeMillis() - plugin.getDeathTime(event.getPlayer()) >= plugin.getConfig().getInt("unconscious-time") * 60000) {
                plugin.setUnconscious(event.getPlayer(), false);
            }
        } else {
            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 0, 0), true);
        }
    }

}
