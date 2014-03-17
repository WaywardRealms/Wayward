package net.wayward_realms.waywardessentials.bookshelf;

import net.wayward_realms.waywardessentials.WaywardEssentials;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class BookshelfBlockBreakListener implements Listener {

    private WaywardEssentials plugin;

    public BookshelfBlockBreakListener(WaywardEssentials plugin) {
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
            plugin.getBookshelfInventories().remove(event.getBlock());
            File bookshelfFile = new File(plugin.getDataFolder().getPath() + File.separator + "bookshelves" + event.getBlock().getWorld().getName() + File.separator + event.getBlock().getX() + File.separator + event.getBlock().getY() + File.separator + event.getBlock().getZ() + "bookshelf.yml");
            if (bookshelfFile.exists()) {
                bookshelfFile.delete();
            }
        }
    }

}
