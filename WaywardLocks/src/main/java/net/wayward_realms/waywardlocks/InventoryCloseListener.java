package net.wayward_realms.waywardlocks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class InventoryCloseListener implements Listener {

    private WaywardLocks plugin;

    public InventoryCloseListener(WaywardLocks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getTitle().equalsIgnoreCase("Keyring")) {
            List<ItemStack> keyring = plugin.getKeyringManager().getKeyring((Player) event.getPlayer());
            keyring.clear();
            keyring.addAll(Arrays.asList(event.getInventory().getContents()));
            plugin.getKeyringManager().setKeyring((Player) event.getPlayer(), keyring);
        }
    }

}
