package net.wayward_realms.waywardmechanics.drawbridge;

import net.wayward_realms.waywardmechanics.WaywardMechanics;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class DrawbridgeSignChangeListener implements Listener {

    private WaywardMechanics plugin;

    public DrawbridgeSignChangeListener(WaywardMechanics plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if (event.getLine(0).equalsIgnoreCase("[drawbridge]")) {
            if (event.getPlayer().hasPermission("wayward.mechanics.drawbridge.create")) {
                event.setLine(0, ChatColor.GRAY + "[drawbridge]");
            } else {
                event.getBlock().breakNaturally();
                event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission to create drawbridge signs!");
            }
        }
    }

}
