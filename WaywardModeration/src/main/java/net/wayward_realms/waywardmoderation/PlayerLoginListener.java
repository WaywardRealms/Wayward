package net.wayward_realms.waywardmoderation;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerLoginListener implements Listener {

    private WaywardModeration plugin;

    public PlayerLoginListener(WaywardModeration plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (plugin.isTempBanned(event.getPlayer())) {
            long millis = plugin.getRemainingBanTime(event.getPlayer());
            long second = (millis / 1000) % 60;
            long minute = (millis / (1000 * 60)) % 60;
            long hour = (millis / (1000 * 60 * 60));
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, ChatColor.RED + "You are temporarily banned. Please wait " + (hour > 0 ? hour + " hours, " : "") + (minute > 0 ? minute + " minutes, " : "") + (second > 0 ? second + " seconds " : "") + "before you can rejoin.");
        }
    }

}
