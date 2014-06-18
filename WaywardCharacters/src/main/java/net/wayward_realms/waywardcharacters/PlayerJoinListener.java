package net.wayward_realms.waywardcharacters;

import net.wayward_realms.waywardlib.util.player.PlayerNamePlateUtils;
import org.bukkit.entity.Player;
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
        plugin.getActiveCharacter(event.getPlayer());
        event.getPlayer().setDisplayName(plugin.getActiveCharacter(event.getPlayer()).getName());
        final Player player = event.getPlayer();
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                PlayerNamePlateUtils.refreshPlayer(player);
            }
        });

    }

}
