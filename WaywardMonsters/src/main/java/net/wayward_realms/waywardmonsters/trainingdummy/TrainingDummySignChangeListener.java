package net.wayward_realms.waywardmonsters.trainingdummy;

import net.wayward_realms.waywardmonsters.WaywardMonsters;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class TrainingDummySignChangeListener implements Listener {

    private WaywardMonsters plugin;

    public TrainingDummySignChangeListener(WaywardMonsters plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if (event.getLine(0).equalsIgnoreCase("[dummy]")) {
            if (!event.getPlayer().hasPermission("wayward.monsters.trainingdummy")) {
                event.getBlock().breakNaturally();
                event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
                return;
            }
            event.setLine(0, ChatColor.DARK_RED + "[dummy]");
            event.setLine(1, "" + 0);
        }
    }

}
