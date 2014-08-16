package net.wayward_realms.waywardskills;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

public class PlayerExpChangeListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerExpChange(PlayerExpChangeEvent event) {
        event.setAmount(0);
    }

}
