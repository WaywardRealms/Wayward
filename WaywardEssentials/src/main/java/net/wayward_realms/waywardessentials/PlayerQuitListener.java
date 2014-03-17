package net.wayward_realms.waywardessentials;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private WaywardEssentials plugin;

    public PlayerQuitListener(WaywardEssentials plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        for (Player player : plugin.getPlayersWithLogMessagesEnabled()) {
            player.sendMessage(event.getQuitMessage());
        }
        event.setQuitMessage("");
    }

}
