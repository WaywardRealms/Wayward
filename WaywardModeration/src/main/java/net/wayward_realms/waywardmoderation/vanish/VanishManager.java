package net.wayward_realms.waywardmoderation.vanish;

import net.wayward_realms.waywardmoderation.WaywardModeration;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class VanishManager {

    private WaywardModeration plugin;
    private Set<UUID> vanished = new HashSet<>();

    public VanishManager(WaywardModeration plugin) {
        this.plugin = plugin;
    }

    public boolean isVanished(Player player) {
        return vanished.contains(player.getUniqueId());
    }

    public void setVanished(Player player, boolean vanished) {
        if (vanished) {
            this.vanished.add(player.getUniqueId());
            for (Player player1 : plugin.getServer().getOnlinePlayers()) {
                if (!canSee(player1, player)) player1.hidePlayer(player);
            }
        } else {
            this.vanished.remove(player.getUniqueId());
            for (Player player1 : plugin.getServer().getOnlinePlayers()) {
                player1.showPlayer(player);
            }
        }
    }

    public Set<Player> getVanishedPlayers() {
        Set<Player> vanishedPlayers = new HashSet<>();
        for (UUID uuid : vanished) {
            if (plugin.getServer().getOfflinePlayer(uuid).isOnline()) {
                vanishedPlayers.add(plugin.getServer().getPlayer(uuid));
            }
        }
        return vanishedPlayers;
    }

    public boolean canSee(Player player, Player target) {
        if (!vanished.contains(target.getUniqueId())) return true;
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
