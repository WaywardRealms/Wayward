package net.wayward_realms.waywardevents;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {

    private WaywardEvents plugin;

    public PlayerInteractListener(WaywardEvents plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.hasBlock()) {
            if (plugin.hasBlockDescription(event.getClickedBlock())) {
                event.getClickedBlock().setType(Material.AIR);
                BlockDescriptor blockDescriptor = plugin.getBlockDescription(event.getClickedBlock());
                if (blockDescriptor.getDescription() != null) event.getPlayer().sendMessage("" + ChatColor.YELLOW + ChatColor.ITALIC + blockDescriptor.getDescription());
                if (blockDescriptor.getItems() != null) {
                    for (ItemStack item : event.getPlayer().getInventory().addItem(blockDescriptor.getItems()).values()) {
                        event.getClickedBlock().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), item);
                    }
                    event.getPlayer().updateInventory();
                    event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Found some items.");
                }
                plugin.removeBlockDescription(event.getClickedBlock());
            }
        }
    }

}
