package net.wayward_realms.waywardessentials.warp;

import net.wayward_realms.waywardessentials.WaywardEssentials;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class WarpPlayerInteractListener implements Listener {

    private WaywardEssentials plugin;

    public WarpPlayerInteractListener(WaywardEssentials plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getState() instanceof Sign) {
                Sign sign = (Sign) event.getClickedBlock().getState();
                if (sign.getLine(0).equalsIgnoreCase(ChatColor.GREEN + "[warp]")) {
                    if (plugin.getWarp(sign.getLine(1).toLowerCase()) != null) {
                        event.getPlayer().teleport(plugin.getWarp(sign.getLine(1).toLowerCase()));
                    }
                }
            }
        }
    }

}
