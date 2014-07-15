package net.wayward_realms.waywardmonsters.bleed;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BleedBlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getMetadata("isBlood") != null && event.getBlock().getMetadata("isBlood").size() > 0) {
            event.setCancelled(true);
            event.getBlock().setType(Material.AIR);
        }
    }

}
