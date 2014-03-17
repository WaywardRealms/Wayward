package net.wayward_realms.waywardchat.irc;

import net.wayward_realms.waywardchat.WaywardChat;
import net.wayward_realms.waywardlib.chat.Channel;
import org.bukkit.ChatColor;
import org.pircbotx.User;

public class IrcChCommand extends IrcCommand {

    private WaywardChat plugin;

    public IrcChCommand(WaywardChat plugin) {
        super("ch");
        this.plugin = plugin;
    }

    @Override
    public void execute(User sender, IrcCommand cmd, String label, String[] args) {
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("list")) {
                sender.send().message(ChatColor.stripColor(plugin.getPrefix()) + "Channel list: ");
                for (Channel channel : plugin.getChannels()) {
                    if (channel.isIrcEnabled()) {
                        sender.send().message(channel.getName());
                        sender.send().message(" - Format: " + channel.getFormat());
                    }
                }
            } else {
                sender.send().message(ChatColor.stripColor(plugin.getPrefix()) + "Incorrect usage! Use !chathelp for help.");
            }
        } else {
            sender.send().message(ChatColor.stripColor(plugin.getPrefix()) + "Incorrect usage! Use !chathelp for help.");
        }
    }

}
