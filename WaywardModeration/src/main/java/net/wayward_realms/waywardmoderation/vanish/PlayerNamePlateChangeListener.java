package net.wayward_realms.waywardmoderation.vanish;

import net.wayward_realms.waywardlib.util.player.PlayerNamePlateChangeEvent;
import net.wayward_realms.waywardmoderation.WaywardModeration;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerNamePlateChangeListener implements Listener {

    private WaywardModeration plugin;

    public PlayerNamePlateChangeListener(WaywardModeration plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerNamePlateChange(PlayerNamePlateChangeEvent event) {
        if (plugin.isVanished(event.getPlayer())) {
            event.setName(ChatColor.BLUE + event.getPlayer().getName());
        }
    }

}
