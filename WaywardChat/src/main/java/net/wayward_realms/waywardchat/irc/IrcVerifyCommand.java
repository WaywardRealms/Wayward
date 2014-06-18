package net.wayward_realms.waywardchat.irc;

import net.wayward_realms.waywardchat.WaywardChat;
import org.pircbotx.Channel;
import org.pircbotx.User;

public class IrcVerifyCommand extends IrcCommand {

    private WaywardChat plugin;

    public IrcVerifyCommand(WaywardChat plugin) {
        super("verify");
        this.plugin = plugin;
    }

    @Override
    public void execute(Channel channel, User sender, IrcCommand cmd, String label, String[] args) {
        if (args.length > 0) {
            plugin.getIrcBot().sendIRC().message("NickServ", "VERIFY REGISTER " + plugin.getIrcBot().getNick() + " " + args[0]);
            sender.send().message("Attempted to verify. If you correctly entered the verification code, I should now be verified.");
        } else {
            sender.send().message("You must specify the registration code from your email.");
        }
    }

}
