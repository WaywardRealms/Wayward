package net.wayward_realms.waywardcharacters;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Arrays;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getTitle().equalsIgnoreCase("Equipment")) {
            if (event.isShiftClick()) {
                event.setCancelled(true);
                return;
            }
            if (Arrays.asList(0, 1, 2, 3, 4, 5, 9, 12, 13, 14, 18, 19, 20, 21, 22, 23).contains(event.getRawSlot())) {
                event.setCancelled(true);
                return;
            }
            if (Arrays.asList(6, 7, 8, 15, 16, 17, 24, 25, 26).contains(event.getRawSlot())) {
                if (event.getCursor().getType() != Material.PAPER || !event.getCursor().hasItemMeta() || !event.getCursor().getItemMeta().hasDisplayName() || !event.getCursor().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Scroll")) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

}
