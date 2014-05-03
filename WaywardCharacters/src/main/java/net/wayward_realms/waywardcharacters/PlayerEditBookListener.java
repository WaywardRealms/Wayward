package net.wayward_realms.waywardcharacters;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.meta.BookMeta;

public class PlayerEditBookListener implements Listener {

    private WaywardCharacters plugin;

    public PlayerEditBookListener(WaywardCharacters plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerEditBook(PlayerEditBookEvent event) {
        if (event.isSigning()) {
            BookMeta meta = event.getNewBookMeta();
            meta.setAuthor(plugin.getActiveCharacter(event.getPlayer()).getName());
            event.setNewBookMeta(meta);
        }
    }

}
