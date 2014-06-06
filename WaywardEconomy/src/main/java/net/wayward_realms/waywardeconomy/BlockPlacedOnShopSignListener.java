package net.wayward_realms.waywardeconomy;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlacedOnShopSignListener implements Listener{

    private WaywardEconomy plugin;

    public BlockPlacedOnShopSignListener(WaywardEconomy plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings({"deprecation"})
    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent e){
        Block targetSign = e.getPlayer().getTargetBlock(null, 5);
        if (targetSign.getType() == Material.SIGN_POST || targetSign.getType() == Material.WALL_SIGN){
            if(targetSign.getState() instanceof Sign) {
                if(((Sign) targetSign.getState()).getLine(0).equalsIgnoreCase(ChatColor.DARK_PURPLE + "[shop]")){
                    e.setCancelled(true);
                }
            }
        }
    }
}
