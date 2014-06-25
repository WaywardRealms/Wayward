package net.wayward_realms.waywardmechanics.portcullis;

import net.wayward_realms.waywardmechanics.WaywardMechanics;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class PortcullisSignChangeListener implements Listener {

    private WaywardMechanics plugin;

    public PortcullisSignChangeListener(WaywardMechanics plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if (event.getLine(0).equalsIgnoreCase("[portcullis]")) {
            if (event.getPlayer().hasPermission("wayward.mechanics.portcullis.create")) {
                event.setLine(0, ChatColor.GRAY + "[portcullis]");
            } else {
                event.getBlock().breakNaturally();
                event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission to create portcullis signs!");
            }
        }
    }

}
