package net.wayward_realms.waywardchat;

import net.wayward_realms.waywardlib.chat.Channel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class QuickChannelSwitchCommand implements CommandExecutor {

    private WaywardChat plugin;

    public QuickChannelSwitchCommand(WaywardChat inplugin){
        this.plugin = inplugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("wayward.chat.ch.talkin." + cmd.getName())) {
            Channel channel = plugin.getPlayerChannel((Player) sender);
            plugin.setPlayerChannel((Player) sender, plugin.getChannel(cmd.getName()));
            if (args.length >= 1) {
                String message = "";
                for (String arg : args) {
                    message += arg + " ";
                }
                sender.sendMessage(plugin.getPrefix() + channel.getColour() + "Now talking in " + channel.getName() + ".");
                Bukkit.getServer().getPluginManager().callEvent(new AsyncPlayerChatEvent(true, (Player) sender, message, null));
            } else {
                sender.sendMessage(plugin.getPrefix() + channel.getColour() + "Now talking in " + channel.getName() + ".");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission!");
        }
        return true;
    }

}
