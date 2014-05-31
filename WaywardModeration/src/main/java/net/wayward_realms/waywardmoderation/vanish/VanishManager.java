package net.wayward_realms.waywardmoderation.vanish;

import net.wayward_realms.waywardmoderation.WaywardModeration;
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
                if (!canSee(player1, player)) player1.hidePlayer(player);
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

    public boolean canSee(Player player, Player target) {
        if (!vanished.contains(target.getName())) return true;
        int playerTier = 4, targetTier = 4;
        if (player.hasPermission("wayward.moderation.vanish.see.tier3")) {
            playerTier = 3;
        }
        if (target.hasPermission("wayward.moderation.vanish.see.tier3")) {
            targetTier = 3;
        }
        if (player.hasPermission("wayward.moderation.vanish.see.tier2")) {
            playerTier = 2;
        }
        if (target.hasPermission("wayward.moderation.vanish.see.tier2")) {
            targetTier = 2;
        }
        if (player.hasPermission("wayward.moderation.vanish.see.tier1")) {
            playerTier = 1;
        }
        if (target.hasPermission("wayward.moderation.vanish.see.tier1")) {
            targetTier = 1;
        }
        return playerTier <= targetTier;
    }

}
