package net.wayward_realms.waywardchat;

import net.wayward_realms.waywardlib.chat.Channel;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private VaylerynChat plugin;

    public PlayerJoinListener(VaylerynChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        for (Channel channel : plugin.getChannels()) {
            if (event.getPlayer().hasPermission("wayward.chat.ch.listen." + channel.getName().toLowerCase())) {
                channel.addListener(event.getPlayer());
            }
        }
        plugin.setPlayerChannel(event.getPlayer(), plugin.getChannel(plugin.getConfig().getString("default-channel").toLowerCase()));
    }

}
