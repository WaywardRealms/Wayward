package net.wayward_realms.waywardmechanics.bookshelf;

import net.wayward_realms.waywardmechanics.WaywardMechanics;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class BookshelfBlockBreakListener implements Listener {

    private WaywardMechanics plugin;

    public BookshelfBlockBreakListener(WaywardMechanics plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (plugin.getBookshelfInventory(event.getBlock()) != null) {
            for (ItemStack itemStack : plugin.getBookshelfInventory(event.getBlock())) {
                if (itemStack != null) {
                    event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), itemStack);
                }
            }
            plugin.setBookshelfInventory(event.getBlock(), null);
        }
    }

}
