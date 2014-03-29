package net.wayward_realms.waywardchat.irc;

import net.wayward_realms.waywardchat.WaywardChat;
import net.wayward_realms.waywardlib.chat.Channel;
import org.bukkit.ChatColor;
import org.pircbotx.User;

public class IrcBroadcastCommand extends IrcCommand {

    private WaywardChat plugin;

    public IrcBroadcastCommand(WaywardChat plugin) {
        super("broadcast");
        this.plugin = plugin;
    }

    @Override
    public void execute(User sender, IrcCommand cmd, String label, String[] args) {
        if (sender.isIrcop()) {
            StringBuilder messageBuilder = new StringBuilder();
            for (String arg : args) {
                messageBuilder.append(arg).append(" ");
            }
            String message = ChatColor.GRAY + "[" + ChatColor.GREEN + "BROADCAST" + ChatColor.GRAY + "]" + ChatColor.WHITE + messageBuilder.toString();
            plugin.getServer().broadcastMessage(message);
            for (Channel channel : plugin.getChannels()) {
                if (channel.isIrcEnabled()) {
                    plugin.getIrcBot().sendIRC().message(channel.getIrcChannel(), ChatColor.stripColor(message));
                }
            }
        }
    }

}
