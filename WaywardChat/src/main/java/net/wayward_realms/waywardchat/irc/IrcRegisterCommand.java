package net.wayward_realms.waywardchat.irc;

import net.wayward_realms.waywardchat.WaywardChat;
import org.pircbotx.Channel;
import org.pircbotx.User;

public class IrcRegisterCommand extends IrcCommand {

    private WaywardChat plugin;

    public IrcRegisterCommand(WaywardChat plugin) {
        super("register");
        this.plugin = plugin;
    }

    @Override
    public void execute(Channel channel, User sender, IrcCommand cmd, String label, String[] args) {
        if (args.length > 0) {
            if (args[0].matches("(\\w[-._\\w]*\\w@\\w[-._\\w]*\\w\\.\\w{2,3})")) {
                if (plugin.getIrcConfig().get("password") != null) {
                    plugin.getIrcBot().sendIRC().message("NickServ", "REGISTER " + plugin.getIrcConfig().getString("password") + " " + args[0]);
                    sender.send().message("Registered. Please check your email for the verification code to complete the verification process.");
                } else {
                    sender.send().message("Could not retrieve password from IRC config.");
                }
            } else {
                sender.send().message("You did not specify a valid email.");
            }
        } else {
            sender.send().message("You must specify an email.");
        }
    }

}
