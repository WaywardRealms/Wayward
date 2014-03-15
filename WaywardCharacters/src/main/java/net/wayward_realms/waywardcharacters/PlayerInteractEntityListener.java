package net.wayward_realms.waywardcharacters;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerInteractEntityListener implements Listener {

    private WaywardCharacters plugin;

    public PlayerInteractEntityListener(WaywardCharacters plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getPlayer().isSneaking()) {
            if (event.getRightClicked() instanceof Player) {
                plugin.getServer().dispatchCommand(event.getPlayer(), "character card " + ((Player) event.getRightClicked()).getName());
            }
        }
    }

}
