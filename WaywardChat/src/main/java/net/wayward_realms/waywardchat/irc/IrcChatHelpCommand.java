package net.wayward_realms.waywardchat.irc;

import net.wayward_realms.waywardchat.WaywardChat;
import org.bukkit.ChatColor;
import org.pircbotx.Channel;
import org.pircbotx.User;

public class IrcChatHelpCommand extends IrcCommand {

    private WaywardChat plugin;

    public IrcChatHelpCommand(WaywardChat plugin) {
        super("chathelp");
        this.plugin = plugin;
    }

    @Override
    public void execute(Channel channel, User sender, IrcCommand cmd, String label, String[] args) {
        sender.send().message(ChatColor.stripColor(plugin.getPrefix()) + "Chat help:");
        sender.send().message("!ch list: Lists channels");
    }

}
