package net.wayward_realms.waywarditems;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private WaywardItems plugin;

    public PlayerQuitListener(WaywardItems plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.getJingleNoteManager().stop(event.getPlayer().getName());
    }

}
