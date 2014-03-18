package net.wayward_realms.waywardtravel.boat;

import net.wayward_realms.waywardtravel.WaywardTravel;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class BoatSignChangeListener implements Listener {

    private WaywardTravel plugin;

    public BoatSignChangeListener(WaywardTravel plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if (event.getLine(0).equalsIgnoreCase("[boat]")) {
            if (event.getPlayer().hasPermission("wayward.travel.boatsigns.create")) {
                event.setLine(0, ChatColor.BLUE + "[boat]");
            } else {
                event.getBlock().breakNaturally();
                event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission to create boat signs!");
            }
        }
    }

}
