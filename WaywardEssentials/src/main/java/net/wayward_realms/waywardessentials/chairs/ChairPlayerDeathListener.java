package net.wayward_realms.waywardessentials.chairs;

import net.wayward_realms.waywardessentials.WaywardEssentials;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class ChairPlayerDeathListener implements Listener {

    private WaywardEssentials plugin;

    public ChairPlayerDeathListener(WaywardEssentials plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority= EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (plugin.getChairManager().isSitting(player)) {
            plugin.getChairManager().unsitPlayerNow(player);
        }
    }

}
