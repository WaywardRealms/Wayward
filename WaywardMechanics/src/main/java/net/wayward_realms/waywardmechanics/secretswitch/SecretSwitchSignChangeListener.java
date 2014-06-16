package net.wayward_realms.waywardmechanics.secretswitch;

import net.wayward_realms.waywardmechanics.WaywardMechanics;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SecretSwitchSignChangeListener implements Listener {

    private WaywardMechanics plugin;

    public SecretSwitchSignChangeListener(WaywardMechanics plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if (event.getLine(0).equalsIgnoreCase("[switch]")) {
            if (!event.getPlayer().hasPermission("wayward.mechanics.secretswitch")) {
                event.getBlock().breakNaturally();
                event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
                return;
            }
            if (!(event.getLine(1).equalsIgnoreCase("button") || event.getLine(1).equalsIgnoreCase("lever"))) {
                event.getBlock().breakNaturally();
                event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify a valid type on the second line (lever or button)");
                return;
            }
            event.setLine(0, ChatColor.BLUE + "[switch]");
        }
    }

}
