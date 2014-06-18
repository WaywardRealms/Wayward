package net.wayward_realms.waywardchat;

import net.wayward_realms.waywardlib.chat.Channel;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private WaywardChat plugin;

    public PlayerQuitListener(WaywardChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        for (Channel channel : plugin.getChannels()) {
            if (channel.getListeners().contains(event.getPlayer())) {
                channel.removeListener(event.getPlayer());
            }
            if (channel.getSpeakers().contains(event.getPlayer())) {
                channel.removeSpeaker(event.getPlayer());
            }
        }
        plugin.setSnooping(event.getPlayer(), false);
        for (ChatGroup chatGroup : plugin.getChatGroups()) {
            chatGroup.disposeIfUnused();
        }
    }

}
