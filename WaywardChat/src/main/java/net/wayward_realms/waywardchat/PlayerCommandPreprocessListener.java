package net.wayward_realms.waywardchat;

import net.wayward_realms.waywardlib.chat.Channel;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommandPreprocessListener implements Listener {

    private VaylerynChat plugin;

    public PlayerCommandPreprocessListener(VaylerynChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        for (Channel channel : plugin.getChannels()) {
            for (String alias : channel.getCommand().getAliases()) {
                if (event.getMessage().startsWith("/" + alias + " ")) {
                    String[] args = new String[event.getMessage().split(" ").length - 1];
                    for (int i = 1; i < event.getMessage().split(" ").length; i++) {
                        args[i - 1] = event.getMessage().split(" ")[i];
                    }
                    channel.getCommand().execute(event.getPlayer(), alias, args);
                    event.setCancelled(true);
                }
                if (event.getMessage().startsWith("/" + alias) && event.getMessage().length() - 1 == alias.length()) {
                    channel.getCommand().execute(event.getPlayer(), alias, new String[] {});
                    event.setCancelled(true);
                }
            }
        }
    }

}
