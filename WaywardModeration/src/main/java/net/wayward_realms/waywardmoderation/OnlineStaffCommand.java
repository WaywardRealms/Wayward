package net.wayward_realms.waywardmoderation;

import net.wayward_realms.waywardlib.chat.Channel;
import net.wayward_realms.waywardlib.chat.ChatPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class OnlineStaffCommand implements CommandExecutor {

    private WaywardModeration plugin;

    public OnlineStaffCommand(WaywardModeration plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Online staff:");
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (player.hasPermission("wayward.moderation.staff")) {
                sender.sendMessage(ChatColor.GREEN + player.getName());
            }
        }

        RegisteredServiceProvider<ChatPlugin> chatPluginProvider = plugin.getServer().getServicesManager().getRegistration(ChatPlugin.class);
        if (chatPluginProvider != null) {
            ChatPlugin chatPlugin = chatPluginProvider.getProvider();
            sender.sendMessage(ChatColor.GRAY + "----------------");
            sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Staff in IRC:");
            for (Channel channel : chatPlugin.getChannels()) {
                if (channel.getIrcChannel() != null) {
                    sender.sendMessage(ChatColor.GRAY + channel.getIrcChannel() + ":");
                    for (String user : chatPlugin.getStaffInIrcChannel(channel.getIrcChannel())) {
                        sender.sendMessage(ChatColor.GREEN + user);
                    }
                }
            }
        }
        return true;
    }

}
