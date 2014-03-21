package net.wayward_realms.waywardmechanics.bookshelf;

import net.wayward_realms.waywardmechanics.WaywardMechanics;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class BookshelfPlayerInteractListener implements Listener {

    private WaywardMechanics plugin;

    public BookshelfPlayerInteractListener(WaywardMechanics plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getType() == Material.BOOKSHELF) {
                if (!event.getPlayer().isSneaking()) {
                    if (plugin.getBookshelfInventory(event.getClickedBlock()) == null) {
                        plugin.createBookshelfInventory(event.getClickedBlock());
                    }
                    event.getPlayer().openInventory(plugin.getBookshelfInventory(event.getClickedBlock()));
                    event.setCancelled(true);
                }
            }
        }

    }

}
