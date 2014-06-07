package net.wayward_realms.waywardmoderation.vanish;

import net.wayward_realms.waywardmoderation.WaywardModeration;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kitteh.tag.AsyncPlayerReceiveNameTagEvent;

public class AsyncPlayerRecieveNameTagListener implements Listener {

    private WaywardModeration plugin;

    public AsyncPlayerRecieveNameTagListener(WaywardModeration plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onNameTag(AsyncPlayerReceiveNameTagEvent event) {
        if (plugin.isVanished(event.getNamedPlayer())) {
            event.setTag(ChatColor.BLUE + event.getNamedPlayer().getName());
        }
    }

}
