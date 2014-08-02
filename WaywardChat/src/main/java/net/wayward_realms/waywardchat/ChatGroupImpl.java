package net.wayward_realms.waywardchat;

import net.wayward_realms.waywardlib.chat.ChatGroup;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ChatGroupImpl implements ChatGroup {

    private WaywardChat plugin;

    private String name;
    private Set<UUID> players = new HashSet<>();
    private Set<UUID> invited = new HashSet<>();

    private long lastUsed;

    public ChatGroupImpl(WaywardChat plugin, String name, Player... players) {
        this.plugin = plugin;
        this.name = name.toLowerCase();
        for (Player player : players) {
            this.players.add(player.getUniqueId());
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Collection<OfflinePlayer> getPlayers() {
        Set<OfflinePlayer> players = new HashSet<>();
        for (UUID uuid : getPlayerUUIDs()) {
            players.add(Bukkit.getOfflinePlayer(uuid));
        }
        return players;
    }

    public Set<UUID> getPlayerUUIDs() {
        return players;
    }

    public Set<UUID> getInvitedUUIDs() {
        return invited;
    }

    @Override
    public boolean isInvited(Player player) {
        return invited.contains(player.getUniqueId());
    }

    @Override
    public void sendMessage(Player sender, String message) {
        lastUsed = System.currentTimeMillis();
        String format = ChatColor.WHITE + "[" + ChatColor.DARK_GRAY + (name.startsWith("_pm_") ? "private message" : name) + ChatColor.WHITE + "] " + ChatColor.GRAY + sender.getName() + ": " + message;
        for (UUID player : players) {
            if (plugin.getServer().getPlayer(player) != null) {
                plugin.getServer().getPlayer(player).sendMessage(format);
            }
        }
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (plugin.isSnooping(player)) player.sendMessage(format);
        }
    }

    @Override
    public void addPlayer(OfflinePlayer player) {
        players.add(player.getUniqueId());
        invited.remove(player.getUniqueId());
    }

    @Override
    public void removePlayer(OfflinePlayer player) {
        players.remove(player.getUniqueId());
        invited.add(player.getUniqueId());
    }

    @Override
    public Collection<OfflinePlayer> getInvited() {
        Set<OfflinePlayer> players = new HashSet<>();
        for (UUID uuid : getInvitedUUIDs()) {
            players.add(Bukkit.getOfflinePlayer(uuid));
        }
        return players;
    }

    @Override
    public void invite(OfflinePlayer player) {
        invited.add(player.getUniqueId());
    }

    @Override
    public void uninvite(OfflinePlayer player) {
        invited.remove(player.getUniqueId());
    }

    public void disposeIfUnused() {
        boolean playersOffline = true;
        for (UUID player : players) {
            if (plugin.getServer().getPlayer(player) != null) {
                playersOffline = false;
            }
        }
        if (playersOffline || System.currentTimeMillis() - lastUsed > 1800000) {
            plugin.removeChatGroup(this);
        }
    }

}
