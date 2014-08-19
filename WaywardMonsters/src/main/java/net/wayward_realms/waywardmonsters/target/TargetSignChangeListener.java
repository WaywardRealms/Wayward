package net.wayward_realms.waywardmonsters.target;

import net.wayward_realms.waywardmonsters.WaywardMonsters;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class TargetSignChangeListener implements Listener {

    private WaywardMonsters plugin;

    public TargetSignChangeListener(WaywardMonsters plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if (event.getLine(0).equalsIgnoreCase("[target]")) {
            if (!event.getPlayer().hasPermission("wayward.monsters.target")) {
                event.getBlock().breakNaturally();
                event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
                return;
            }
            event.setLine(0, ChatColor.DARK_RED + "[target]");
            event.setLine(1, "" + 0);
        }
    }

}
