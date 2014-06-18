package net.wayward_realms.waywardchat;

import net.wayward_realms.waywardlib.chat.Channel;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class IrcCommand implements CommandExecutor {

    private WaywardChat plugin;

    public IrcCommand(WaywardChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("list")) {
                sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Users on IRC:");
                for (Channel channel : plugin.getChannels()) {
                    if (channel.getIrcChannel() != null) {
                        sender.sendMessage(ChatColor.GRAY + channel.getIrcChannel() + ":");
                        for (String user : plugin.getUsersInIrcChannel(channel.getIrcChannel())) {
                            sender.sendMessage(ChatColor.GREEN + user);
                        }
                    }
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /" + label + " [list]");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /" + label + " [list]");
        }
        return true;
    }
}
