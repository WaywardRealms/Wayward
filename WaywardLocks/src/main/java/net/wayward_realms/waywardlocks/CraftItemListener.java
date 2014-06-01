package net.wayward_realms.waywardlocks;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public class CraftItemListener implements Listener {

    private WaywardLocks plugin;

    public CraftItemListener(WaywardLocks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        for (ItemStack item : event.getInventory().getContents()) {
            if (item != null) {
                if (item.hasItemMeta()) {
                    if (item.getItemMeta().hasDisplayName()) {
                        if (item.getItemMeta().getDisplayName().equalsIgnoreCase("Key")) {
                            event.setCancelled(true);
                            ((Player) event.getWhoClicked()).sendMessage(plugin.getPrefix() + ChatColor.RED + "You may not use keys as a substitute for iron ingots! ;)");
                        }
                    }
                }
            }
        }
    }

}
