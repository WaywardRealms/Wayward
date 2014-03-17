package net.wayward_realms.waywardessentials.portcullis;

import net.wayward_realms.waywardessentials.WaywardEssentials;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class PortcullisSignChangeListener implements Listener {

    private WaywardEssentials plugin;

    public PortcullisSignChangeListener(WaywardEssentials plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if (event.getLine(0).equalsIgnoreCase("[portcullis]")) {
            if (event.getPlayer().hasPermission("wayward.essentials.portcullissigns.create")) {
                event.setLine(0, ChatColor.GRAY + "[portcullis]");
            } else {
                event.getBlock().breakNaturally();
                event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission to create portcullis signs!");
            }
        }
    }

}
