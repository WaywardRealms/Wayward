package net.wayward_realms.waywardchat.irc;

import net.wayward_realms.waywardchat.VaylerynChat;
import org.bukkit.ChatColor;
import org.pircbotx.User;

public class IrcChatHelpCommand extends IrcCommand {

    private VaylerynChat plugin;

    public IrcChatHelpCommand(VaylerynChat plugin) {
        super("chathelp");
        this.plugin = plugin;
    }

    @Override
    public void execute(User sender, IrcCommand cmd, String label, String[] args) {
        sender.send().message(ChatColor.stripColor(plugin.getPrefix()) + "Chat help:");
        sender.send().message("!ch list: Lists channels");
        sender.send().message("!ch talkin [channel]: Makes you talk in the given channel");
        sender.send().message("!ch mute [channel]: Mutes a certain channel");
        sender.send().message("!ch listen [channel]: Unmutes a certain channel");
    }

}
