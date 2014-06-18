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
    public void execute(org.pircbotx.Channel channel, User sender, IrcCommand cmd, String label, String[] args) {
        if (sender.getChannelsOpIn().contains(channel)
                || sender.getChannelsHalfOpIn().contains(channel)
                || sender.getChannelsSuperOpIn().contains(channel)
                || sender.getChannelsOwnerIn().contains(channel)
                || sender.getChannelsVoiceIn().contains(channel)) {
            StringBuilder messageBuilder = new StringBuilder();
            for (String arg : args) {
                messageBuilder.append(arg).append(" ");
            }
            String message = ChatColor.GRAY + "[" + ChatColor.GREEN + "BROADCAST" + ChatColor.GRAY + "] " + ChatColor.WHITE + messageBuilder.toString();
            plugin.getServer().broadcastMessage(message);
            for (Channel channel1 : plugin.getChannels()) {
                if (channel1.isIrcEnabled()) {
                    plugin.getIrcBot().sendIRC().message(channel1.getIrcChannel(), ChatColor.stripColor(message));
                }
            }
        }
    }

}
