package net.wayward_realms.waywardchat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class ChatGroup {

    private WaywardChat plugin;

    private String name;
    private Set<String> players = new HashSet<>();
    private Set<String> invited = new HashSet<>();

    private long lastUsed;

    public ChatGroup(WaywardChat plugin, String name, Player... players) {
        this.plugin = plugin;
        this.name = name.toLowerCase();
        for (Player player : players) {
            this.players.add(player.getName());
        }
    }

    public String getName() {
        return name;
    }

    public Set<String> getPlayers() {
        return players;
    }

    public Set<String> getInvited() {
        return invited;
    }

    public boolean isInvited(Player player) {
        return invited.contains(player.getName());
    }

    public void sendMessage(Player sender, String message) {
        lastUsed = System.currentTimeMillis();
        String format = ChatColor.WHITE + "[" + ChatColor.DARK_GRAY + (name.startsWith("_pm_") ? "private message" : name) + ChatColor.WHITE + "] " + ChatColor.GRAY + sender.getName() + ": " + message;
        for (String player : players) {
            if (plugin.getServer().getPlayerExact(player) != null) {
                plugin.getServer().getPlayerExact(player).sendMessage(format);
            }
        }
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (plugin.isSnooping(player)) player.sendMessage(format);
        }
    }

    public void addPlayer(Player player) {
        players.add(player.getName());
        invited.remove(player.getName());
    }

    public void removePlayer(Player player) {
        players.remove(player.getName());
        invited.add(player.getName());
    }

    public void invitePlayer(Player player) {
        invited.add(player.getName());
    }

    public void uninvitePlayer(Player player) {
        invited.remove(player.getName());
    }

    public void disposeIfUnused() {
        boolean playersOffline = true;
        for (String player : players) {
            if (plugin.getServer().getPlayerExact(player) != null) {
                playersOffline = false;
            }
        }
        if (playersOffline || System.currentTimeMillis() - lastUsed > 1800000) {
            plugin.removeChatGroup(getName());
        }
    }

}
