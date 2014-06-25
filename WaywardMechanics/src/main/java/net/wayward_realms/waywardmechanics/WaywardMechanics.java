package net.wayward_realms.waywardmechanics;

import net.wayward_realms.waywardlib.mechanics.MechanicsPlugin;
import net.wayward_realms.waywardmechanics.bookshelf.BookshelfBlockBreakListener;
import net.wayward_realms.waywardmechanics.bookshelf.BookshelfManager;
import net.wayward_realms.waywardmechanics.bookshelf.BookshelfPlayerInteractListener;
import net.wayward_realms.waywardmechanics.chairs.*;
import net.wayward_realms.waywardmechanics.drawbridge.DrawbridgeBlockRedstoneListener;
import net.wayward_realms.waywardmechanics.drawbridge.DrawbridgePlayerInteractListener;
import net.wayward_realms.waywardmechanics.drawbridge.DrawbridgeSignChangeListener;
import net.wayward_realms.waywardmechanics.portcullis.PortcullisBlockRedstoneListener;
import net.wayward_realms.waywardmechanics.portcullis.PortcullisPlayerInteractListener;
import net.wayward_realms.waywardmechanics.portcullis.PortcullisSignChangeListener;
import net.wayward_realms.waywardmechanics.secretswitch.SecretSwitchPlayerInteractListener;
import net.wayward_realms.waywardmechanics.secretswitch.SecretSwitchSignChangeListener;
import org.bukkit.block.Block;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public class WaywardMechanics extends JavaPlugin implements MechanicsPlugin {

    private BookshelfManager bookshelfManager = new BookshelfManager(this);
    private ChairManager chairManager = new ChairManager(this);

    @Override
    public void onEnable() {
        registerListeners(new BookshelfBlockBreakListener(this), new BookshelfPlayerInteractListener(this),
                new ChairBlockBreakListener(this), new ChairExitVehicleListener(this), new ChairPlayerDeathListener(this), new ChairPlayerInteractListener(this), new ChairPlayerTeleportListener(this),
                new PortcullisBlockRedstoneListener(), new PortcullisPlayerInteractListener(), new PortcullisSignChangeListener(this),
                new SecretSwitchPlayerInteractListener(this), new SecretSwitchSignChangeListener(this),
                new DrawbridgeBlockRedstoneListener(), new DrawbridgePlayerInteractListener(), new DrawbridgeSignChangeListener(this));
    }

    @Override
    public void onDisable() {
        saveState();
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    @Override
    public String getPrefix() {
        return "";
    }

    @Override
    public void loadState() {
        bookshelfManager.load();
    }

    @Override
    public void saveState() {
        bookshelfManager.save();
    }

    public Inventory getBookshelfInventory(Block block) {
        return bookshelfManager.getBookshelfInventory(block);
    }

    public void createBookshelfInventory(Block block) {
        bookshelfManager.createBookshelfInventory(block);
    }

    public Map<Block, Inventory> getBookshelfInventories() {
        return bookshelfManager.getBookshelfInventories();
    }

    public ChairManager getChairManager() {
        return chairManager;
    }

}
