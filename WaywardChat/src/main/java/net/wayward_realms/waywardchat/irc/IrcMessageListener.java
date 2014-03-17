package net.wayward_realms.waywardchat.irc;

import net.wayward_realms.waywardchat.WaywardChat;
import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class IrcMessageListener extends ListenerAdapter<PircBotX> {

    private WaywardChat plugin;

    public IrcMessageListener(WaywardChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onMessage(MessageEvent<PircBotX> event) {
        final User user = event.getUser();
        final Channel channel = event.getChannel();
        final String message = event.getMessage();
        if (user != plugin.getIrcBot().getUserBot()) {
            if (!message.startsWith("!")) {
                plugin.handleChat(user, channel, message);
            }
        }
    }

}
