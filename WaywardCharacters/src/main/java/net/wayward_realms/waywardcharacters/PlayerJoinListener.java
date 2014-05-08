package net.wayward_realms.waywardcharacters;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private WaywardCharacters plugin;

    public PlayerJoinListener(WaywardCharacters plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (plugin.getActiveCharacter(event.getPlayer()) == null) {
            plugin.setActiveCharacter(event.getPlayer(), new CharacterImpl(plugin, event.getPlayer()));
        }
        event.getPlayer().setDisplayName(plugin.getActiveCharacter(event.getPlayer()).getName());
    }

}
