package net.wayward_realms.waywardchat;

import net.wayward_realms.waywardlib.chat.Channel;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class QuickChannelSwitchCommand extends Command {

    private VaylerynChat plugin;
    private Channel channel;

    public QuickChannelSwitchCommand(String name, String description, List<String> aliases, VaylerynChat plugin, Channel channel) {
        super(name, description, "/<command> [message]", aliases);
        this.plugin = plugin;
        this.channel = channel;
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender.hasPermission("wayward.chat.ch.talkin." + this.channel.getName())) {
            Channel channel = plugin.getPlayerChannel((Player) sender);
            plugin.setPlayerChannel((Player) sender, this.channel);
            if (args.length >= 1) {
                String message = "";
                for (String arg : args) {
                    message += arg + " ";
                }
                plugin.handleChat((Player) sender, message);
                plugin.setPlayerChannel((Player) sender, channel);
            } else {
                sender.sendMessage(plugin.getPrefix() + this.channel.getColour() + "Now talking in " + this.channel.getName() + ".");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission!");
        }
        return true;
    }

}
