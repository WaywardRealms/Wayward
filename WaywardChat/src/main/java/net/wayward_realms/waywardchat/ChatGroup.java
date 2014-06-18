package net.wayward_realms.waywardchat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ChatGroup {

    private WaywardChat plugin;

    private String name;
    private Set<UUID> players = new HashSet<>();
    private Set<UUID> invited = new HashSet<>();

    private long lastUsed;

    public ChatGroup(WaywardChat plugin, String name, Player... players) {
        this.plugin = plugin;
        this.name = name.toLowerCase();
        for (Player player : players) {
            this.players.add(player.getUniqueId());
        }
    }

    public String getName() {
        return name;
    }

    public Set<UUID> getPlayers() {
        return players;
    }

    public Set<UUID> getInvited() {
        return invited;
    }

    public boolean isInvited(Player player) {
        return invited.contains(player.getUniqueId());
    }

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

    public void addPlayer(Player player) {
        players.add(player.getUniqueId());
        invited.remove(player.getUniqueId());
    }

    public void removePlayer(Player player) {
        players.remove(player.getUniqueId());
        invited.add(player.getUniqueId());
    }

    public void invitePlayer(Player player) {
        invited.add(player.getUniqueId());
    }

    public void uninvitePlayer(Player player) {
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
            plugin.removeChatGroup(getName());
        }
    }

}
