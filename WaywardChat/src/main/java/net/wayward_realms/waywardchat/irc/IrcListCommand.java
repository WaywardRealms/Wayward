package net.wayward_realms.waywardchat.irc;

import net.wayward_realms.waywardchat.VaylerynChat;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.pircbotx.User;

public class IrcListCommand extends IrcCommand {

    private VaylerynChat plugin;

    public IrcListCommand(VaylerynChat plugin) {
        super("list");
        this.plugin = plugin;
    }

    @Override
    public void execute(User sender, IrcCommand cmd, String label, String[] args) {
        sender.send().message(ChatColor.stripColor(plugin.getPrefix()) + "Online players: ");
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            sender.send().message(player.getName());
        }
    }

}
