package net.wayward_realms.waywardchat.irc;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public abstract class IrcCommand extends ListenerAdapter<PircBotX> {

    private String name;

    public IrcCommand(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void onMessage(MessageEvent<PircBotX> event) {
        if (event.getMessage().startsWith("!" + getName() + " ") || event.getMessage().replace("!" + getName(), "").isEmpty()) {
            String[] args = new String[0];
            if (event.getMessage().contains(" ")) {
                args = new String[event.getMessage().split(" ").length - 1];
                for (int i = 1; i < event.getMessage().split(" ").length; i++) {
                    args[i - 1] = event.getMessage().split(" ")[i];
                }
            }
            execute(event.getChannel(), event.getUser(), this, event.getMessage().contains(" ") ? event.getMessage().split(" ")[0].replace("!", "") : event.getMessage().replace("!", ""), args);
        }
    }

    public abstract void execute(Channel channel, User sender, IrcCommand cmd, String label, String[] args);

}
