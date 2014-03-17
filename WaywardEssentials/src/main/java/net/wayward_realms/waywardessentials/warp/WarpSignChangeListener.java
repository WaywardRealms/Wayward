package net.wayward_realms.waywardessentials.warp;

import net.wayward_realms.waywardessentials.WaywardEssentials;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class WarpSignChangeListener implements Listener {

    private WaywardEssentials plugin;

    public WarpSignChangeListener(WaywardEssentials plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if (event.getLine(0).equalsIgnoreCase("[warp]")) {
            if (event.getPlayer().hasPermission("wayward.essentials.warpsigns.create")) {
                if (plugin.getWarp(event.getLine(1).toLowerCase()) != null) {
                    event.setLine(0, ChatColor.GREEN + "[warp]");
                    event.setLine(1, event.getLine(1).toLowerCase());
                }
            }
        }
    }

}
