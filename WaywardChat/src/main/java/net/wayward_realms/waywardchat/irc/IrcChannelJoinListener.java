package net.wayward_realms.waywardchat.irc;

import net.wayward_realms.waywardchat.WaywardChat;
import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;

public class IrcChannelJoinListener extends ListenerAdapter<PircBotX>{

    private WaywardChat plugin;

    public IrcChannelJoinListener(WaywardChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onJoin(JoinEvent<PircBotX> event) {
        User thisuser = event.getUser();
        Channel thischannel = event.getChannel();
        boolean verified = thisuser.isVerified();
        //Start Auto Op/HOp/Voice Block
        for(String channelOps: plugin.getConfig().getStringList("channels." + thischannel + ".hops")){
            if((channelOps.equalsIgnoreCase(thisuser.getNick()))){
                if(verified){
                    if(!(thisuser.getChannelsOpIn().contains(thischannel))) {
                        event.getBot().sendIRC().message(thischannel.getName(), "/cs op " + thischannel.getName() + " " + thisuser.getNick());
                    }
                }
            }
        }
        for(String channelHOps: plugin.getConfig().getStringList("channels." + event.getChannel() + ".hops")){
            if((channelHOps.equalsIgnoreCase(thisuser.getNick()))){
                if(verified){
                    if(!(thisuser.getChannelsHalfOpIn().contains(thischannel))) {
                        event.getBot().sendIRC().message(thischannel.getName(), "/cs hop " + thischannel.getName() + " " + thisuser.getNick());
                    }
                }
            }
        }
        for(String channelVoice: plugin.getConfig().getStringList("channels." + event.getChannel() + ".voice")){
            if((channelVoice.equalsIgnoreCase(thisuser.getNick()))){
                if(verified){
                    if(!(thisuser.getChannelsVoiceIn().contains(thischannel))) {
                        event.getBot().sendIRC().message(thischannel.getName(), "/cs voice " + thischannel.getName() + " " + thisuser.getNick());
                    }
                }
            }
        }
        //End Auto Op
        //Start Whitelist Block
        if(plugin.getConfig().getBoolean("channel." + thischannel.getName() + ".whitelist")){
            if(!verified){
                event.getBot().sendIRC().message(thischannel.getName(), "/kick " + thischannel.getName() + " " + thisuser.getNick() + " Only registered/Identified users may join this channel.");
            }else if(!(thisuser.getChannelsVoiceIn().contains(thischannel) || thisuser.getChannelsHalfOpIn().contains(thischannel) || thisuser.getChannelsOpIn().contains(thischannel))){
                event.getBot().sendIRC().message(thischannel.getName(), "/kick " + thischannel.getName() + " " + thisuser.getNick() + " Only authorized users may join this channel.");
            }
        }
        //End Whitelist Block
    }

}
