package net.wayward_realms.waywardskills;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getCurrentItem() != null) {
            if (event.getCurrentItem().hasItemMeta()) {
                if (event.getCurrentItem().getItemMeta().hasDisplayName()) {
                    if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Mage armour")) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

}
