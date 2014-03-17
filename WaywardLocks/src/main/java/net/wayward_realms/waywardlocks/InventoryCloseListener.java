package net.wayward_realms.waywardlocks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.Arrays;

public class InventoryCloseListener implements Listener {

    private WaywardLocks plugin;

    public InventoryCloseListener(WaywardLocks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getTitle().equalsIgnoreCase("Keyring")) {
            plugin.getKeyringManager().getKeyring((Player) event.getPlayer()).clear();
            plugin.getKeyringManager().getKeyring((Player) event.getPlayer()).addAll(Arrays.asList(event.getInventory().getContents()));
        }
    }

}
