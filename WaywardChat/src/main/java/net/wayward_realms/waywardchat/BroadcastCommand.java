package net.wayward_realms.waywardchat;

import net.wayward_realms.waywardlib.chat.Channel;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BroadcastCommand implements CommandExecutor {

    private WaywardChat plugin;

    public BroadcastCommand(WaywardChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("wayward.chat.command.broadcast")) {
            StringBuilder messageBuilder = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                messageBuilder.append(args[i]).append(" ");
            }
            String message = ChatColor.GRAY + "[" + ChatColor.GREEN + "BROADCAST" + ChatColor.GRAY + "]" + ChatColor.WHITE + messageBuilder.toString();
            plugin.getServer().broadcastMessage(message);
            for (Channel channel : plugin.getChannels()) {
                if (channel.isIrcEnabled()) {
                    plugin.getIrcBot().sendIRC().message(channel.getIrcChannel(), ChatColor.stripColor(message));
                }
            }
        }
        return true;
    }

}
