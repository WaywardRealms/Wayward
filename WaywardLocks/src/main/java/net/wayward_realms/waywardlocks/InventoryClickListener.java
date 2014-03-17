package net.wayward_realms.waywardlocks;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    private WaywardLocks plugin;

    public InventoryClickListener(WaywardLocks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getTitle().equalsIgnoreCase("Keyring")) {
            if (event.getCurrentItem() != null) {
                event.setCancelled(true);
                if (event.getCurrentItem().hasItemMeta()) {
                    if (event.getCurrentItem().getItemMeta().hasDisplayName()) {
                        if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("Key")) {
                            event.setCancelled(false);
                        }
                    }
                }
                if (event.getCurrentItem().getType() == Material.AIR) {
                    event.setCancelled(false);
                }
                if (event.isCancelled()) {
                    ((Player) event.getWhoClicked()).sendMessage(plugin.getPrefix() + ChatColor.RED + "You may not place non-key items on your keyring");
                }
            }
        }
    }

}
