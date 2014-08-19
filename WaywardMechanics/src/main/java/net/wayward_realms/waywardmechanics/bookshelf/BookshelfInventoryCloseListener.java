package net.wayward_realms.waywardmechanics.bookshelf;

import net.wayward_realms.waywardlib.util.lineofsight.LineOfSightUtils;
import net.wayward_realms.waywardmechanics.WaywardMechanics;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.Arrays;
import java.util.HashSet;

public class BookshelfInventoryCloseListener implements Listener {

    private WaywardMechanics plugin;

    public BookshelfInventoryCloseListener(WaywardMechanics plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getTitle().equals("Bookshelf")) {
            HashSet<Material> transparentBlocks = new HashSet<>();
            transparentBlocks.addAll(Arrays.asList(Material.values()));
            transparentBlocks.remove(Material.BOOKSHELF);
            Block block = LineOfSightUtils.getTargetBlock(event.getPlayer(), transparentBlocks, 8);
            if (block != null) {
                plugin.setBookshelfInventory(block, event.getInventory());
            }
        }
    }

}
