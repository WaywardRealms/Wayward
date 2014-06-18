package net.wayward_realms.waywardchat.irc;

import net.wayward_realms.waywardchat.ChannelImpl;
import net.wayward_realms.waywardchat.WaywardChat;
import net.wayward_realms.waywardlib.chat.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.UserListEvent;

public class IrcUserListListener extends ListenerAdapter<PircBotX> {

    private WaywardChat plugin;

    public IrcUserListListener(WaywardChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onUserList(UserListEvent<PircBotX> event) {
        plugin.getLogger().info("Recieved user list for " + event.getChannel().getName());
        Channel channel = plugin.getChannelFromIrcChannel(event.getChannel().getName());
        if (channel != null && channel instanceof ChannelImpl) {
            ((ChannelImpl) channel).setIrcChannelObject(event.getChannel());
            plugin.getLogger().info("Set IRC channel instance for '" + channel.getName() + "' to IRC channel '" + event.getChannel().getName() + "'");
        }
    }

}
