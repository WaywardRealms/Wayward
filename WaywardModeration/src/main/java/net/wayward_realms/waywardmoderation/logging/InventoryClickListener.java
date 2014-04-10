package net.wayward_realms.waywardmoderation.logging;

import net.wayward_realms.waywardmoderation.WaywardModeration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    private WaywardModeration plugin;

    public InventoryClickListener(WaywardModeration plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        switch (event.getAction()) {
            case NOTHING:
                break;
            case PICKUP_ALL:case PICKUP_SOME:case PICKUP_HALF:case PICKUP_ONE:case SWAP_WITH_CURSOR:case MOVE_TO_OTHER_INVENTORY:case HOTBAR_MOVE_AND_READD:case HOTBAR_SWAP:case CLONE_STACK:case COLLECT_TO_CURSOR:
                plugin.recordInventoryRemoval(event.getInventory().getHolder(), event.getCurrentItem(), (Player) event.getWhoClicked());
                break;
            case PLACE_ALL:case PLACE_SOME:case PLACE_ONE:case DROP_ALL_CURSOR:case DROP_ONE_CURSOR:case DROP_ALL_SLOT:case DROP_ONE_SLOT:
                plugin.recordInventoryAddition(event.getInventory().getHolder(), event.getCurrentItem(), (Player) event.getWhoClicked());
                break;
            case UNKNOWN:
                break;
        }
    }

}
