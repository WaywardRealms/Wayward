package net.wayward_realms.waywardcharacters;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {

    private WaywardCharacters plugin;

    public PlayerRespawnListener(WaywardCharacters plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        plugin.getActiveCharacter(event.getPlayer()).setHealth(plugin.getActiveCharacter(event.getPlayer()).getMaxHealth());
    }

}
