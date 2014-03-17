package net.wayward_realms.waywardmoderation;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class VanishManager {

    private WaywardModeration plugin;
    private Set<String> vanished = new HashSet<>();

    public VanishManager(WaywardModeration plugin) {
        this.plugin = plugin;
    }

    public boolean isVanished(Player player) {
        return vanished.contains(player.getName());
    }

    public void setVanished(Player player, boolean vanished) {
        if (vanished) {
            this.vanished.add(player.getName());
            for (Player player1 : plugin.getServer().getOnlinePlayers()) {
                player1.hidePlayer(player);
            }
        } else {
            this.vanished.remove(player.getName());
            for (Player player1 : plugin.getServer().getOnlinePlayers()) {
                player1.showPlayer(player);
            }
        }
    }

    public Set<Player> getVanishedPlayers() {
        Set<Player> vanishedPlayers = new HashSet<>();
        for (String playerName : vanished) {
            if (plugin.getServer().getOfflinePlayer(playerName).isOnline()) {
                vanishedPlayers.add(plugin.getServer().getPlayer(playerName));
            }
        }
        return vanishedPlayers;
    }

}
