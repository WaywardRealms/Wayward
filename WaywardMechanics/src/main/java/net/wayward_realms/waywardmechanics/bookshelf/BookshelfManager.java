package net.wayward_realms.waywardmechanics.bookshelf;

import net.wayward_realms.waywardmechanics.WaywardMechanics;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class BookshelfManager {

    private WaywardMechanics plugin;
    private File bookshelfDirectory;

    public BookshelfManager(WaywardMechanics plugin) {
        this.plugin = plugin;
        bookshelfDirectory = new File(plugin.getDataFolder(), "bookshelves");
    }

    public Inventory getBookshelfInventory(Block block) {
        File worldDirectory = new File(bookshelfDirectory, block.getWorld().getName());
        File xDirectory = new File(worldDirectory, "" + block.getX());
        File yDirectory = new File(xDirectory, "" + block.getY());
        File zDirectory = new File(yDirectory, "" + block.getZ());
        File bookshelfFile = new File(zDirectory, "bookshelf.yml");
        YamlConfiguration bookshelfConfig = YamlConfiguration.loadConfiguration(bookshelfFile);
        Inventory inventory = plugin.getServer().createInventory(null, 9, "Bookshelf");
        if (bookshelfConfig.get("contents") != null) {
            for (Object itemStack : bookshelfConfig.getList("contents")) {
                if (itemStack != null) {
                    if (itemStack instanceof ItemStack) {
                        inventory.addItem((ItemStack) itemStack);
                    }
                }
            }
        }
        return inventory;
    }

    public void setBookshelfInventory(Block block, Inventory inventory) {
        File worldDirectory = new File(bookshelfDirectory, block.getWorld().getName());
        File xDirectory = new File(worldDirectory, "" + block.getX());
        File yDirectory = new File(xDirectory, "" + block.getY());
        File zDirectory = new File(yDirectory, "" + block.getZ());
        File bookshelfFile = new File(zDirectory, "bookshelf.yml");
        if (inventory == null || isInventoryEmpty(inventory)) {
            bookshelfFile.delete();
        } else {
            YamlConfiguration bookshelfConfig = new YamlConfiguration();
            bookshelfConfig.set("contents", Arrays.asList(inventory.getContents()));
            try {
                bookshelfConfig.save(bookshelfFile);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    private boolean isInventoryEmpty(Inventory inventory) {
        for (ItemStack item : inventory.getContents()) {
            if (item != null) return false;
        }
        return true;
    }

}
