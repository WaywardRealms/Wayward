package net.wayward_realms.waywardcharacters;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerLoginListener implements Listener {

    private WaywardCharacters plugin;

    public PlayerLoginListener(WaywardCharacters plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (event.getResult() == PlayerLoginEvent.Result.ALLOWED) {
            if (plugin.getActiveCharacter(event.getPlayer()) == null) {
                plugin.setActiveCharacter(event.getPlayer(), plugin.createNewCharacter(event.getPlayer()));
            }
        }
    }

}
