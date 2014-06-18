package net.wayward_realms.waywardchat.irc;

import net.wayward_realms.waywardchat.WaywardChat;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;

public class IrcChannelJoinListener extends ListenerAdapter<PircBotX>{

    private WaywardChat plugin;

    public IrcChannelJoinListener(WaywardChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onJoin(JoinEvent<PircBotX> event) {
        boolean verified = event.getUser().isVerified();
        //Start Auto Op/HOp/Voice Block
        for (String channelOps: plugin.getConfig().getStringList("channels." + event.getChannel().getName() + ".hops")){
            if ((channelOps.equalsIgnoreCase(event.getUser().getNick()))){
                if (verified){
                    if (!(event.getUser().getChannelsOpIn().contains(event.getChannel()))) {
                        event.getBot().sendIRC().message("ChanServ", "OP " + event.getChannel().getName() + " " + event.getUser().getNick());
                    }
                }
            }
        }
        for (String channelHOps: plugin.getConfig().getStringList("channels." + event.getChannel().getName() + ".hops")){
            if ((channelHOps.equalsIgnoreCase(event.getUser().getNick()))){
                if (verified){
                    if (!(event.getUser().getChannelsHalfOpIn().contains(event.getChannel()))) {
                        event.getBot().sendIRC().message("ChanServ", "HALFOP " + event.getChannel().getName() + " " + event.getUser().getNick());
                    }
                }
            }
        }
        for (String channelVoice: plugin.getConfig().getStringList("channels." + event.getChannel() + ".voice")){
            if ((channelVoice.equalsIgnoreCase(event.getUser().getNick()))){
                if (verified){
                    if (!(event.getUser().getChannelsVoiceIn().contains(event.getChannel()))) {
                        event.getBot().sendIRC().message("ChanServ", "VOICE " + event.getChannel().getName() + " " + event.getUser().getNick());
                    }
                }
            }
        }
        //End Auto Op
        //Start Whitelist Block
        if (plugin.getConfig().getBoolean("channels." + event.getChannel().getName() + ".whitelist")){
            if (!verified){
                event.getBot().sendIRC().message(event.getChannel().getName(), "/kick " + event.getChannel().getName() + " " + event.getUser().getNick() + " Only registered/identified users may join this channel.");
                event.getChannel().send().message(event.getUser().getNick() + " attempted to join, but was not registered.");
            } else if (!(event.getUser().getChannelsVoiceIn().contains(event.getChannel()) || event.getUser().getChannelsHalfOpIn().contains(event.getChannel()) || event.getUser().getChannelsOpIn().contains(event.getChannel()))){
                event.getBot().sendIRC().message(event.getChannel().getName(), "/kick " + event.getChannel().getName() + " " + event.getUser().getNick() + " Only authorised users may join this channel.");
                event.getChannel().send().message(event.getUser().getNick() + " attempted to join, but was not authorised.");
            }
        }
        //End Whitelist Block
    }

}
