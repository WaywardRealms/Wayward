package net.wayward_realms.waywardmoderation;

import net.wayward_realms.waywardlib.util.player.PlayerNamePlateUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private WaywardModeration plugin;

    public PlayerJoinListener(WaywardModeration plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        for (Player player : plugin.getVanishedPlayers()) {
            if (event.getPlayer() != player) {
                if (!plugin.canSee(event.getPlayer(), player)) {
                    event.getPlayer().hidePlayer(player);
                } else {
                    PlayerNamePlateUtils.refreshPlayer(player);
                }
            }
        }
        if (plugin.isVanished(event.getPlayer())) {
            event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.GREEN + "You are vanished.");
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                if (!plugin.canSee(player, event.getPlayer())) player.hidePlayer(event.getPlayer());
            }
            PlayerNamePlateUtils.refreshPlayer(event.getPlayer());
        }
        event.getPlayer().sendMessage(plugin.getWarnings(event.getPlayer()).size() <= 0 ? ChatColor.GREEN + "You have no warnings." : ChatColor.RED + "You have " + plugin.getWarnings(event.getPlayer()).size() + " warnings. Use /warnings to view them.");
    }

}
