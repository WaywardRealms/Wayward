package net.wayward_realms.waywardessentials;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private WaywardEssentials plugin;

    public PlayerJoinListener(WaywardEssentials plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        for (Player player : plugin.getPlayersWithLogMessagesEnabled()) {
            player.sendMessage(event.getJoinMessage());
        }
        event.setJoinMessage("");
        event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Welcome to " + plugin.getServer().getServerName() + "!");
        if (!event.getPlayer().hasPlayedBefore()) {
            event.getPlayer().teleport(event.getPlayer().getWorld().getSpawnLocation());
            event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.GREEN + "It appears this is your first time playing here.");
            event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Feel free to ask for help by using /support [message]");
            event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Quick in-game command references can be found by using /help, or for a specific plugin, /help [plugin]");
            event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.GREEN + "More comprehensive help can be found at:");
            event.getPlayer().sendMessage("" + ChatColor.BLUE + ChatColor.UNDERLINE + "http://waywardrealms.enjin.com/howtoplay");
            event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.GREEN + "We hope you enjoy your time roleplaying with us!");
            plugin.getServer().broadcastMessage(plugin.getPrefix() + ChatColor.GREEN + event.getPlayer().getName() + " just joined for the first time, be sure to give them a warm welcome to " + plugin.getServer().getServerName() + "!");
        }
    }

}
