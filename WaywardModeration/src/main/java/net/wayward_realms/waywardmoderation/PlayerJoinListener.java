package net.wayward_realms.waywardmoderation;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private WaywardModeration plugin;

    public PlayerJoinListener(WaywardModeration plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        for (Player player : plugin.getVanishedPlayers()) {
            if (!plugin.canSee(event.getPlayer(), player)) event.getPlayer().hidePlayer(player);
        }
        if (plugin.isVanished(event.getPlayer())) {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                if (!plugin.canSee(player, event.getPlayer())) player.hidePlayer(event.getPlayer());
            }
        }
    }

}
